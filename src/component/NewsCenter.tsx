import React from "react";
import {Action} from "../App";
import {GlobalState} from "../model/model";
import {NewsList} from "./NewsList";

interface NewsCenterProps {
    state: GlobalState,
    dispatch: React.Dispatch<Action<string>>
}

export const NewsCenter: React.FC<NewsCenterProps> = (props) => {
    return (
        <>
            <div className={"news-menu"}>
                {props.state.loading ? "Loading" : "----"}

                <button onClick={() => props.dispatch({type: "load_start"})}>Load</button>
                <button onClick={() => props.dispatch({type: "load_success"})}>OK</button>
                <button onClick={() => props.dispatch({type: "load_error"})}>Bad</button>
            </div>
            <div className={"news-list"}>
                {props.state.root && <NewsList news={props.state.root!.news}/>}
            </div>
            <div className={"news-content"}>
                content
            </div>
        </>
    );
};
