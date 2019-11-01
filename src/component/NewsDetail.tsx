import React from "react";
import {News} from "../service/model";
import "./NewsDetail.scss";

interface NewsDetailProps {
    selectedNews?: News
}

function createMarkup(html: string) {
    return {__html: html};
}

const NoNewsSelected = () => {
    return <p className={"empty-news"}>No News selected.</p>;
};

const NewsSelected: React.FC<NewsDetailProps> = (props) => {
    if (!props.selectedNews) {
        return <NoNewsSelected/>;
    }

    return (
        <>
            <h2>{props.selectedNews!.title}</h2>
            <div className={"external"} dangerouslySetInnerHTML={createMarkup(props.selectedNews!.body)}/>
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
