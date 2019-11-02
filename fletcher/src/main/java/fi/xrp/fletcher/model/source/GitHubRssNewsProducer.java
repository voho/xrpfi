package fi.xrp.fletcher.model.source;

import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import fi.xrp.fletcher.service.CustomHttpClient;
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
        return String.format("http://github.com/%s/%s/commits/%s.atom", author, project, branch);
    }

    @Override
    public String getHomeUrl() {
        return String.format("http://github.com/%s/%s/tree/%s", author, project, branch);
    }

    @Override
    protected void updateDatabase(final CustomHttpClient customHttpClient, final NewsGraph database, final String guid, final SyndFeed rssFeed, final SyndEntry rssFeedEntry) {
        super.updateDatabase(customHttpClient, database, guid, rssFeed, rssFeedEntry);

        database.attachGithubSource(guid, author, project, branch);
    }
}
