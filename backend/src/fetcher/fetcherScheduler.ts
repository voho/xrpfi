import {ALL_FETCHERS} from "./fetcherConfiguration";
import {refreshFetcher} from "./fetcherLoader";

const MAX_QUARTZ = 4;
const QUARTZ_INTERVAL_MS = 3000;

export function scheduleFetcherRefresh() {
    let quartz = 0;

    function shouldRefreshFetcher(fetcher) {
        return quartz % fetcher.updateFrequencyDivider == 0;
    }

    function tick() {
        quartz = (quartz + 1) % MAX_QUARTZ;
    }

    function handler() {
        ALL_FETCHERS.forEach(fetcher => {
            if (shouldRefreshFetcher(fetcher)) {
                refreshFetcher(fetcher);
            }
        });

        tick();
    }

    setInterval(handler, QUARTZ_INTERVAL_MS);
}
