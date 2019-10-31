import React, {useEffect, useState} from "react";
import logoSrc from "../image/logo.svg";
import {PriceIndicator} from "./PriceIndicator";

type PriceUpdater = (state: TickerState) => void;

interface TickerState {
    xrp_btc_price: number,
    xrp_btc_change1d: number,
    xrp_usd_price: number,
    xrp_usb_change1d: number
}

function scheduleUpdate(setState: PriceUpdater) {
    const url = "https://min-api.cryptocompare.com/data/pricemultifull?fsyms=XRP&tsyms=BTC,USD";
    console.log("updating");

    fetch(url)
        .then(response => response.json())
        .then(responseJson => {
            setState({
                xrp_btc_price: responseJson.RAW.XRP.BTC.PRICE * 100000000,
                xrp_usd_price: responseJson.RAW.XRP.USD.PRICE,
                xrp_btc_change1d: responseJson.RAW.XRP.BTC.CHANGEPCT24HOUR / 100.0,
                xrp_usb_change1d: responseJson.RAW.XRP.USD.CHANGEPCT24HOUR / 100.0
            });

            setTimeout(() => {scheduleUpdate(setState);}, 30000);
        });
}

interface NewsControlProps {

}

export const NewsControl: React.FC<NewsControlProps> = (props) => {
    const [ticker, setTicker] = useState({xrp_usd_price: 0, xrp_btc_price: 0} as TickerState);

    useEffect(() => {scheduleUpdate(setTicker);}, []);

    return (
        <div className={"news-control"}>
            <img src={logoSrc} width={50} alt={"XRP"}/><br/>
            <h1>XRP<span>.fi</span></h1>
            <PriceIndicator symbol={"XRP/USD"} price={ticker.xrp_usd_price} change={ticker.xrp_usb_change1d} changeLabel={"1d"}/>
            <PriceIndicator symbol={"XRP/BTC"} price={ticker.xrp_btc_price} change={ticker.xrp_btc_change1d} changeLabel={"1d"}/>
        </div>
    );
};
