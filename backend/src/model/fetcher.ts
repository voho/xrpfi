import {News, TagId} from "../../../frontend/src/common/model";

type Status = "INITIALIZED" | "OK" | "WORKING" | "ERROR";

export interface FetcherStatus {
    status: Status,
    lastUpdateStartTime: number,
    lastUpdateEndTime: number,
    lastErrorTime: number,
    lastNewsCount: number,
    lastErrorMessage: string | null
}

export interface Fetcher {
    tags: Set<TagId>,
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
