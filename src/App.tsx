import React, {useEffect, useState} from "react";
import "./App.css";
import {NewsList} from "./component/NewsList";
import {GlobalState} from "./model/model";
import {fetchRoot} from "./service/api";

function scheduleUpdate(setState: any) {
    setState({loading: true, error: undefined});

    let refreshCallback = () => {
        fetchRoot()
            .then(newState => {
                console.log("new state! " + JSON.stringify(newState));
                setState({loading: false, error: undefined, root: newState});
            })
            .catch(error => setState({loading: false, error: error.toString()}));

        setTimeout(refreshCallback, 5000);
    };

    setTimeout(refreshCallback, 1000);
}

export const App: React.FC = () => {
    const [state, setState] = useState({loading: false, root: undefined} as GlobalState);

    useEffect(() => {
        scheduleUpdate(setState);
    }, []);

    return (
        <div>
            {state.loading && <p>Loading...</p>}
            {state.root && <NewsList news={state.root!.news}/>}
        </div>
    );
};
