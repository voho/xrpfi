import {News} from "../../../common/src/model";
import {httpGet} from "../http/httpClient";
import {Fetcher} from "../model/fetcher";
import {addNews} from "../store/newsStorage";
import {logError, logInfo} from "../utils/logger";
import {now} from "../utils/time";

export function refreshFetcher(fetcher: Fetcher) {
    logInfo(`Scheduling fetcher: ${fetcher.title}`);
    fetcher.status.lastUpdateStartTime = now();
    fetcher.status.status = "WORKING";

    function getStringResponse(response: any): string {
        if (!response.text) {
            throw new Error("Response text is undefined.");
        }
        if (typeof response.text !== "string") {
            throw new Error("Invalid response type: " + typeof response.text);
        }
        return response.text.replace("\ufeff", "").trim();
    }

    function mappedResponseHandler(news: News[]): void {
        fetcher.status.lastNewsCount = news.length;
        fetcher.status.lastUpdateEndTime = now();
        fetcher.status.status = "OK";
        addNews(news);
    }

    function responseHandler(response: any): void {
        if (response.ok) {
            fetcher.mapper(fetcher, getStringResponse(response))
                .then((news: News[]) => news.filter(fetcher.filter))
                .then((news: News[]) => news.splice(0, fetcher.limit))
                .then(mappedResponseHandler)
                .catch(errorHandler);
        }
    }

    function errorHandler(error: any) {
        fetcher.status.lastErrorTime = now();
        fetcher.status.lastErrorMessage = error.message;
        fetcher.status.status = "ERROR";
        logError(fetcher.fetchUrl + ": " + error.message);
    }

    httpGet(fetcher.fetchUrl)
        .then(responseHandler)
        .catch(errorHandler);
}
