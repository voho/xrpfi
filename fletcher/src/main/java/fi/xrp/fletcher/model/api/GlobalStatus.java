package fi.xrp.fletcher.model.api;

import lombok.Data;

@Data
public class GlobalStatus {
    private long startTime;
    private long endTime;
    private long totalNews;
    private long totalNewsProducers;
}
