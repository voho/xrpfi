import "normalize.css";
import React, {useReducer} from "react";
import "./App.css";
import {NewsCenter} from "./component/NewsCenter";
import {NewsState, TickersState} from "./service/model";
import {newsReducer, UseNewsReducerContext} from "./service/NewsReducer";
import {tickersReducer, UseTickersReducerContext} from "./service/TickersReducer";

export const App: React.FC = () => {
    const [newsState, newsDispatch] = useReducer(newsReducer, {loading: false} as NewsState);
    const [tickersState, tickersDispatch] = useReducer(tickersReducer, {loading: false} as TickersState);

    return (
        <UseNewsReducerContext.Provider value={{state: newsState, dispatch: newsDispatch}}>
            <UseTickersReducerContext.Provider value={{state: tickersState, dispatch: tickersDispatch}}>
                <NewsCenter/>
            </UseTickersReducerContext.Provider>
        </UseNewsReducerContext.Provider>

    );
};
