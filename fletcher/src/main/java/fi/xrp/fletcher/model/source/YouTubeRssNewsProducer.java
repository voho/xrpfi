package fi.xrp.fletcher.model.source;

import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import fi.xrp.fletcher.service.CustomHttpClient;
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
    protected void updateDatabase(final CustomHttpClient customHttpClient, final NewsGraph database, final String guid, SyndFeed rssFeed, final SyndEntry rssFeedEntry) {
        super.updateDatabase(customHttpClient, database, guid, rssFeed, rssFeedEntry);

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
                                    database.attachBody(guid, groupElement.getTextTrim());
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

        database.attachYoutubeSource(guid, channelId, channelName, videoId, viewCount);
        if (ratingAverage != null && ratingCount != null) {
            database.attachRating(guid, ratingAverage, 5.0, ratingCount);
        }
        database.attachAvatarUrl(
                guid,
                String.format("http://img.youtube.com/vi/%s/default.jpg", videoId),
                String.format("http://img.youtube.com/vi/%s/mqdefault.jpg", videoId),
                String.format("http://img.youtube.com/vi/%s/hqdefault.jpg", videoId)
        );
    }
}
