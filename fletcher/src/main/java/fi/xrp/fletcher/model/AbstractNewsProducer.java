package fi.xrp.fletcher.model;

import com.google.common.base.Strings;
import com.google.common.collect.Sets;
import fi.xrp.fletcher.utility.TextUtility;
import lombok.RequiredArgsConstructor;

import java.net.URI;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
public abstract class AbstractNewsProducer implements NewsProducer {
    private final Set<String> KEYWORDS = Sets.newHashSet(
            "xrp", "ripple", "swell", "xrapid", "xvia", "xcurrent", "swift", "ilp", "dlt"
    );

    private final Set<String> IMPORTANT_MARKERS = Sets.newHashSet(
            "launch", "launches", "launched", "launching", "breaking", "authorized", "authorised", "authorizes", "authorises", "authorize", "authorise"
    );

    private final Set<Tag> tags;

    protected Set<Tag> getTags() {
        return this.tags;
    }

    protected boolean isImportant(final String title) {
        if (this.tags.contains(Tag.ALWAYS_IMPORTANT)) {
            return true;
        }
        return !Sets.intersection(TextUtility.getKeywords(title), this.IMPORTANT_MARKERS).isEmpty();
    }

    protected boolean isAcceptable(final String title) {
        if (!this.tags.contains(Tag.NEEDS_FILTERING)) {
            return true;
        }
        return !Sets.intersection(TextUtility.getKeywords(title), this.KEYWORDS).isEmpty();
    }

    @Override
    public String getTitle() {
        return this.getTitle(this.getHomeUrl());
    }

    @Override
    public String getHomeUrl() {
        return this.getFeedUrl();
    }

    @Override
    public List<News> fetchNews(final CustomHttpClient customHttpClient) throws Exception {
        customHttpClient.execute(this.getFeedUrl(), this.getHandler(newsListener));
    }

    protected abstract HttpResponseHandler getHandler(final NewsListener newsListener);

    private String getTitle(final String feedUrl) {
        if (Strings.isNullOrEmpty(feedUrl)) {
            return "N/A";
        }
        final URI uri = URI.create(feedUrl);
        String name;
        if ("feeds.feedburner.com".equalsIgnoreCase(uri.getHost())) {
            name = uri.getPath();
        } else {
            name = uri.getHost();
        }
        if (Strings.isNullOrEmpty(name)) {
            return feedUrl;
        }
        name = name.replace("www.", "");
        final int lastDotIndex = name.lastIndexOf('.');
        if (lastDotIndex != -1) {
            name = name.substring(0, lastDotIndex);
        }
        while (name.endsWith("/")) {
            name = name.substring(0, name.length() - 1);
        }
        while (name.startsWith("/")) {
            name = name.substring(1);
        }
        return name;
    }
}
