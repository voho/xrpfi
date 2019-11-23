import React, {useEffect, useRef} from "react";
import {NEWS_UPDATE_INTERVAL_MS} from "../../common/constants";
import {Action, NewsState} from "../../common/model";
import {updateNews} from "../../service/api";

export const NewsUpdateScheduler: React.FC<{ newsState: NewsState, newsDispatch: React.Dispatch<Action> }> = (props) => {
    const stateRef = useRef(props.newsState);
    stateRef.current = props.newsState;

    useEffect(() => {
        function updateNewsCallback() {
            updateNews(stateRef.current, props.newsDispatch);
        }

        updateNewsCallback();
        const interval = setInterval(updateNewsCallback, NEWS_UPDATE_INTERVAL_MS);
        return () => clearInterval(interval);
    }, []);

    return null;
};
