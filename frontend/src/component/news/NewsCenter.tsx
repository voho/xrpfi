import React, {useContext} from "react";
import {News} from "../../common/model";
import {UseNewsReducerContext} from "../../service/NewsReducer";
import {PriceChartEmbed} from "../trade/PriceChartEmbed";
import "./NewsCenter.scss";
import {NewsControl} from "./NewsControl";
import {NewsDetail} from "./NewsDetail";
import {NewsList} from "./NewsList";

const NewsListWrapper: React.FC<{ news?: News[] }> = (props) => {
    if (!props.news) {
        return null;
    }
    return (
        <div className={"news-site-list"}>
            <NewsList news={props.news!}/>
        </div>
    );
};

const NewsDetailWrapper: React.FC<{ news?: News }> = (props) => {
    if (!props.news) {
        return <PriceChartEmbed/>;
    }
    return (
        <div className={"news-site-detail"}>
            <NewsDetail selectedNews={props.news!}/>
        </div>
    );
};

export const NewsCenter = () => {
    const context = useContext(UseNewsReducerContext);

    return (
        <>
            <div className={"news-site-menu"}>
                <NewsControl/>
            </div>
            <NewsListWrapper news={context.state.news}/>
            <NewsDetailWrapper news={context.state.selectedNews}/>
        </>
    );
};
