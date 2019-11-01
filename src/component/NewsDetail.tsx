import React from "react";
import {News} from "../service/model";
import "./NewsDetail.scss";

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
        <div className={"inside"}>
            <h3>{props.selectedNews!.title}</h3>
            <div dangerouslySetInnerHTML={createMarkup(props.selectedNews!.body)}/>
            <div>
                <pre>{JSON.stringify(props.selectedNews!, null, 2)}</pre>
            </div>
        </div>
    );
};
