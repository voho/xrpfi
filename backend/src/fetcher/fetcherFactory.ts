import {News} from "../model/model";
import {DIVIDER_SLOWEST} from "../utils/constants";
import {genericRssMapper, redditRssMapper, twitterRssMapper, youtubeRssMapper} from "./mappers";

type Status = "INITIALIZED" | "OK" | "WORKING" | "ERROR";

export interface FetcherStatus {
    status: Status,
    lastUpdateStartTime: number,
    lastUpdateEndTime: number,
    lastErrorTime: number,
    lastNewsCount: number,
    lastErrorMessage: string | null
}

export enum Tag {
    "twitter",
    "social",
    "news",
    "good",
    "official",
    "community",
    "bot",
    "reddit",
    "filter",
    "youtube"
}

export interface Fetcher {
    tags: Set<Tag>,
    title: string,
    homeUrl: string,
    fetchUrl: string,
    updateFrequencyDivider: number,
    status: FetcherStatus,
    mapper: ResponseToNewsMapper,
    limit: number
}

export function getTwitterFetcher(alias: string, tags: Set<Tag>): Fetcher {
    return {
        tags: tags,
        title: `@${alias} at twitter`,
        homeUrl: `https://twitter.com/${alias}`,
        fetchUrl: `https://twitrss.me/twitter_user_to_rss/?user=${alias}`,
        updateFrequencyDivider: DIVIDER_SLOWEST,
        status: getInitialStatus(),
        mapper: twitterRssMapper,
        limit: 10
    };
}

export function getRedditFetcher(sub: string, tags: Set<Tag>): Fetcher {
    return {
        tags: tags,
        title: `/r/${sub}`,
        homeUrl: `https://www.reddit.com/r/${sub}/`,
        fetchUrl: `https://www.reddit.com/r/${sub}/.rss`,
        updateFrequencyDivider: DIVIDER_SLOWEST,
        status: getInitialStatus(),
        mapper: redditRssMapper,
        limit: 20
    };
}

export function getNewsFetcher(feedUrl: string, tags: Set<Tag>): Fetcher {
    return {
        tags: tags,
        title: `news://${feedUrl}`,
        homeUrl: feedUrl,
        fetchUrl: feedUrl,
        updateFrequencyDivider: DIVIDER_SLOWEST,
        status: getInitialStatus(),
        mapper: genericRssMapper,
        limit: 20
    };
}

export function getYouTubeFetcher(channelId: string, tags: Set<Tag>): Fetcher {
    return {
        tags: tags,
        title: `youtube://${channelId}`,
        homeUrl: `http://www.youtube.com/channel/${channelId}`,
        fetchUrl: `http://www.youtube.com/feeds/videos.xml?channel_id=${channelId}`,
        updateFrequencyDivider: DIVIDER_SLOWEST,
        status: getInitialStatus(),
        mapper: youtubeRssMapper,
        limit: 10
    };
}

export type ResponseToNewsMapper = (response: string) => Promise<News[]>;

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
