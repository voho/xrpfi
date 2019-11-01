import React from "react";
import "./LoadingIndicator.scss";

interface LoadingIndicatorProps {
    title: string,
    loading: boolean,
    error?: string
}

export const LoadingIndicator: React.FC<LoadingIndicatorProps> = (props) => {
    return <p className={"loading-indicator"}>{props.title} {props.loading ? "..." : props.error ? "❌" : "✔️"}</p>;
};
