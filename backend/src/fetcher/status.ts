import {KNOWN_TAGS, Meta, TagMeta} from "../../../common/src/model";
import {Fetcher} from "../model/fetcher";
import {ALL_FETCHERS} from "./configuration";

export function getStatus(): Meta[] {
    return ALL_FETCHERS
        .map((fetcher: Fetcher) => {
            return {
                feedUrl: fetcher.fetchUrl,
                homeUrl: fetcher.homeUrl,
                lastError: fetcher.status.lastErrorMessage,
                lastUpdateStartDate: fetcher.status.lastUpdateStartTime,
                lastUpdateEndDate: fetcher.status.lastUpdateEndTime,
                lastUpdateNewsCount: fetcher.status.lastNewsCount,
                status: fetcher.status.status,
                title: fetcher.title
            };
        });
}

export function getTags(): TagMeta[] {
    return KNOWN_TAGS;
}
