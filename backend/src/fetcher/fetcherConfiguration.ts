import {Fetcher, getTwitterFetcher} from "./fetcherFactory";

export const ALL_FETCHERS: Fetcher[] = [
    getTwitterFetcher("Ripple")
];
