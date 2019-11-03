package fi.xrp.fletcher.model.source;

import java.util.List;

public interface NewsProducerStatus {
    List<fi.xrp.fletcher.model.api.NewsProducerStatus> getStatuses();

    fi.xrp.fletcher.model.api.NewsProducerStatus getStatus(NewsProducer producer);

    void onUpdateStarted(NewsProducer producer);

    void onUpdateFinished(NewsProducer producer);

    void onUpdateFailed(NewsProducer producer, Object reason); // TODO better type
}
