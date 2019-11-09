import React from "react";
import {Action, Tickers, TickersState} from "../../../backend/src/model/model";

export interface TickersLoadSuccessAction extends Action {
    tickers: Tickers
}

export interface TickersLoadErrorAction extends Action {
    errorMessage: string;
}

interface UseTickersReducerContextState {
    state: TickersState,
    dispatch: React.Dispatch<Action>
}

export const tickersReducer = (state: TickersState, action: Action): TickersState => {
    switch (action.type) {
        case "tickers_load_start":
            return {...state, loading: true, error: undefined};
        case "tickers_load_success":
            return {...state, loading: false, tickers: (action as TickersLoadSuccessAction).tickers};
        case "tickers_load_error":
            return {...state, loading: false, error: (action as TickersLoadErrorAction).errorMessage};
        default:
            console.error("Unexpected action: " + JSON.stringify(action));
            return state;
    }
};

export const UseTickersReducerContext = React.createContext({} as UseTickersReducerContextState);
