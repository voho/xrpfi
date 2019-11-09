// @ts-ignore
import {NewsState, TickersState} from "@xrpfi/backend/build/model/model";
import "normalize.css";
import React, {useEffect, useReducer} from "react";
import {BrowserRouter, Redirect, Route, Switch} from "react-router-dom";
import "./App.css";
import {NewsCenter} from "./component/news/NewsCenter";
import {scheduleRegularNewsUpdate, scheduleRegularTickersUpdate} from "./service/api";
import {newsReducer, UseNewsReducerContext} from "./service/NewsReducer";
import {tickersReducer, UseTickersReducerContext} from "./service/TickersReducer";

export const App: React.FC = () => {
    const [newsState, newsDispatch] = useReducer(newsReducer, {loading: false} as NewsState);
    const [tickersState, tickersDispatch] = useReducer(tickersReducer, {loading: false} as TickersState);

    useEffect(
        () => {
            scheduleRegularNewsUpdate(newsDispatch);
            scheduleRegularTickersUpdate(tickersDispatch);
        },
        []);

    return (
        <UseNewsReducerContext.Provider value={{state: newsState, dispatch: newsDispatch}}>
            <UseTickersReducerContext.Provider value={{state: tickersState, dispatch: tickersDispatch}}>
                <BrowserRouter>
                    <Switch>
                        <Route path={"/"} component={NewsCenter}/>
                        <Redirect to={"/"}/>
                    </Switch>
                </BrowserRouter>
            </UseTickersReducerContext.Provider>
        </UseNewsReducerContext.Provider>
    );
};
