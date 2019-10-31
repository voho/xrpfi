import React, {Dispatch, useEffect, useReducer} from "react";
import "./App.css";
import {NewsCenter} from "./component/NewsCenter";
import {GlobalState, Root} from "./model/model";
import {fetchRoot} from "./service/api";

function scheduleUpdate(dispatch: Dispatch<Action<string>>) {
    const callback = () => {
        fetchRoot()
            .then(newState => {
                dispatch({type: "load_success", data: newState} as LoadSuccessAction);
            })
            .catch(error => {
                dispatch({type: "load_error", errorMessage: error.toString()} as LoadErrorAction);
            });

        setTimeout(callback, 5000);
    };

    setTimeout(callback, 1000);
}

export interface Action<S extends string> {
    type: S
}

export interface SelectAction extends Action<"select"> {
    selected: string
}

export interface LoadSuccessAction extends Action<"load_success"> {
    data: Root
}

export interface LoadErrorAction extends Action<"load_error"> {
    errorMessage: string
}

const initialState: GlobalState = {loading: false};

const reducer = (state: GlobalState, action: Action<string>): GlobalState => {
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
            throw new Error("Unexpected action");
    }
};

export const App: React.FC = () => {
    const [state, dispatch] = useReducer(reducer, initialState);

    useEffect(() => {
        scheduleUpdate(dispatch);
    });

    return (
        <NewsCenter state={state} dispatch={dispatch}/>
    );
};
