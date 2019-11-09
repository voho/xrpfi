package fi.xrp.fletcher.model.source;

import com.google.common.collect.Sets;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import fi.xrp.fletcher.model.api.News;
import lombok.Builder;
import lombok.Singular;
import org.jdom.Element;

import java.util.List;
import java.util.Set;

public class YouTubeRssNewsProducer extends AbstractRssNewsProducer {
    private final String channelName;
    private final String channelId;

    @Builder
    public YouTubeRssNewsProducer(final String channelName, final String channelId, @Singular final Set<Tag> tags) {
        super(tags);
        this.channelId = channelId;
        this.channelName = channelName;
    }

    @Override
    public String getFeedUrl() {
        return String.format("http://www.youtube.com/feeds/videos.xml?channel_id=%s", channelId);
    }

    @Override
    public String getHomeUrl() {
        return String.format("http://www.youtube.com/channel/%s", channelId);
    }

    @Override
    public String getTitle() {
        return String.format("YouTube: %s (channel)", channelName);
    }

    @Override
    protected News getNews(final String guid, final SyndFeed rssFeed, final SyndEntry rssFeedEntry) {
        final News news = super.getNews(guid, rssFeed, rssFeedEntry);

        news.setSourceId("youtube");
        final String videoId = rssFeedEntry.getUri().replace("yt:video:", "");

        Double ratingAverage = null;
        Long ratingCount = null;
        Long viewCount = null;

        final Object foreignMarkupUntyped = rssFeedEntry.getForeignMarkup();

        if (foreignMarkupUntyped instanceof List) {
            final List<Element> foreignMarkup = (List<Element>) foreignMarkupUntyped;

            for (final Element foreignElement : foreignMarkup) {
                switch (foreignElement.getName()) {
                    case "videoId":
                        break;
                    case "channelId":
                        break;
                    case "group":
                        for (final Element groupElement : (List<Element>) foreignElement.getChildren()) {
                            switch (groupElement.getName()) {
                                case "title":
                                    break;
                                case "content":
                                    break;
                                case "thumbnail":
                                    break;
                                case "description":
                                    news.setBody(groupElement.getTextTrim());
                                    break;
                                case "community":
                                    for (final Element communityElement : (List<Element>) groupElement.getChildren()) {
                                        switch (communityElement.getName()) {
                                            case "starRating":
                                                final String averageStr = communityElement.getAttributeValue("average");
                                                ratingAverage = Double.valueOf(averageStr);
                                                final String countStr = communityElement.getAttributeValue("count");
                                                ratingCount = Long.valueOf(countStr);
                                                break;
                                            case "statistics":
                                                final String viewsStr = communityElement.getAttributeValue("views");
                                                viewCount = Long.valueOf(viewsStr);
                                                break;
                                        }
                                    }
                                    break;
                            }
                        }
                        break;
                }
            }
        }

        news.setYoutubeChannelId(channelId);
        news.setYoutubeChannelName(channelName);
        news.setVideoId(videoId);
        news.setViewCount(viewCount);

        if (ratingAverage != null) {
            news.setRatingCount(ratingCount);
            news.setRatingAverage(ratingAverage);
            news.setMaxRating(5.0);
        }

        news.setAvatarImageUrls(Sets.newHashSet(
                String.format("http://img.youtube.com/vi/%s/default.jpg", videoId),
                String.format("http://img.youtube.com/vi/%s/mqdefault.jpg", videoId),
                String.format("http://img.youtube.com/vi/%s/hqdefault.jpg", videoId)));

        return news;
    }
}
