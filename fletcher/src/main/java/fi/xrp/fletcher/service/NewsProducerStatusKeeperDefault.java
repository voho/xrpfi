package fi.xrp.fletcher.service;

import com.google.common.collect.Lists;
import fi.xrp.fletcher.model.api.GlobalStatus;
import fi.xrp.fletcher.model.api.NewsProducerStatus;
import fi.xrp.fletcher.model.source.NewsProducer;
import fi.xrp.fletcher.service.aws.CustomMetricsClient;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
public class NewsProducerStatusKeeperDefault implements NewsProducerStatusKeeper {
    private final Map<NewsProducer, NewsProducerStatus> statuses = new HashMap<>();

    private long startTime;
    private long endTime;

    private final @NonNull CustomMetricsClient customMetricsClient;

    @Override
    public List<NewsProducerStatus> getStatuses() {
        return Lists.newArrayList(statuses.values());
    }

    @Override
    public NewsProducerStatus getStatus(final NewsProducer producer) {
        return statuses.computeIfAbsent(producer, (p) -> new NewsProducerStatus());
    }

    @Override
    public void onUpdateStarted(final NewsProducer producer) {
        log.debug("Update Started: {}", producer.getTitle());
        final NewsProducerStatus status = getStatus(producer);
        status.setLastUpdateStartDate(System.currentTimeMillis());
        status.setHomeUrl(producer.getHomeUrl());
        status.setFeedUrl(producer.getFeedUrl());
        status.setTitle(producer.getTitle());
        status.setStatus("UPDATE_STARTED");
    }

    @Override
    public void onUpdateFinished(final NewsProducer producer, final long newsCount) {
        log.debug("DB Update Finished: {}", producer.getTitle());
        final NewsProducerStatus status = getStatus(producer);
        status.setLastUpdateEndDate(System.currentTimeMillis());
        status.setLastUpdateNewsCount(newsCount);
        status.setStatus("DB_UPDATE_FINISHED");

        for (final NewsProducer.Tag tag : producer.getTags()) {
            customMetricsClient.emitAfterUpdateNewsMetrics(
                    tag.name(),
                    producer.getId(),
                    status.getLastUpdateEndDate() - status.getLastUpdateStartDate(),
                    true);
        }
    }

    @Override
    public void onUpdateFailed(final NewsProducer producer, final Object reason) {
        log.warn("Update failed: {}", producer.getTitle());
        final NewsProducerStatus status = getStatus(producer);
        status.setLastUpdateFailureDate(System.currentTimeMillis());
        status.setLastError(Objects.toString(reason));
        status.setStatus("FAILED");

        for (final NewsProducer.Tag tag : producer.getTags()) {
            customMetricsClient.emitAfterUpdateNewsMetrics(
                    tag.name(),
                    producer.getId(),
                    status.getLastUpdateFailureDate() - status.getLastUpdateStartDate(),
                    false);
        }
    }

    @Override
    public void onGlobalStart() {
        startTime = System.currentTimeMillis();
    }

    @Override
    public void onGlobalFinished() {
        endTime = System.currentTimeMillis();
    }

    @Override
    public GlobalStatus getGlobalStatus() {
        final GlobalStatus status = new GlobalStatus();
        status.setStartTime(startTime);
        status.setEndTime(endTime);
        status.setTotalNewsProducers(statuses.size());
        status.setTotalNews(statuses.values().stream().map(NewsProducerStatus::getLastUpdateNewsCount).filter(Objects::nonNull).mapToLong(k -> k.longValue()).sum());
        return status;
    }
}
