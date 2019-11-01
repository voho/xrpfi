import React from "react";

interface LoadingIndicatorProps {
    title: string,
    loading: boolean,
    error?: string
}

export const LoadingIndicator: React.FC<LoadingIndicatorProps> = (props) => {
    if (props.loading) {
        return <>Loading...</>;
    }
    if (props.error) {
        return <>Error: {props.error}</>;
    }
    return null;
};
