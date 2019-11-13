import {Fetcher, FetcherStatus, Tag} from "../model/fetcher";
import {DIVIDER_SLOWEST} from "../utils/constants";
import {genericRssMapper, getTagBasedFilter, redditRssMapper, twitterRssMapper, youtubeRssMapper} from "./fetcherMappers";

export function getTwitterFetcher(alias: string, tags: Set<Tag>, quality = 1): Fetcher {
    return {
        tags: tags,
        customFields: [],
        title: `@${alias} at twitter`,
        homeUrl: `https://twitter.com/${alias}`,
        fetchUrl: `https://twitrss.me/twitter_user_to_rss/?user=${alias}`,
        updateFrequencyDivider: DIVIDER_SLOWEST,
        status: getInitialStatus(),
        mapper: twitterRssMapper,
        filter: getTagBasedFilter(tags),
        limit: 10,
        quality
    };
}

export function getRedditFetcher(sub: string, tags: Set<Tag>, quality = 2): Fetcher {
    return {
        tags: tags,
        customFields: [],
        title: `/r/${sub}`,
        homeUrl: `https://www.reddit.com/r/${sub}/`,
        fetchUrl: `https://www.reddit.com/r/${sub}/.rss`,
        updateFrequencyDivider: DIVIDER_SLOWEST,
        status: getInitialStatus(),
        mapper: redditRssMapper,
        filter: getTagBasedFilter(tags),
        limit: 20,
        quality
    };
}

export function getNewsFetcher(feedUrl: string, tags: Set<Tag>, quality = 2): Fetcher {
    return {
        tags: tags,
        customFields: [],
        title: `news://${feedUrl}`,
        homeUrl: feedUrl,
        fetchUrl: feedUrl,
        updateFrequencyDivider: DIVIDER_SLOWEST,
        status: getInitialStatus(),
        mapper: genericRssMapper,
        filter: getTagBasedFilter(tags),
        limit: 20,
        quality
    };
}

export function getYouTubeFetcher(channelId: string, tags: Set<Tag>, quality = 2): Fetcher {
    return {
        tags: tags,
        customFields: ["yt:videoId", "media:group"],
        title: `youtube://${channelId}`,
        homeUrl: `http://www.youtube.com/channel/${channelId}`,
        fetchUrl: `http://www.youtube.com/feeds/videos.xml?channel_id=${channelId}`,
        updateFrequencyDivider: DIVIDER_SLOWEST,
        status: getInitialStatus(),
        mapper: youtubeRssMapper,
        filter: getTagBasedFilter(tags),
        limit: 10,
        quality
    };
}

function getInitialStatus(): FetcherStatus {
    return {
        status: "INITIALIZED",
        lastUpdateStartTime: 0,
        lastUpdateEndTime: 0,
        lastErrorTime: 0,
        lastNewsCount: 0,
        lastErrorMessage: null
    };
}
