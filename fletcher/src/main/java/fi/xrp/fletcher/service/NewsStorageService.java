package fi.xrp.fletcher.service;

import com.google.common.base.Strings;
import fi.xrp.fletcher.model.api.News;
import fi.xrp.fletcher.model.api.NewsSourceMeta;
import fi.xrp.fletcher.model.source.NewsDatabase;
import fi.xrp.fletcher.model.source.NewsProducer;
import fi.xrp.fletcher.utility.BasicUtility;
import fi.xrp.fletcher.utility.UrlUtility;
import lombok.extern.slf4j.Slf4j;

import java.net.URI;
import java.time.Duration;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Slf4j
public class NewsStorageService implements NewsDatabase {
    private static final long MAX_NEWS_AGE_MS = Duration.ofDays(14).toMillis();
    private static final int MAX_VIEW_SIZE = 50;

    private final Map<String, News> guidToNews = new HashMap<>();
    private final Map<NewsProducer, NewsSourceMeta> producerMeta = new HashMap<>();

    private boolean hasAnyTag(final News news, final Set<NewsProducer.Tag> tagWhitelist) {
        return tagWhitelist.stream().map(Enum::name).anyMatch(a -> news.getTags().contains(a));
    }

    public List<NewsProducer.Mode> getModes() {
        return Arrays.asList(NewsProducer.Mode.values());
    }

    public List<NewsSourceMeta> getSourceMeta() {
        return producerMeta.values().stream().sorted(Comparator.comparing(NewsSourceMeta::getTitle)).collect(Collectors.toList());
    }

    public long getTotalNews() {
        return guidToNews.size();
    }

    public long getOldestNewsDate() {
        return guidToNews.values().stream().mapToLong(News::getDate).min().orElse(0);
    }

    public long getNewestNewsDate() {
        return guidToNews.values().stream().mapToLong(News::getDate).max().orElse(0);
    }

    // TODO this is some kind of graph DB

    private synchronized void attach(final String guid, final Consumer<News> newsUpdater) {
        guidToNews.compute(guid, new BiFunction<String, News, News>() {
            @Override
            public News apply(final String guid, final News existingNews) {
                final News news = existingNews == null ? new News() : existingNews;
                news.setGuid(guid);
                newsUpdater.accept(news);
                news.setHash(news.hashCode());
                return news;
            }
        });
    }


    @Override
    public void attachUrl(final String guid, final String url, final String feedUrl, final String homeUrl, final String imageUrl) {
        attach(guid, a -> {
            a.setUrl(url);
            a.setSourceFeedUrl(feedUrl);
            a.setSourceHomeUrl(homeUrl);
            a.setSourceImageUrl(imageUrl);
        });
    }

    @Override
    public void attachExternalUrl(final String guid, final String externalUrl) {
        attach(guid, a -> {
            a.setExternal(true);
            a.getExternalUrls().add(externalUrl);

            if (a.getUrl() != null) {
                // in case external URL is already URL of some known news

                for (final News news : guidToNews.values()) {
                    if (externalUrl.equalsIgnoreCase(news.getUrl())) {
                        news.getSourceUrls().add(a.getUrl());
                    }
                }
            }

            // extract video ID for youtube

            final String videoId = UrlUtility.getYoutubeVideoId(URI.create(externalUrl));

            if (!Strings.isNullOrEmpty(videoId) && Strings.isNullOrEmpty(a.getVideoId())) {
                a.setVideoId(videoId);
            }
        });
    }

    @Override
    public void attachImageUrl(final String guid, final String imageSrc) {
        attach(guid, a -> a.getImageUrls().add(imageSrc));
    }

    @Override
    public void attachAvatarUrl(final String guid, final String smallHref, final String mediumHref, final String largeHref) {
        attach(guid, a -> {
            a.getImageUrls().add(smallHref);
            a.getImageUrls().add(mediumHref);
            a.getImageUrls().add(largeHref);
        });
    }

    @Override
    public void attachTitle(final String guid, final String title) {
        attach(guid, a -> a.setTitle(title));
    }

    @Override
    public void attachBody(final String guid, final String body) {
        attach(guid, a -> a.setBody(body));
    }

    @Override
    public void attachGenericSource(final String guid, final String sourceTitle) {
        attach(guid, a -> {
            a.setSourceId("generic");
            a.setSourceName(sourceTitle);
        });
    }

    @Override
    public void attachDates(final String guid, final Date publishedDate, final Date updatedDate) {
        attach(guid, a -> {
            final Date date = BasicUtility.coalesce(publishedDate, updatedDate);
            if (date != null) {
                a.setDate(date.getTime());
            }
        });
    }

    @Override
    public void attachTag(final String guid, final NewsProducer.Tag tag) {
        attach(guid, a -> a.getTags().add(tag.name()));
    }

    @Override
    public void attachKeyword(final String guid, final String keyword) {
        attach(guid, a -> a.getKeywords().add(keyword));
    }

    @Override
    public void markAsBullish(final String guid) {
        attachTag(guid, NewsProducer.Tag.BULLISH);
    }

    @Override
    public void markAsBearish(final String guid) {
        attachTag(guid, NewsProducer.Tag.BEARISH);
    }

    @Override
    public void markImportant(final String guid) {
        attach(guid, a -> a.setImportant(true));
    }

    @Override
    public void markCommunity(final String guid) {
        attach(guid, a -> a.setCommunity(true));
    }

    @Override
    public void attachGithubSource(final String guid, final String author, final String project, final String branch) {
        attach(guid, a -> {
            a.setSourceName(String.format("%s/%s (%s branch)", author, project, branch));
            a.setSourceId("github");
        });
    }

    @Override
    public void attachRedditSource(final String guid, final String sub) {
        attach(guid, a -> {
            a.setSourceName(String.format("/r/%s", sub));
            a.setSourceId("reddit");
        });
    }

    @Override
    public void attachTwitterSource(final String guid, final String alias, final String name) {
        attach(guid, a -> {
            a.setSourceId("twitter");
            a.setSourceName(String.format("@%s", alias));
            if (name != null) {
                a.setSourceName(name);
            }
        });
    }

    @Override
    public void attachPerson(final String guid, final String name, final String position, final String company) {
        attach(guid, a -> {
            final StringBuilder builder = new StringBuilder(name);
            if (!Strings.isNullOrEmpty(position)) {
                builder.append(", ").append(position);
            }
            if (!Strings.isNullOrEmpty(company)) {
                builder.append(" from ").append(company);
            }
            a.setSourceName(builder.toString());
        });
    }

    @Override
    public void attachYoutubeSource(final String guid, final String channelId, final String channelName, final String videoId, final Long viewCount) {
        attach(guid, a -> {
            a.setSourceId("youtube");
            a.setSourceName(channelName);
            a.setVideoId(videoId);

            if (viewCount != null) {
                a.setViewCount(viewCount);
            }

            if (!Strings.isNullOrEmpty(a.getVideoId())) {
                for (final News n : guidToNews.values()) {
                    if (Objects.equals(a.getVideoId(), n.getVideoId())) {
                        if (viewCount != null) {
                            n.setViewCount(viewCount);
                        }
                    }
                }
            }
        });
    }

    @Override
    public void attachTradingViewSource(final String guid) {
        attach(guid, a -> {
            a.setSourceId("tradingview");
        });
    }

    @Override
    public void attachRating(final String guid, final Double rating, final Double maxRating, final Long ratingCount) {
        attach(guid, a -> {
            if (rating != null) {
                a.setRatingAverage(rating);
            }
            if (maxRating != null) {
                a.setMaxRating(maxRating);
            }
            if (ratingCount != null) {
                a.setRatingCount(ratingCount);
            }

            if (!Strings.isNullOrEmpty(a.getVideoId())) {
                // assign the same rating to all news with the same video URL

                for (final News n : guidToNews.values()) {
                    if (Objects.equals(a.getVideoId(), n.getVideoId())) {
                        if (rating != null) {
                            n.setRatingAverage(rating);
                        }
                        if (maxRating != null) {
                            n.setMaxRating(maxRating);
                        }
                        if (ratingCount != null) {
                            n.setRatingCount(ratingCount);
                        }
                    }
                }
            }
        });
    }

    @Override
    public void attachLikes(final String guid, final Long upvotes, final Long downvotes) {
        attach(guid, a -> {
            a.setUpvotes(upvotes);
            a.setDownvotes(downvotes);
        });
    }

    @Override
    public List<News> getMergedNews() {
        final long now = System.currentTimeMillis();

        return guidToNews
                .values()
                .stream()
                .filter(a -> a.getDate() != null)
                .filter(a -> a.getDate() != 0)
                .filter(a -> now - a.getDate() <= MAX_NEWS_AGE_MS)
                .sorted(Comparator.comparingLong(News::getDate).reversed())
                .limit(MAX_VIEW_SIZE)
                .collect(Collectors.toList());
    }

    @Override
    public void setNews(final List<News> news) {
        log.info("Adding {} news.", news.size());
        guidToNews.clear();
        news.forEach(n -> guidToNews.put(n.getGuid(), n));
    }
}
