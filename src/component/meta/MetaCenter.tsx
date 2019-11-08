import React from "react";
import {NewsControl} from "../news/NewsControl";
import {SourcesStatus} from "./SourcesStatus";

export const MetaCenter = () => {
    return (
        <>
            <div className={"news-site-menu"}>
                <NewsControl/>
            </div>
            <div className={"news-site-detail"}>
                <SourcesStatus/>
            </div>
        </>
    );
};
