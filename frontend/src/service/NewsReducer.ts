import React from "react";
import {Action, News, NewsState, TagId} from "../common/model";

export interface NewsSelectAction extends Action {
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

export interface ToggleTagEnabledAction extends Action {
    tag: TagId
}

interface UseNewsReducerContextState {
    state: NewsState,
    dispatch: React.Dispatch<Action>
}

function toggleTagVisible(selectedTags: TagId[], toggledTag: TagId): TagId[] {
    if (selectedTags.includes(toggledTag)) {
        return selectedTags.filter(t => t !== toggledTag);
    } else {
        return [...selectedTags, toggledTag];
    }
}

function findNews(param: News[], selected?: string): News | undefined {
    if (!selected) {
        return undefined;
    }
    const matches = param.filter(n => n.guid === selected);
    if (matches.length < 1) {
        return undefined;
    }
    return matches[0];
}

export const newsReducer = (state: NewsState, action: Action): NewsState => {
    switch (action.type) {
        case "toggle_tag_visible":
            return {...state, selectedTagIds: toggleTagVisible(state.selectedTagIds, (action as ToggleTagEnabledAction).tag)};
        case "news_select":
            if (state.selectedNews && state.selectedNews.guid === (action as NewsSelectAction).selected) {
                return {...state, selectedNews: undefined};
            }
            return {...state, selectedNews: findNews(state.news, (action as NewsSelectAction).selected)};
        case "news_load_start":
            return {...state, loading: true, error: undefined};
        case "news_load_success":
            return {...state, loading: false, news: (action as NewsLoadSuccessAction).news};
        case "news_load_error":
            return {...state, loading: false, error: (action as NewsLoadErrorAction).errorMessage};
        default:
            console.error("Unexpected action: " + JSON.stringify(action));
            return state;
    }
};

export const UseNewsReducerContext = React.createContext({} as UseNewsReducerContextState);
