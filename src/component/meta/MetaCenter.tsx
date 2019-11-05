import React, {useContext} from "react";
import {UseNewsReducerContext} from "../../service/NewsReducer";
import {NewsControl} from "../news/NewsControl";

export const MetaCenter = () => {
    const context = useContext(UseNewsReducerContext);

    return (
        <>
            <div className={"news-site-menu"}>
                <NewsControl/>
            </div>
            <div className={"news-site-detail"}>
                {JSON.stringify(context.state.meta)}
            </div>
        </>
    );
};
