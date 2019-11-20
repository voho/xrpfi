import "normalize.css";
import React, {useReducer} from "react";
import {BrowserRouter, Redirect, Route, Switch} from "react-router-dom";
import {NewsState, StatusState, TickersState} from "../../common/src/model";
import "./App.scss";
import {NewsCenter} from "./component/news/NewsCenter";
import {newsReducer, UseNewsReducerContext} from "./service/NewsReducer";
import {statusReducer, UseStatusReducerContext} from "./service/StatusReducer";
import {tickersReducer, UseTickersReducerContext} from "./service/TickersReducer";

const initialNewsState: NewsState = {loading: false, news: [], selectedTagIds: ["news"]};
const initialStatusState: StatusState = {loading: false, status: []};
const initialTickerState: TickersState = {loading: false};

export const App: React.FC = () => {
    const [newsState, newsDispatch] = useReducer(newsReducer, initialNewsState);
    const [statusState, statusDispatch] = useReducer(statusReducer, initialStatusState);
    const [tickersState, tickersDispatch] = useReducer(tickersReducer, initialTickerState);

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
