import {STATUS_UPDATE_INTERVAL_MS, TICKERS_UPDATE_INTERVAL_MS} from "../../../common/src/constants";
import {TagId} from "../../../common/src/model";
import {NewsLoadErrorAction, NewsLoadStartAction, NewsLoadSuccessAction} from "./NewsReducer";
import {StatusLoadErrorAction, StatusLoadStartAction, StatusLoadSuccessAction} from "./StatusReducer";
import {TickersLoadSuccessAction} from "./TickersReducer";

const priceUrl = "https://min-api.cryptocompare.com/data/pricemultifull?fsyms=XRP&tsyms=BTC,USD";
const newsApiUrl = "/api/news";
const statusApiUrl = "/api/status";

function getPriceUrl(): string {
    return priceUrl;
}

function getNewsApiUrl(selectedTagIds: TagId[]): string {
    const params = new URLSearchParams();
    params.append("whitelist", selectedTagIds.join(","));
    return newsApiUrl + "?" + params.toString();
}

function getStatusApiUrl(): string {
    return statusApiUrl;
}

export function updateTickers(context) {
    fetch(getPriceUrl())
        .then(response => {
            if (!response.ok) {
                throw new Error("Invalid response: " + response.status);
            }
            return response.json();
        })
        .then(responseJson => {
            context.dispatch({
                type: "tickers_load_success",
                tickers: {
                    xrp_btc_price: responseJson.RAW.XRP.BTC.PRICE * 100000000,
                    xrp_usd_price: responseJson.RAW.XRP.USD.PRICE,
                    xrp_btc_change1d: responseJson.RAW.XRP.BTC.CHANGEPCT24HOUR / 100.0,
                    xrp_usb_change1d: responseJson.RAW.XRP.USD.CHANGEPCT24HOUR / 100.0
                }
            } as TickersLoadSuccessAction);
        })
        .catch(error => {
            context.dispatch({type: "tickers_load_error", errorMessage: error.toString()} as NewsLoadErrorAction);
        });
}

export function updateNews(dispatch, state) {
    dispatch({type: "news_load_start"} as NewsLoadStartAction);

    console.log("Loading news: " + JSON.stringify(state.selectedTagIds));

    fetch(getNewsApiUrl(state.selectedTagIds))
        .then(response => {
            if (!response.ok) {
                throw new Error("Invalid response: " + response.status);
            }
            return response.json();
        })
        .then(newState => {
            if (!newState || !newState.root) {
                throw new Error("Invalid new state: " + newState);
            }
            dispatch({type: "news_load_success", news: newState.root} as NewsLoadSuccessAction);
        })
        .catch(error => {
            dispatch({type: "news_load_error", errorMessage: error.toString()} as NewsLoadErrorAction);
        });
}

export function updateStatus(context) {
    context.dispatch({type: "status_load_start"} as StatusLoadStartAction);

    fetch(getStatusApiUrl())
        .then(response => {
            if (!response.ok) {
                throw new Error("Invalid response: " + response.status);
            }
            return response.json();
        })
        .then(newState => {
            if (!newState || !newState.root) {
                throw new Error("Invalid new state: " + newState);
            }
            context.dispatch({type: "status_load_success", status: newState.root} as StatusLoadSuccessAction);
        })
        .catch(error => {
            context.dispatch({type: "status_load_error", errorMessage: error.toString()} as StatusLoadErrorAction);
        });
}

export function scheduleRegularTickersUpdate(context) {
    const callback = () => updateTickers(context);
    callback();
    setInterval(callback, TICKERS_UPDATE_INTERVAL_MS);
}

export function scheduleRegularStatusUpdate(context) {
    const callback = () => updateStatus(context);
    callback();
    setInterval(callback, STATUS_UPDATE_INTERVAL_MS);
}
