package fi.xrp.fletcher.model.source;

public interface NewsProducerStatus {
    void onUpdateStarted(NewsProducer producer);

    void onDatabaseUpdateStarted(NewsProducer producer);

    void onDatabaseUpdateFinished(NewsProducer producer);

    void onUpdateFailed(NewsProducer producer, Object reason); // TODO better type
}
