import React from "react";
import {News} from "../model/model";

interface NewsListItemProps {
    news: News,
    selectNewsHandler: (guid: string) => void
}

export const NewsListItem: React.FC<NewsListItemProps> = (props) => {
    return (
        <div onClick={() => props.selectNewsHandler(props.news.guid)}>
            <h6><a href={props.news.url}>{props.news.title}</a></h6>
            <p>{JSON.stringify(props.news.tags)}</p>
        </div>
    );
};
