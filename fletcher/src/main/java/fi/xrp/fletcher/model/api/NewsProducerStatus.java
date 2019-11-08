package fi.xrp.fletcher.model.api;

import lombok.Data;

@Data
public class NewsProducerStatus {
    private String homeUrl;
    private String feedUrl;
    private String title;
    private Long lastUpdateStartDate;
    private Long lastUpdateEndDate;
    private Long lastUpdateFailureDate;
    private Long lastUpdateNewsCount;
    private String lastError;
    private String status;
}
