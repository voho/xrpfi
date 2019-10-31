import React from "react";

interface PriceIndicatorProps {
    symbol: string,
    price: number,
    change: number
    changeLabel: string
}

const Percentage: React.FC<{ value: number }> = (props) => {
    return <>{(props.value * 100.0).toPrecision(2)}%</>;
};

const Change: React.FC<{ value: number }> = (props) => {
    if (props.value > 0.01) {
        return <span className={"color-positive"}>▲ <Percentage value={props.value}/></span>;
    } else if (props.value < -0.01) {
        return <span className={"color-negative"}>▼ <Percentage value={props.value}/></span>;
    } else {
        return <span><Percentage value={props.value}/></span>;
    }
};

export const PriceIndicator: React.FC<PriceIndicatorProps> = (props) => {
    return (
        <>
            <div className={"price-indicator"}>
                <p>
                    <span className={"price-symbol"}>{props.symbol}</span>
                </p>
                <p>
                    <span className={"price-value"}>{props.price.toFixed(3)}</span>
                </p>
                <p>
                    <span className={"price-change"}><Change value={props.change}/></span>
                    <span className={"price-change-label"}>{props.changeLabel}</span>
                </p>
            </div>
        </>
    );
};
