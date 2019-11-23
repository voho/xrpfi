import React, {useContext} from "react";
import {News} from "../../common/model";
import {NewsSelectAction, UseNewsReducerContext} from "../../service/NewsReducer";
import "./NewsDetail.scss";
import {NewsItem} from "./NewsItem";

interface NewsListProps {
    news: News[]
}

export const NewsList: React.FC<NewsListProps> = (props) => {
    const newsContext = useContext(UseNewsReducerContext);

    function selectNews(guid: string): void {
        newsContext.dispatch({type: "news_select", selected: guid} as NewsSelectAction);
    }

    function handleSelectNeighbour(delta: number): void {
        if (!newsContext.state.selectedNews) {
            return;
        }
        const guids = newsContext.state.news.map(a => a.guid);
        const selIndex = guids.indexOf(newsContext.state.selectedNews!.guid || "");
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
            {props.news.map(news => <NewsItem news={news}/>)}
        </>
    );
};
