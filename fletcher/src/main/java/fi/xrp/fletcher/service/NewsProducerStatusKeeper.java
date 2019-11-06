package fi.xrp.fletcher.service;

import fi.xrp.fletcher.model.api.GlobalStatus;
import fi.xrp.fletcher.model.api.NewsProducerStatus;
import fi.xrp.fletcher.model.source.NewsProducer;

import java.util.List;

public interface NewsProducerStatusKeeper {
    List<NewsProducerStatus> getStatuses();

    NewsProducerStatus getStatus(NewsProducer producer);

    void onUpdateStarted(NewsProducer producer);

    void onUpdateFinished(NewsProducer producer);

    void onUpdateFailed(NewsProducer producer, Object reason); // TODO better type

    GlobalStatus getGlobalStatus();
}
