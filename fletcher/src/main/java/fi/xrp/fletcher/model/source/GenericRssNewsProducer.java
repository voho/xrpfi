package fi.xrp.fletcher.model.source;

import lombok.Builder;
import lombok.Singular;

import java.util.Set;

public class GenericRssNewsProducer extends AbstractRssNewsProducer {
    private final String name;
    private final String feedUrl;

    @Builder
    public GenericRssNewsProducer(final String name, final String feedUrl, @Singular final Set<Tag> tags) {
        super(tags);
        this.name = name;
        this.feedUrl = feedUrl;
    }

    @Override
    public String getFeedUrl() {
        return feedUrl;
    }

    @Override
    public String getTitle() {
        if (name != null) {
            return name;
        }

        return super.getTitle();
    }
}
