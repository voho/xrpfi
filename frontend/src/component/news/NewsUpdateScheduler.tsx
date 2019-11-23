import React, {useEffect, useRef} from "react";
import {NEWS_UPDATE_INTERVAL_MS} from "../../common/constants";
import {updateNews} from "../../service/api";

export const NewsUpdateScheduler: React.FC<{ newsState, newsDispatch }> = (props) => {
    const stateRef = useRef(props.newsState);
    stateRef.current = props.newsState;

    useEffect(() => {
        function updateNewsCallback() {
            updateNews(props.newsDispatch, stateRef.current);
        }

        updateNewsCallback();
        const interval = setInterval(updateNewsCallback, NEWS_UPDATE_INTERVAL_MS);
        return () => clearInterval(interval);
    }, []);

    return null;
};
