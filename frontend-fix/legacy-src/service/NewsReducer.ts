import React from "react";
import {Action, NewsState, Root} from "./model";

export interface NewsSelectAction extends Action {
    selected: string;
}

export interface NewsLoadStartAction extends Action {

}

export interface NewsLoadSuccessAction extends Action {
    root: Root
}

export interface NewsLoadErrorAction extends Action {
    errorMessage: string;
}

interface UseNewsReducerContextState {
    state: NewsState,
    dispatch: React.Dispatch<Action>
}

export const newsReducer = (state: NewsState, action: Action): NewsState => {
    switch (action.type) {
        case "news_select":
            if (state.selectedNewsGuid === (action as NewsSelectAction).selected) {
                return {...state, selectedNewsGuid: undefined};
            }
            return {...state, selectedNewsGuid: (action as NewsSelectAction).selected};
        case "news_load_start":
            return {...state, loading: true, error: undefined};
        case "news_load_success":
            return {...state, loading: false, root: (action as NewsLoadSuccessAction).root};
        case "news_load_error":
            return {...state, loading: false, error: (action as NewsLoadErrorAction).errorMessage};
        default:
            console.error("Unexpected action: " + JSON.stringify(action));
            return state;
    }
};

export const UseNewsReducerContext = React.createContext({} as UseNewsReducerContextState);
