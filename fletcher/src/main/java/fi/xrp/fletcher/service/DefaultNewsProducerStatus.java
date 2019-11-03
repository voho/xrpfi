package fi.xrp.fletcher.service;

import fi.xrp.fletcher.model.source.NewsProducer;
import fi.xrp.fletcher.model.source.NewsProducerStatus;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DefaultNewsProducerStatus implements NewsProducerStatus {
    @Override
    public void onUpdateStarted(final NewsProducer producer) {
        log.info("Update Started: {}", producer.getTitle());
    }

    @Override
    public void onDatabaseUpdateStarted(final NewsProducer producer) {
        //log.info("DB Update Started: {}", producer.getTitle());
    }

    @Override
    public void onDatabaseUpdateFinished(final NewsProducer producer) {
        //log.info("DB Update Finished: {}", producer.getTitle());
    }

    @Override
    public void onUpdateFailed(final NewsProducer producer, final Object reason) {
        log.error("Update failed: {}", producer.getTitle());
    }
}
