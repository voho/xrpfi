import React, {useContext} from "react";
import {UseNewsReducerContext} from "../../service/NewsReducer";
import "./NewsCenter.scss";
import {NewsControl} from "./NewsControl";
import {NewsDetail} from "./NewsDetail";
import {NewsList} from "./NewsList";

export const NewsCenter = () => {
    const context = useContext(UseNewsReducerContext);

    return (
        <>
            <div className={"news-site-menu"}>
                <NewsControl/>
            </div>
            <div className={"news-site-list"}>
                {context.state.news && <NewsList news={context.state.news}/>}
            </div>
            <div className={"news-site-detail"}>
                {context.state.news && <NewsDetail selectedNews={context.state.selectedNews}/>}
            </div>
        </>
    );
};
