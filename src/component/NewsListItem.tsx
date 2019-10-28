import React from "react";
import {News} from "../model/model";

interface NewsListItemProps {
    news: News
}

export const NewsListItem: React.FC<NewsListItemProps> = (props) => {
    return (
        <div>
            <p>{props.news.title}</p>
        </div>
    );
};
