import moment from "moment";
import React, {useContext, useEffect} from "react";
import {Meta} from "../../../../backend/src/model/model";
import {scheduleRegularStatusUpdate} from "../../service/api";
import {UseStatusReducerContext} from "../../service/StatusReducer";
import "./SourcesStatus.scss";
import {TradingChart} from "./TradingChart";

const SourceTableStatus: React.FC<{ status: string, lastError: string | null }> = (props) => {
    function getClassName() {
        switch (props.status) {
            case "OK":
                return "color-positive";
            case "ERROR":
                return "color-negative";
            default:
                return "";
        }
    }

    return (
        <span className={getClassName()}>{props.status}
            <SourceTableLastError error={props.lastError}/>
    </span>
    );
};

const SourceTableLatency: React.FC<{ start: number, end: number }> = (props) => {
    const diff = (props.end - props.start) / 1000.0;
    return <small>{diff} s</small>;
};

const SourceTableTiming: React.FC<{ end: number }> = (props) => {
    return <b>{moment(props.end).fromNow()}</b>;
};

const SourceTableLastError: React.FC<{ error: string | null }> = (props) => {
    if (!props.error) {
        return null;
    }
    return <small><br/>{props.error}</small>;
};

const SourceTableCount: React.FC<{ count: number | null }> = (props) => {
    if (!props.count) {
        return null;
    }
    return <span>{props.count}</span>;
};

const SourceTableRow: React.FC<{ row: Meta }> = (props) => {
    return (
        <tr>
            <td>
                <a href={props.row.homeUrl} target={"_blank"}>{props.row.title}</a>
                <small> (<a href={props.row.feedUrl}>feed</a>)</small>
            </td>
            <td><SourceTableStatus status={props.row.status} lastError={props.row.lastError}/></td>
            <td><SourceTableLatency start={props.row.lastUpdateStartDate} end={props.row.lastUpdateEndDate}/></td>
            <td><SourceTableTiming end={props.row.lastUpdateEndDate}/></td>
            <td><SourceTableCount count={props.row.lastUpdateNewsCount}/></td>
        </tr>
    );
};

const SourceTable: React.FC<{ rows: Meta[] }> = (props) => {
    return (
        <table className={"sources"}>
            <thead>
            <tr>
                <th>Source</th>
                <th>Status</th>
                <th>Latency</th>
                <th>Updated</th>
                <th>News</th>
            </tr>
            </thead>
            <tbody>
            {props.rows.map(row => <SourceTableRow row={row}/>)}
            </tbody>
        </table>
    );
};

export const SourcesStatus = () => {
    const context = useContext(UseStatusReducerContext);

    useEffect(() => scheduleRegularStatusUpdate(context.dispatch), []);

    return (
        <>
            <h2>Actual Price</h2>
            <TradingChart/>
            <h2>News Sources</h2>
            <SourceTable rows={context.state.status}/>
        </>
    );
};
