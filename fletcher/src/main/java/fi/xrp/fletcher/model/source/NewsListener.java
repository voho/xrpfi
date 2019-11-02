package fi.xrp.fletcher.model.source;

import fi.xrp.fletcher.model.News;

import java.util.List;

public interface NewsListener {
    void onUpdateStarted(NewsProducer newsProducer);

    void onUpdateCompleted(NewsProducer newsProducer, List<News> news);

    void onUpdateFailed(NewsProducer newsProducer, Throwable error);
}
