package fi.xrp.fletcher.model.api;

import lombok.Data;

@Data
public class NewsSourceMeta {
    private String homeUrl;
    private String feedUrl;
    private String title;
    private long lastUpdateStartDate;
    private long lastUpdateEndDate;
    private long updateCount;
    private long minUpdateDuration;
    private long maxUpdateDuration;
    private long[] latencySparkData;
    private String lastError;
    private String status;
}
