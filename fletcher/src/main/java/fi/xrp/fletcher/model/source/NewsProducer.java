package fi.xrp.fletcher.model.source;

import com.google.common.collect.Sets;
import fi.xrp.fletcher.service.CustomHttpClient;
import lombok.Getter;

import java.util.EnumSet;
import java.util.Set;
import java.util.stream.Collectors;

public interface NewsProducer {
    String getHomeUrl();

    String getFeedUrl();

    String getTitle();

    void scheduleAsyncUpdate(CustomHttpClient customHttpClient, NewsListener listener, NewsGraph database);

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

    enum Mode {
        DEFAULT("Default", EnumSet.noneOf(Tag.class), EnumSet.of(Tag.MICROBLOG)),
        NEWS("News", EnumSet.of(Tag.NEWS), EnumSet.noneOf(Tag.class)),
        MEDIA("Media", EnumSet.of(Tag.MEDIA), EnumSet.noneOf(Tag.class)),
        COMMUNITY("Community", EnumSet.of(Tag.UNOFFICIAL), EnumSet.noneOf(Tag.class)),
        OFFICIAL("Official", EnumSet.of(Tag.OFFICIAL), EnumSet.noneOf(Tag.class)),
        TRADING("Trading", EnumSet.of(Tag.TRADING), EnumSet.noneOf(Tag.class)),
        ALL("Everything", EnumSet.noneOf(Tag.class), EnumSet.noneOf(Tag.class));

        @Getter
        private final String title;
        private final Set<String> whitelist;
        private final Set<String> blacklist;

        Mode(final String title, final Set<Tag> whitelist, final Set<Tag> blacklist) {
            this.title = title;
            this.whitelist = whitelist.stream().map(Enum::name).collect(Collectors.toSet());
            this.blacklist = blacklist.stream().map(Enum::name).collect(Collectors.toSet());
        }

        public boolean test(final Set<String> tags) {
            if (!whitelist.isEmpty()) {
                if (Sets.intersection(tags, whitelist).isEmpty()) {
                    return false;
                }
            }
            if (!blacklist.isEmpty()) {
                return Sets.intersection(tags, blacklist).isEmpty();
            }
            return true;
        }
    }
}
