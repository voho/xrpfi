package fi.xrp.fletcher.model.api;

import lombok.Data;

import java.util.LinkedHashSet;
import java.util.Set;

@Data
public class News {
    private int hash = 0;
    private Long date = 0L;
    private String guid;
    private String title;
    private String url;
    private String body;
    private String language;
    private String sourceId;
    private String sourceName;
    private String sourceFeedUrl;
    private String sourceHomeUrl;
    private String sourceImageUrl;
    private Set<String> keywords = new LinkedHashSet<>();
    private Set<String> tags = new LinkedHashSet<>();
    private Set<String> sourceUrls = new LinkedHashSet<>();
    private Set<String> externalUrls = new LinkedHashSet<>();
    private Set<String> imageUrls = new LinkedHashSet<>();
    private String videoId;
    private Long viewCount;
    private Long ratingCount;
    private Double ratingAverage;
    private Double maxRating;
    private Long upvotes;
    private Long downvotes;
    private boolean important;
    private boolean external;
    private boolean popular;
    private boolean community;
}
