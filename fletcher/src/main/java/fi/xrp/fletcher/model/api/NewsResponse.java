package fi.xrp.fletcher.model.api;

import lombok.Data;

import java.util.List;

@Data
public class NewsResponse {
    private List<News> news;
    private long totalNewsCount;
}
