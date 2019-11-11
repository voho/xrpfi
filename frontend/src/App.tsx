// @ts-ignore
import {NewsState, StatusState, TickersState} from "@xrpfi/backend/build/model/model";
import "normalize.css";
import React, {useEffect, useReducer} from "react";
import {BrowserRouter, Redirect, Route, Switch} from "react-router-dom";
import "./App.css";
import {NewsCenter} from "./component/news/NewsCenter";
import {scheduleRegularStatusUpdate, scheduleRegularTickersUpdate} from "./service/api";
import {newsReducer, UseNewsReducerContext} from "./service/NewsReducer";
import {statusReducer, UseStatusReducerContext} from "./service/StatusReducer";
import {tickersReducer, UseTickersReducerContext} from "./service/TickersReducer";

export const App: React.FC = () => {
    const [newsState, newsDispatch] = useReducer(newsReducer, {loading: false, news: []} as NewsState);
    const [statusState, statusDispatch] = useReducer(statusReducer, {loading: false, status: []} as StatusState);
    const [tickersState, tickersDispatch] = useReducer(tickersReducer, {loading: false} as TickersState);

    useEffect(
        () => {
            scheduleRegularTickersUpdate(tickersDispatch);
            scheduleRegularStatusUpdate(statusDispatch);
        },
        []);

    return (
        <UseNewsReducerContext.Provider value={{state: newsState, dispatch: newsDispatch}}>
            <UseTickersReducerContext.Provider value={{state: tickersState, dispatch: tickersDispatch}}>
                <UseStatusReducerContext.Provider value={{state: statusState, dispatch: statusDispatch}}>
                    <BrowserRouter>
                        <Switch>
                            <Route path={"/"} component={NewsCenter}/>
                            <Redirect to={"/"}/>
                        </Switch>
                    </BrowserRouter>
                </UseStatusReducerContext.Provider>
            </UseTickersReducerContext.Provider>
        </UseNewsReducerContext.Provider>
    );
};
