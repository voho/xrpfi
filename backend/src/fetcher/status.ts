import {Fetcher, Tag} from "../model/fetcher";
import {Meta} from "../model/model";
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

export function getTags(): string[] {
    const tags = [] as string[];
    for (let tag in Tag) {
        tags.push(Tag[tag]);
    }
    return tags;
}