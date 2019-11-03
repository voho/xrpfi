package fi.xrp.fletcher.service;

import com.google.common.collect.Lists;
import fi.xrp.fletcher.model.api.NewsProducerStatus;
import fi.xrp.fletcher.model.source.NewsProducer;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
public class DefaultNewsProducerStatus implements fi.xrp.fletcher.model.source.NewsProducerStatus {
    private final Map<NewsProducer, NewsProducerStatus> statuses = new HashMap<>();

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
    public void onUpdateFinished(final NewsProducer producer) {
        log.debug("DB Update Finished: {}", producer.getTitle());
        final NewsProducerStatus status = getStatus(producer);
        status.setLastUpdateEndDate(System.currentTimeMillis());
        status.setLastError(null);
        status.setStatus("DB_UPDATE_FINISHED");
    }

    @Override
    public void onUpdateFailed(final NewsProducer producer, final Object reason) {
        log.warn("Update failed: {}", producer.getTitle());
        final NewsProducerStatus status = getStatus(producer);
        status.setLastUpdateEndDate(System.currentTimeMillis());
        status.setLastError(Objects.toString(reason));
        status.setStatus("FAILED");
    }
}
