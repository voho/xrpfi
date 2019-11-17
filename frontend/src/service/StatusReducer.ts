import React from "react";
import {Action, Meta, StatusState} from "../../../backend/src/model/model";

export interface StatusLoadStartAction extends Action {

}

export interface StatusLoadSuccessAction extends Action {
    status: Meta[],
    tags: string[]
}

export interface StatusLoadErrorAction extends Action {
    errorMessage: string;
}

export interface ToggleTagEnabledAction extends Action {
    tag: string
}

function toggleTagVisible(selectedTags: string[], toggledTag: string): string[] {
    if (selectedTags.includes(toggledTag)) {
        return selectedTags.filter(t => t !== toggledTag);
    } else {
        return [...selectedTags, toggledTag];
    }
}

interface UseStatusReducerContextState {
    state: StatusState,
    dispatch: React.Dispatch<Action>
}

export const statusReducer = (state: StatusState, action: Action): StatusState => {
    switch (action.type) {
        case "status_load_start":
            return {...state, loading: true, error: undefined};
        case "status_load_success":
            return {...state, loading: false, status: (action as StatusLoadSuccessAction).status, tags: (action as StatusLoadSuccessAction).tags};
        case "status_load_error":
            return {...state, loading: false, error: (action as StatusLoadErrorAction).errorMessage};
        case "toggle_tag_visible":
            return {...state, selectedTags: toggleTagVisible(state.selectedTags, (action as ToggleTagEnabledAction).tag)};
        default:
            console.error("Unexpected action: " + JSON.stringify(action));
            return state;
    }
};

export const UseStatusReducerContext = React.createContext({} as UseStatusReducerContextState);
