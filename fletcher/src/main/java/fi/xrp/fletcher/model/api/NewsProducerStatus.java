package fi.xrp.fletcher.model.api;

import lombok.Data;

@Data
public class NewsProducerStatus {
    private String homeUrl;
    private String feedUrl;
    private String title;
    private long lastUpdateStartDate;
    private long lastUpdateEndDate;
    private long lastUpdateNewsCount;
    private String lastError;
    private String status;
}
