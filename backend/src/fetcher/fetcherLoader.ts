import {httpGet} from "../http/httpClient";
import {Meta} from "../model/model";
import {addNews} from "../store/newsStorage";
import {logError, logInfo} from "../utils/logger";
import {ALL_FETCHERS} from "./fetcherConfiguration";
import {Fetcher} from "./fetcherFactory";

function now() {
    return new Date().getTime();
}

export function getStatus(): Meta[] {
    return ALL_FETCHERS
        .map(fetcher => {
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

export function refreshFetcher(fetcher: Fetcher) {
    logInfo(`Scheduling fetcher: ${fetcher.title}`);
    fetcher.status.lastUpdateStartTime = now();
    fetcher.status.status = "WORKING";

    function getStringResponse(response) {
        if (!response.text) {
            throw new Error("Response text is undefined.");
        }
        if (typeof response.text !== "string") {
            throw new Error("Invalid response type: " + typeof response.text);
        }
        return response.text.replace("\ufeff", "").trim();
    }

    function mappedResponseHandler(news) {
        fetcher.status.lastNewsCount = news.length;
        fetcher.status.lastUpdateEndTime = now();
        fetcher.status.status = "OK";
        addNews(news.splice(0, fetcher.limit));
    }

    function responseHandler(response) {
        if (response.ok) {
            fetcher.mapper(getStringResponse(response))
                .then(mappedResponseHandler)
                .catch(errorHandler);
        }
    }

    function errorHandler(error) {
        fetcher.status.lastErrorTime = now();
        fetcher.status.lastErrorMessage = error.message;
        fetcher.status.status = "ERROR";
        logError(fetcher.fetchUrl + ": " + error.message);
    }

    httpGet(fetcher.fetchUrl)
        .then(responseHandler)
        .catch(errorHandler);
}
