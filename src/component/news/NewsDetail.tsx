import React from "react";
import {News} from "../../service/model";
import {Button} from "../common/Button";
import {SourcesStatus} from "../meta/SourcesStatus";
import "./NewsDetail.scss";
import {NewsDate, NewsFlags, NewsSource} from "./NewsItem";

interface NewsDetailProps {
    selectedNews?: News
}

function createMarkup(html: string) {
    return {__html: html};
}

const NewsOpen: React.FC<{ news: News }> = (props) => {
    return <Button text={"Open in new window"} onClick={() => window.open(props.news.url)}/>;
};

const NoNewsSelected = () => {
    return <p className={"empty-news"}>No News selected.</p>;
};

const NewsSelected: React.FC<NewsDetailProps> = (props) => {
    if (!props.selectedNews) {
        return <SourcesStatus/>;
    }

    const news = props.selectedNews!;

    return (
        <>
            <h2>{props.selectedNews!.title}</h2>
            <p className={"flags"}>
                <NewsOpen news={news}/>
                <NewsFlags news={news}/>
                <NewsSource news={news}/>
                <NewsDate news={news}/>
            </p>
            <div className={"external"} dangerouslySetInnerHTML={createMarkup(news.body)}/>
        </>
    );
};

export const NewsDetail: React.FC<NewsDetailProps> = (props) => {
    return (
        <div className={"inside"}>
            <NewsSelected selectedNews={props.selectedNews}/>
        </div>
    );
};
