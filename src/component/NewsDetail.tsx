import React from "react";
import {News} from "../model/model";

interface NewsDetailProps {
    selectedNews?: News
}

export const NewsDetail: React.FC<NewsDetailProps> = (props) => {
    if (!props.selectedNews) {
        return <p>No News selected.</p>;
    }

    return (
        <div>
            <h3>{props.selectedNews!.title}</h3>
            <p>{props.selectedNews!.body}</p>
        </div>
    );
};
