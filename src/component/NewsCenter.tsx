import React, {useContext, useEffect} from "react";
import {scheduleRegularNewsUpdate} from "../service/api";
import {News} from "../service/model";
import {UseNewsReducerContext} from "../service/NewsReducer";
import {NewsControl} from "./NewsControl";
import {NewsDetail} from "./NewsDetail";
import {NewsList} from "./NewsList";

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
    const context = useContext(UseNewsReducerContext);

    useEffect(() => { scheduleRegularNewsUpdate(context.dispatch); }, []);

    return (
        <>
            <div className={"news-menu"}>
                <NewsControl/>
            </div>
            <div className={"news-list"}>
                {context.state.news && <NewsList news={context.state.news}/>}
            </div>
            <div className={"news-content"}>
                {context.state.news && <NewsDetail selectedNews={findNews(context.state.news, context.state.selectedNewsGuid)}/>}
            </div>
        </>
    );
};
