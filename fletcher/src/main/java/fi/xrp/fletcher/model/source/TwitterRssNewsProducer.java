package fi.xrp.fletcher.model.source;

import com.google.common.collect.Sets;
import com.google.common.net.UrlEscapers;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import fi.xrp.fletcher.model.api.News;
import lombok.Builder;
import lombok.Singular;

import java.util.Set;

public class TwitterRssNewsProducer extends AbstractRssNewsProducer {
    private final String alias;
    private final String name;
    private final String position;
    private final String company;

    @Builder
    public TwitterRssNewsProducer(final String alias, final String name, final String position, final String company, @Singular final Set<Tag> tags) {
        super(tags);
        this.alias = alias;
        this.name = name;
        this.position = position;
        this.company = company;
    }

    @Override
    public String getFeedUrl() {
        return "http://queryfeed.net/twitter?q=from%3A%40" + alias + "&title-type=tweet-text-full&order-by=recent&geocode=&omit-direct=on";
    }

    @Override
    public String getHomeUrl() {
        return String.format("http://twitter.com/%s", alias);
    }

    @Override
    public String getTitle() {
        return String.format("Twitter: @%s", alias);
    }

    @Override
    protected News getNews(final String guid, final SyndFeed rssFeed, final SyndEntry rssFeedEntry) {
        final News news = super.getNews(guid, rssFeed, rssFeedEntry);

        news.setSourceId("twitter");
        news.setPersonName(name);
        news.setPersonPosition(position);
        news.setPersonCompany(company);

        final String twitterUrl = news.getUrl();
        final String oembedUrl = String.format("https://publish.twitter.com/oembed?format=json&dnt=true&theme=dark&url=%s", UrlEscapers.urlPathSegmentEscaper().escape(twitterUrl));
        news.setOembedUrl(oembedUrl);

        news.setAvatarImageUrls(Sets.newHashSet(
                String.format("http://avatars.io/twitter/%s/small", alias),
                String.format("http://avatars.io/twitter/%s/medium", alias),
                String.format("http://avatars.io/twitter/%s/large", alias)));

        return news;
    }
}
