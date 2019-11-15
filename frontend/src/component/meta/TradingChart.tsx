import React from "react";
import "./TradingChart.scss";

export const TradingChart = () => {
    return (
        <iframe
            title={"Trading Chart"}
            className={"trading-chart"}
            src="https://www.tradingview.com/widgetembed/?symbol=BITSTAMP%3AXRPUSD&amp;interval=30&amp;hidelegend=1&amp;hidesidetoolbar=1&amp;symboledit=0&amp;saveimage=0&amp;toolbarbg=rgba(12,23,34,1)&amp;theme=Dark&amp;style=1&amp;locale=en&amp;utm_source=www.xrp.fi"
            frameBorder="0"/>
    );
};


