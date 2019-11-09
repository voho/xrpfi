import React from "react";
import {News} from "../../service/model";
import "./NewsDetail.scss";
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
