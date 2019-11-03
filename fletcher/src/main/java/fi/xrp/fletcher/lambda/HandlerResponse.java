package fi.xrp.fletcher.lambda;

import fi.xrp.fletcher.model.api.News;
import fi.xrp.fletcher.model.api.NewsSourceMeta;
import lombok.Value;

import java.util.List;

@Value
public class HandlerResponse {
    List<News> news;
    List<NewsSourceMeta> meta;
}
