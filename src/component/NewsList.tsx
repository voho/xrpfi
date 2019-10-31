import React from "react";
import {News} from "../model/model";
import {NewsItem} from "./NewsItem";

interface NewsListProps {
    news: News[]
}

export const NewsList: React.FC<NewsListProps> = (props) => {
    return (
        <>
            {props.news.map(news => <NewsItem news={news}/>)}
        </>
    );
};
