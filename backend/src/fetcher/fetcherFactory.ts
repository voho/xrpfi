import {News} from "../model/model";
import {twitterRssMapper} from "./mappers";

export interface FetcherMeta {
    lastUpdateStart: number,
    lastUpdateEnd: number,
    lastNewsCount: number,
    lastError: string | null
}

export interface Fetcher {
    title: string,
    homeUrl: string,
    fetchUrl: string,
    updateIntervalSeconds: number,
    meta: FetcherMeta,
    mapper: ResponseToNewsMapper
}

export function getTwitterFetcher(alias: string): Fetcher {
    return {
        title: `@${alias} at twitter`,
        homeUrl: `https://twitter.com/${alias}`,
        fetchUrl: `https://twitrss.me/twitter_user_to_rss/?user=${alias}`,
        updateIntervalSeconds: 30,
        meta: {lastError: null, lastNewsCount: 0, lastUpdateStart: 0, lastUpdateEnd: 0},
        mapper: twitterRssMapper
    };
}

export type ResponseToNewsMapper = (response: string) => News[];
