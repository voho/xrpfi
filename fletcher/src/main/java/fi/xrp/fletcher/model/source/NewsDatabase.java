package fi.xrp.fletcher.model.source;

import fi.xrp.fletcher.model.api.News;

import java.util.List;

@Deprecated
public interface NewsDatabase {
    List<News> getMergedNews();

    void setNews(List<News> news);
}
