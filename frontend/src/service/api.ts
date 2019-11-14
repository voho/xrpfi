import React from "react";
import {Action} from "../../../backend/src/model/model";
import {NewsLoadErrorAction, NewsLoadStartAction, NewsLoadSuccessAction} from "./NewsReducer";
import {StatusLoadErrorAction, StatusLoadStartAction, StatusLoadSuccessAction} from "./StatusReducer";
import {TickersLoadSuccessAction} from "./TickersReducer";

const priceUrl = "https://min-api.cryptocompare.com/data/pricemultifull?fsyms=XRP&tsyms=BTC,USD";
const newsApiUrl = "/api/news";
const statusApiUrl = "/api/status";

const TICKERS_UPDATE_INTERVAL_MS = 10000;
const NEWS_UPDATE_INTERVAL_MS = 10000;
const STATUS_UPDATE_INTERVAL_MS = 30000;

export function scheduleRegularTickersUpdate(dispatch: React.Dispatch<Action>) {
    const callback = () => {
        fetch(priceUrl)
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
    };

    callback();
    setInterval(callback, TICKERS_UPDATE_INTERVAL_MS);
}

export function scheduleRegularNewsUpdate(dispatch: React.Dispatch<Action>) {
    const callback = () => {
        dispatch({type: "news_load_start"} as NewsLoadStartAction);

        fetch(newsApiUrl)
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
    };

    callback();
    setInterval(callback, NEWS_UPDATE_INTERVAL_MS);
}

export function scheduleRegularStatusUpdate(dispatch: React.Dispatch<Action>) {
    const callback = () => {
        dispatch({type: "status_load_start"} as StatusLoadStartAction);

        fetch(statusApiUrl)
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
    };

    callback();
    setInterval(callback, STATUS_UPDATE_INTERVAL_MS);
}
