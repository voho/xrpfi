import React from "react";
import {News} from "../model/model";
import {NewsListItem} from "./NewsListItem";

interface NewsListProps {
    news: News[]
}

export const NewsList: React.FC<NewsListProps> = (props) => {
    return (
        <div>
            {props.news.map(news => <NewsListItem news={news}/>)}
        </div>
    );
};
