package fi.xrp.fletcher.model.source;

import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import fi.xrp.fletcher.model.api.News;
import lombok.Builder;
import lombok.Singular;

import java.util.Set;

public class GitHubRssNewsProducer extends AbstractRssNewsProducer {
    private final String author;
    private final String project;
    private final String branch;

    @Builder
    public GitHubRssNewsProducer(final String author, final String project, final String branch, @Singular final Set<Tag> tags) {
        super(tags);
        this.author = author;
        this.project = project;
        this.branch = branch;
    }

    @Override
    public String getTitle() {
        return String.format("GitHub: %s/%s (%s branch)", author, project, branch);
    }

    @Override
    public String getFeedUrl() {
        return String.format("https://github.com/%s/%s/commits/%s.atom", author, project, branch);
    }

    @Override
    public String getHomeUrl() {
        return String.format("https://github.com/%s/%s/tree/%s", author, project, branch);
    }

    @Override
    protected News getNews(final String guid, final SyndFeed rssFeed, final SyndEntry rssFeedEntry) {
        final News news = super.getNews(guid, rssFeed, rssFeedEntry);
        news.setSourceId("github");
        return news;
    }
}
