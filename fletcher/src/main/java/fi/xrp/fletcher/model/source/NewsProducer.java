package fi.xrp.fletcher.model.source;

import fi.xrp.fletcher.model.api.News;
import fi.xrp.fletcher.service.http.CustomHttpClient;
import lombok.Getter;
import org.asynchttpclient.util.Utf8UrlEncoder;

import java.util.List;
import java.util.Set;
import java.util.concurrent.Future;

public interface NewsProducer {
    String getHomeUrl();

    String getFeedUrl();

    default String getId() {
        return Utf8UrlEncoder.encodePath(getTitle().replace(" ", "-"));
    }

    String getTitle();

    Set<Tag> getTags();

    Future<List<News>> startAsyncUpdate(CustomHttpClient customHttpClient);

    enum Tag {
        SOCIAL("Social media"),
        MICROBLOG("Microblog"),
        MEDIA("Multimedia"),
        SOFTWARE("Software Development"),
        UNOFFICIAL("Unofficial"),
        OFFICIAL("Official"),
        EXCHANGE("Exchange"),
        FINANCIAL("Financial Institution"),
        NEWS("News site"),
        TRADING("Trading"),
        BULLISH("Bullish (long)"),
        BEARISH("Bearish (short)"),
        NEEDS_FILTERING("Filtered source"),
        DO_NOT_DELETE("Do not ever remove"),
        DO_NOT_CLEANUP("Do not cleanup HTML"),
        ALWAYS_IMPORTANT("Always important");

        @Getter
        private final String title;

        Tag(final String title) {
            this.title = title;
        }
    }
}
