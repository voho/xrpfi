import React, {useContext, useEffect} from "react";
import {News} from "../../../../backend/src/model/model";
import {scheduleRegularNewsUpdate} from "../../service/api";
import {UseNewsReducerContext} from "../../service/NewsReducer";
import "./NewsCenter.scss";
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
            <div className={"news-site-menu"}>
                <NewsControl/>
            </div>
            <div className={"news-site-list"}>
                {context.state.root && <NewsList news={context.state.root!.news}/>}
            </div>
            <div className={"news-site-detail"}>
                {context.state.root && <NewsDetail selectedNews={findNews(context.state.root!.news, context.state.selectedNewsGuid)}/>}
            </div>
        </>
    );
};
