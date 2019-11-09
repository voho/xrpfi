import React, {useContext} from "react";
import {Meta} from "../../../../backend/src/model/model";
import {UseNewsReducerContext} from "../../service/NewsReducer";
import "./SourcesStatus.scss";
import {TradingChart} from "./TradingChart";

const SourceTableStatus: React.FC<{ status: string }> = (props) => {
    const className = props.status === "DB_UPDATE_FINISHED"
        ? "color-positive"
        : "color-negative";

    return <span className={className}>{props.status}</span>;
};

const SourceTableTiming: React.FC<{ start: number, end: number }> = (props) => {
    const diff = (props.end - props.start) / 1000.0;
    return (
        <>
            <br/>
            <small>Latency: <b>{diff}</b> s</small>
        </>
    );
};

const SourceTableLastError: React.FC<{ error: string | null }> = (props) => {
    if (!props.error) {
        return null;
    }
    return (
        <>
            <br/>
            <small>{props.error}</small>
        </>
    );
};

const SourceTableRow: React.FC<{ row: Meta }> = (props) => {
    return (
        <tr>
            <td>
                <a href={props.row.homeUrl} target={"_blank"}>{props.row.title}</a>
                <small> (<a href={props.row.feedUrl}>feed</a>)</small>
            </td>
            <td>
                <SourceTableStatus status={props.row.status}/>
                <SourceTableTiming start={props.row.lastUpdateStartDate} end={props.row.lastUpdateEndDate}/>
                <SourceTableLastError error={props.row.lastError}/>
            </td>
        </tr>
    );
};

const SourceTable: React.FC<{ rows: Meta[] }> = (props) => {
    return (
        <table className={"sources"}>
            <thead>
            <tr>
                <th>Source</th>
                <th>Status and Last Error</th>
            </tr>
            </thead>
            <tbody>
            {props.rows.map(row => <SourceTableRow row={row}/>)}
            </tbody>
        </table>
    );
};

export const SourcesStatus = () => {
    const context = useContext(UseNewsReducerContext);

    return (
        <>
            <h2>Actual Price</h2>
            <TradingChart/>
            <h2>News Sources</h2>
            <SourceTable rows={context.state.root!.meta || []}/>
        </>
    );
};
