// @ts-ignore
import CryptowatchEmbed from "cryptowatch-embed";
import React, {useEffect} from "react";
import "./PriceChartEmbed.scss";

export const PriceChartEmbed: React.FC = () => {
    useEffect(() => {
        let chart = new CryptowatchEmbed("bitstamp", "xrpusd");
        chart.mount("#chart-container");
    }, []);

    return (
        <div id={"chart-container"}/>
    );
};
