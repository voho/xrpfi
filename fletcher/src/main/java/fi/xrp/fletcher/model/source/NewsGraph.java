package fi.xrp.fletcher.model.source;

import java.util.Date;

public interface NewsGraph {
    void attachUrl(String guid, String url, String feedUrl, String homeUrl, String imageUrl);

    void attachExternalUrl(String guid, String externalUrl);

    void attachImageUrl(String guid, String imageSrc);

    void attachAvatarUrl(String guid, String smallHref, String mediumHref, String largeHref);

    void attachTitle(String guid, String title);

    void attachBody(String guid, String body);

    void attachDates(String guid, Date publishedDate, Date updatedDate);

    void attachTag(String guid, NewsProducer.Tag tag);

    void attachKeyword(String guid, String keyword);

    void markAsBullish(String guid);

    void markAsBearish(String guid);

    void markImportant(String guid);

    void markCommunity(String guid);

    void attachGenericSource(String guid, String sourceTitle);

    void attachGithubSource(String guid, String author, String project, String branch);

    void attachRedditSource(String guid, String sub);

    void attachTwitterSource(String guid, String alias, String name);

    void attachPerson(String guid, String name, String position, String company);

    void attachYoutubeSource(String guid, String channelId, String channelName, String videoId, Long viewCount);

    void attachTradingViewSource(String guid);

    void attachRating(String guid, Double rating, Double maxRating, Long ratingCount);

    void attachLikes(String guid, Long upvotes, Long downvotes);
}
