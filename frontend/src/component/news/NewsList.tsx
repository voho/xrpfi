import React, {useContext} from "react";
import {News} from "../../../../backend/src/model/model";
import {NewsSelectAction, UseNewsReducerContext} from "../../service/NewsReducer";
import {UseStatusReducerContext} from "../../service/StatusReducer";
import "./NewsDetail.scss";
import {NewsItem} from "./NewsItem";

interface NewsListProps {
    news: News[]
}

export const NewsList: React.FC<NewsListProps> = (props) => {
    const newsContext = useContext(UseNewsReducerContext);
    const statusContext = useContext(UseStatusReducerContext);

    function isAllowed(news: News): boolean {
        let has = false;
        news.tags.forEach(tag => {
            if (statusContext.state.selectedTags.includes(tag)) {
                has = true;
            }
        });
        return has;
    }

    function selectNews(guid: string): void {
        newsContext.dispatch({type: "news_select", selected: guid} as NewsSelectAction);
    }

    function handleSelectNeighbour(delta: number): void {
        const guids = newsContext.state.news.map(a => a.guid);
        const selIndex = guids.indexOf(newsContext.state.selectedNewsGuid || "");
        if (selIndex !== -1) {
            const newIndex = selIndex + delta;
            if (newIndex >= 0 && newIndex < guids.length) {
                selectNews(guids[newIndex]);
            }
        }
    }

    document.onkeydown = (e) => {
        switch (e.key) {
            case "ArrowUp":
            case "ArrowLeft":
                handleSelectNeighbour(-1);
                e.preventDefault();
                break;
            case "ArrowDown":
            case "ArrowRight":
                handleSelectNeighbour(1);
                e.preventDefault();
                break;
        }
    };

    return (
        <>
            {props.news.filter(news => isAllowed(news)).map(news => <NewsItem news={news}/>)}
        </>
    );
};
