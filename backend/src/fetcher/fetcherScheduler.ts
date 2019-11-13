import {Fetcher} from "../model/fetcher";
import {MAX_QUARTZ, QUARTZ_INTERVAL_MS} from "../utils/constants";
import {ALL_FETCHERS} from "./fetcherConfiguration";
import {refreshFetcher} from "./fetcherLoader";

export function scheduleFetcherRefresh() {
    let quartz = 0;

    function tick() {
        quartz = (quartz + 1) % MAX_QUARTZ;
    }

    function shouldRefreshFetcher(fetcher: Fetcher) {
        return quartz % fetcher.updateFrequencyDivider == 0;
    }

    function handler() {
        ALL_FETCHERS.forEach((fetcher: Fetcher) => {
            if (shouldRefreshFetcher(fetcher)) {
                refreshFetcher(fetcher);
            }
        });

        tick();
    }

    setInterval(handler, QUARTZ_INTERVAL_MS);
}
