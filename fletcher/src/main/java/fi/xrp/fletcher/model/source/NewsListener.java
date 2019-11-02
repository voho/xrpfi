package fi.xrp.fletcher.model.source;

public interface NewsListener {
    void onUpdateStarted(NewsProducer newsProducer);

    void onUpdateCompleted(NewsProducer newsProducer);

    void onUpdateFailed(NewsProducer newsProducer, Throwable error);
}
