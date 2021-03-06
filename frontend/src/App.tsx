import "normalize.css";
import React, {useReducer} from "react";
import {BrowserRouter, Redirect, Route, Switch} from "react-router-dom";
import "./App.scss";
import {NEWS_UPDATE_INTERVAL_MS} from "./common/constants";
import {useInterval} from "./common/hooks";
import {KNOWN_TAGS, NewsState, StatusState, TickersState} from "./common/model";
import {SourcesStatus} from "./component/meta/SourcesStatus";
import {NewsCenter} from "./component/news/NewsCenter";
import {updateNews} from "./service/api";
import {newsReducer, UseNewsReducerContext} from "./service/NewsReducer";
import {statusReducer, UseStatusReducerContext} from "./service/StatusReducer";
import {tickersReducer, UseTickersReducerContext} from "./service/TickersReducer";

const initialSelectedTagIds = KNOWN_TAGS.map(tag => tag.id).filter(tagId => tagId !== "twitter");
const initialNewsState: NewsState = {loading: false, news: [], selectedTagIds: initialSelectedTagIds};
const initialStatusState: StatusState = {loading: false, status: []};
const initialTickerState: TickersState = {loading: false};

export const App: React.FC = () => {
    const [newsState, newsDispatch] = useReducer(newsReducer, initialNewsState);
    const [statusState, statusDispatch] = useReducer(statusReducer, initialStatusState);
    const [tickersState, tickersDispatch] = useReducer(tickersReducer, initialTickerState);

    useInterval(() => { updateNews(newsState, newsDispatch); }, NEWS_UPDATE_INTERVAL_MS, true);

    return (
        <UseNewsReducerContext.Provider value={{state: newsState, dispatch: newsDispatch}}>
            <UseTickersReducerContext.Provider value={{state: tickersState, dispatch: tickersDispatch}}>
                <UseStatusReducerContext.Provider value={{state: statusState, dispatch: statusDispatch}}>
                    <BrowserRouter>
                        <Switch>
                            <Route path={"/"} component={NewsCenter}/>
                            <Route path={"/status"} component={SourcesStatus}/>
                            <Redirect to={"/"}/>
                        </Switch>
                    </BrowserRouter>
                </UseStatusReducerContext.Provider>
            </UseTickersReducerContext.Provider>
        </UseNewsReducerContext.Provider>
    );
};
