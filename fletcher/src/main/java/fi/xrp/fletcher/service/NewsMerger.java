package fi.xrp.fletcher.service;

import fi.xrp.fletcher.model.api.News;

import java.util.List;

public interface NewsMerger {
    List<News> getMergedNews();

    void updateNews(List<News> news);
}
