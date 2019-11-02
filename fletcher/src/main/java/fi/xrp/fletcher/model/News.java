package fi.xrp.fletcher.model;

import lombok.Data;

import java.util.Date;
import java.util.Set;

@Data
public class News {
    private Set<NewsProducer.Tag> tags;
    private Date datePublished;
    private Date dateUpdated;
    private String title;
    private String url;
    private String sourceImageUrl;
    private String body;
    private Set<String> imageUrls;
    private Set<String> externalUrls;
    private boolean important;
    private boolean community;
}
