package fi.xrp.fletcher.model.api;

import fi.xrp.fletcher.model.source.NewsProducer;
import lombok.Data;

@Data
public class NewsRequest {
    private NewsProducer.Mode mode;
}
