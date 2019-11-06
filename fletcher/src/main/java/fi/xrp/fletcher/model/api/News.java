package fi.xrp.fletcher.model.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Data
public class News {
    private long date;
    private String guid;
    private String title;
    private String url;
    private String body;
    private String sourceId;
    private String sourceName;
    private String sourceFeedUrl;
    private String sourceHomeUrl;
    private String sourceImageUrl;
    private Set<String> keywords = new HashSet<>(3);
    private Set<String> tags = new HashSet<>(3);
    private Set<String> sourceUrls = new HashSet<>(3);
    private Set<String> externalUrls = new HashSet<>(3);
    private Set<String> imageUrls = new HashSet<>(3);
    private Set<String> avatarImageUrls = new HashSet<>(3);
    private String oembedUrl;
    private String videoId;
    private Long viewCount;
    private Long ratingCount;
    private Double ratingAverage;
    private Double maxRating;
    private Long upvotes;
    private Long downvotes;
    private String personName;
    private String personPosition;
    private String personCompany;
    private boolean important;
    private boolean external;
    private boolean popular;
    private boolean community;
}
