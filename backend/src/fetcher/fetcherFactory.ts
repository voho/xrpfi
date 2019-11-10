import {News} from "../model/model";
import {redditRssMapper, twitterRssMapper} from "./mappers";

const DIVIDER_FASTEST = 1;
const DIVIDER_FAST = DIVIDER_FASTEST * 2;
const DIVIDER_MEDIUM = DIVIDER_FAST * 2;
const DIVIDER_SLOW = DIVIDER_MEDIUM * 2;
const DIVIDER_SLOWEST = DIVIDER_SLOW * 2;

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
    "official",
    "community",
    "bot",
    "reddit",
    "filter"
}

export interface Fetcher {
    tags: Set<Tag>,
    title: string,
    homeUrl: string,
    fetchUrl: string,
    updateFrequencyDivider: number,
    status: FetcherStatus,
    mapper: ResponseToNewsMapper
}

export function getTwitterFetcher(alias: string, tags: Set<Tag>): Fetcher {
    return {
        tags: tags,
        title: `@${alias} at twitter`,
        homeUrl: `https://twitter.com/${alias}`,
        fetchUrl: `https://twitrss.me/twitter_user_to_rss/?user=${alias}`,
        updateFrequencyDivider: DIVIDER_SLOWEST,
        status: getInitialStatus(),
        mapper: twitterRssMapper
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
        mapper: redditRssMapper
    };
}

export type ResponseToNewsMapper = (response: string) => News[];

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
