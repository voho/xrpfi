package fi.xrp.fletcher.model.source;

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

    /*
    @Override
    protected void updateDatabase(final NewsDatabase database, final String guid, final SyndFeed rssFeed, final SyndEntry rssFeedEntry) {
        super.updateDatabase(database, guid, rssFeed, rssFeedEntry);

        final String oembedUrl = String.format("https://publish.twitter.com/oembed?format=json&dnt=true&theme=dark&url=%s", UrlEscapers.urlPathSegmentEscaper().escape(rssFeedEntry.getUri()));

        database.attachExternalUrl(guid, oembedUrl);

        database.attachAvatarUrl(
                guid,
                String.format("http://avatars.io/twitter/%s/small", alias),
                String.format("http://avatars.io/twitter/%s/medium", alias),
                String.format("http://avatars.io/twitter/%s/large", alias)
        );

        database.attachTwitterSource(guid, alias, null);

        if (!Strings.isNullOrEmpty(name) && !Strings.isNullOrEmpty(position) && !Strings.isNullOrEmpty(company)) {
            database.attachPerson(guid, name, position, company);
        }
    }*/

    @Override
    protected News getNews(final String guid, final SyndFeed rssFeed, final SyndEntry rssFeedEntry) {
        final News news = super.getNews(guid, rssFeed, rssFeedEntry);
        // TODO more info
        news.setSourceId("twitter");
        return news;
    }
}
