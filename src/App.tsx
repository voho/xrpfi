import React, {useReducer} from "react";
import "./App.css";
import {NewsCenter} from "./component/NewsCenter";
import {globalReducer, GlobalReducerContext} from "./model/reducer";

export const App: React.FC = () => {
    const [state, dispatch] = useReducer(globalReducer, {loading: false});

    return (
        <GlobalReducerContext.Provider value={{state, dispatch}}>
            <NewsCenter/>
        </GlobalReducerContext.Provider>
    );
};
