import {News} from "./model";

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
    customFields: string[],
    title: string,
    homeUrl: string,
    fetchUrl: string,
    updateFrequencyDivider: number,
    status: FetcherStatus,
    mapper: ResponseToNewsMapper,
    filter: NewsFilter,
    limit: number,
    quality: number
}

export type ResponseToNewsMapper = (fetcher: Fetcher, response: string) => Promise<News[]>;

export type NewsFilter = (news: News) => boolean;
