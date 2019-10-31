import moment from "moment";
import React from "react";
import {News} from "../model/model";

interface NewsItemProps {
    news: News
}

const Image: React.FC<NewsItemProps> = (props) => {
    return <img height={40} src={props.news.imageUrls ? props.news.imageUrls[0] : ""}/>;
};

const Title: React.FC<NewsItemProps> = (props) => {
    return <a href={props.news.url}>{props.news.title}</a>;
};

const NewsFlags: React.FC<NewsItemProps> = (props) => {
    return <span>flags</span>;
};

const NewsSource: React.FC<NewsItemProps> = (props) => {
    return <span>source</span>;
};

const NewsDate: React.FC<NewsItemProps> = (props) => {
    return <span>{moment(props.news.date).fromNow()}</span>;
};

export const NewsItem: React.FC<NewsItemProps> = (props) => {
    return (
        <div className={"news-item"}>
            <div className={"news-item-main"}>
                <p className={"news-item-flags"}>
                    <NewsFlags news={props.news}/>
                    <NewsSource news={props.news}/>
                    <NewsDate news={props.news}/>
                </p>
                <p className={"news-item-title"}>
                    <Title news={props.news}/>
                </p>
            </div>
            <div className={"news-item-image"}>
                <Image news={props.news}/>
            </div>
        </div>
    );
};
