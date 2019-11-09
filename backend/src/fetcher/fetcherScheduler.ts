import {httpGet} from "../http/httpClient";
import {addNews} from "../store/NewsStore";
import {logDebug, logInfo} from "../utils/logger";
import {Fetcher} from "./fetcherFactory";

export function scheduleFetcher(fetcher: Fetcher) {
    logInfo(`Scheduling fetcher: ${fetcher.title}`);

    const intervalMs = fetcher.updateIntervalSeconds * 1000;

    function onResponse(response) {
        const news = fetcher.mapper(response.body);
        logDebug("Received news: " + news.length);
        addNews(news);
    }

    function onRefresh() {
        logInfo("Refreshing: " + fetcher.title);

        httpGet(fetcher.fetchUrl)
            .then(onResponse)
            .catch(error => console.warn(error));
    }

    setInterval(onRefresh, intervalMs);
    onRefresh();
}
