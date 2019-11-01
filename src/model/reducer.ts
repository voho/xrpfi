import React from "react";
import {GlobalState, News, Tickers} from "./model";

export interface Action {
    type: string
}

export interface SelectAction extends Action {
    selected: string;
}

export interface NewsLoadStartAction extends Action {

}

export interface NewsLoadSuccessAction extends Action {
    news: News[]
}

export interface NewsLoadErrorAction extends Action {
    errorMessage: string;
}

export interface TickersLoadSuccessAction extends Action {
    tickers: Tickers
}

export interface TickersLoadErrorAction extends Action {
    errorMessage: string;
}

interface UseReducerContextState {
    state: Partial<GlobalState>,
    dispatch: React.Dispatch<Action>
}

export const globalReducer = (state: GlobalState, action: Action): GlobalState => {
    switch (action.type) {
        case "news_select":
            return {...state, selectedNewsGuid: (action as SelectAction).selected};
        case "news_load_start":
            return {...state, newsLoading: true, newsLoadingError: undefined};
        case "news_load_success":
            return {...state, newsLoading: false, news: (action as NewsLoadSuccessAction).news};
        case "news_load_error":
            return {...state, newsLoading: false, newsLoadingError: (action as NewsLoadErrorAction).errorMessage};
        case "tickers_load_start":
            return {...state, tickersLoading: true, tickersLoadingError: undefined};
        case "tickers_load_success":
            return {...state, tickersLoading: false, tickers: (action as TickersLoadSuccessAction).tickers};
        case "tickers_load_error":
            return {...state, tickersLoading: false, tickersLoadingError: (action as TickersLoadErrorAction).errorMessage};
        default:
            console.error("Unexpected action: " + JSON.stringify(action));
            return state;
    }
};

export const GlobalReducerContext = React.createContext({} as UseReducerContextState);
