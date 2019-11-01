import React from "react";
import {News} from "../model/model";

interface NewsDetailProps {
    selectedNews?: News
}

function createMarkup(html: string) {
    return {__html: html};
}

export const NewsDetail: React.FC<NewsDetailProps> = (props) => {
    if (!props.selectedNews) {
        return <p>No News selected.</p>;
    }

    return (
        <div>
            <h3>{props.selectedNews!.title}</h3>
            <div dangerouslySetInnerHTML={createMarkup(props.selectedNews!.body)}/>
        </div>
    );
};
