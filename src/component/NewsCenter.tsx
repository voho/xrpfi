import React, {useContext, useEffect} from "react";
import {News} from "../model/model";
import {Action, GlobalReducerContext, LoadErrorAction, LoadSuccessAction} from "../model/reducer";
import {fetchRoot} from "../service/api";
import {NewsControl} from "./NewsControl";
import {NewsDetail} from "./NewsDetail";
import {NewsList} from "./NewsList";

function scheduleUpdate(dispatch: React.Dispatch<Action>) {
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

    setTimeout(callback, 100);
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

export const NewsCenter = () => {
    const context = useContext(GlobalReducerContext);

    useEffect(() => { scheduleUpdate(context.dispatch); }, []);

    return (
        <>
            <div className={"news-menu"}>
                <NewsControl/>
            </div>
            <div className={"news-list"}>
                {context.state.root && <NewsList news={context.state.root!.news}/>}
            </div>
            <div className={"news-content"}>
                {context.state.root && <NewsDetail selectedNews={findNews(context.state.root!.news, context.state.selected)}/>}
            </div>
        </>
    );
};
