import {httpGet} from "../http/httpClient";
import {Meta} from "../model/model";
import {addNews} from "../store/newsStorage";
import {logError, logInfo} from "../utils/logger";
import {ALL_FETCHERS} from "./fetcherConfiguration";
import {Fetcher} from "./fetcherFactory";

function now() {
    return new Date().getTime();
}

export function getMeta(): Meta[] {
    return ALL_FETCHERS
        .map(fetcher => {
            return {
                feedUrl: fetcher.fetchUrl,
                homeUrl: fetcher.homeUrl,
                lastError: fetcher.status.lastErrorMessage,
                lastUpdateStartDate: fetcher.status.lastUpdateStartTime,
                lastUpdateEndDate: fetcher.status.lastUpdateEndTime,
                status: fetcher.status.status,
                title: fetcher.title
            };
        });
}

export function refreshFetcher(fetcher: Fetcher) {
    logInfo(`Scheduling fetcher: ${fetcher.title}`);
    fetcher.status.lastUpdateStartTime = now();
    fetcher.status.status = "WORKING";

    function handler(response) {
        if (response.ok) {
            let responseBodyTreated: string = response.text || response.body;
            responseBodyTreated = responseBodyTreated.replace("\ufeff", "").trim();
            const news = fetcher.mapper(responseBodyTreated);
            fetcher.status.lastNewsCount = news.length;
            fetcher.status.lastUpdateEndTime = now();
            fetcher.status.status = "OK";
            addNews(news);
        }
    }

    httpGet(fetcher.fetchUrl)
        .then(handler)
        .catch(error => {
            fetcher.status.lastErrorTime = now();
            fetcher.status.lastErrorMessage = error.message;
            fetcher.status.status = "ERROR";
            return logError(fetcher.fetchUrl + ": " + error.message);
        });
}
