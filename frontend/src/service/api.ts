import {NEWS_UPDATE_INTERVAL_MS, STATUS_UPDATE_INTERVAL_MS, TICKERS_UPDATE_INTERVAL_MS} from "@xrpfi/common/build/constants";
import {Action, TagId} from "@xrpfi/common/build/model";
import React from "react";
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

function updateTickers(dispatch: React.Dispatch<Action>) {
    fetch(getPriceUrl())
        .then(response => {
            if (!response.ok) {
                throw new Error("Invalid response: " + response.status);
            }
            return response.json();
        })
        .then(responseJson => {
            dispatch({
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
            dispatch({type: "tickers_load_error", errorMessage: error.toString()} as NewsLoadErrorAction);
        });
}

function updateNews(selectedTagIds: TagId[], dispatch: React.Dispatch<Action>) {
    dispatch({type: "news_load_start"} as NewsLoadStartAction);

    fetch(getNewsApiUrl(selectedTagIds))
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

function updateStatus(dispatch: React.Dispatch<Action>) {
    dispatch({type: "status_load_start"} as StatusLoadStartAction);

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
            dispatch({type: "status_load_success", status: newState.root} as StatusLoadSuccessAction);
        })
        .catch(error => {
            dispatch({type: "status_load_error", errorMessage: error.toString()} as StatusLoadErrorAction);
        });
}

export function scheduleRegularTickersUpdate(dispatch: React.Dispatch<Action>) {
    const callback = () => updateTickers(dispatch);
    callback();
    setInterval(callback, TICKERS_UPDATE_INTERVAL_MS);
}

export function scheduleRegularNewsUpdate(context) {
    const callback = () => updateNews(context.state.selectedTagIds, context.dispatch);
    callback();
    setInterval(callback, NEWS_UPDATE_INTERVAL_MS);
}

export function scheduleRegularStatusUpdate(context) {
    const callback = () => updateStatus(context.dispatch);
    callback();
    setInterval(callback, STATUS_UPDATE_INTERVAL_MS);
}
