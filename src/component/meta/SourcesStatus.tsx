import React, {useContext} from "react";
import {Meta} from "../../service/model";
import {UseNewsReducerContext} from "../../service/NewsReducer";
import "./SourcesStatus.scss";

const SourceTableStatus: React.FC<{ status: string }> = (props) => {
    const className = props.status === "DB_UPDATE_FINISHED"
        ? "color-positive"
        : "color-negative";

    return <span className={className}>{props.status}</span>;
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
            <h2>News Sources</h2>
            <SourceTable rows={context.state.root!.meta || []}/>
        </>
    );
};
