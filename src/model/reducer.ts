import React from "react";
import {GlobalState, Root} from "./model";

export interface Action {
    type: string
}

export interface SelectAction extends Action {
    selected: string;
}

export interface LoadSuccessAction extends Action {
    data: Root;
}

export interface LoadErrorAction extends Action {
    errorMessage: string;
}

interface UseReducerContextState {
    state: Partial<GlobalState>,
    dispatch: React.Dispatch<Action>
}

export const globalReducer = (state: GlobalState, action: Action): GlobalState => {
    switch (action.type) {
        case "select":
            return {...state, selected: (action as SelectAction).selected};
        case "load_start":
            return {...state, loading: true, error: undefined};
        case "load_success":
            return {...state, loading: false, root: (action as LoadSuccessAction).data};
        case "load_error":
            return {...state, loading: false, error: (action as LoadErrorAction).errorMessage};
        default:
            throw new Error("Unexpected action: " + JSON.stringify(action));
    }
};

export const GlobalReducerContext = React.createContext({} as UseReducerContextState);
