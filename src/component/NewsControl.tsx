import React, {useContext, useEffect} from "react";
import logoSrc from "../image/logo.svg";
import {GlobalReducerContext} from "../model/reducer";
import {scheduleRegularTickersUpdate} from "../service/api";
import {PriceIndicator} from "./PriceIndicator";

export const NewsControl = () => {
    const context = useContext(GlobalReducerContext);
    const tickers = context.state.tickers || {xrp_btc_change1d: 0, xrp_btc_price: 0, xrp_usb_change1d: 0, xrp_usd_price: 0};

    useEffect(() => { scheduleRegularTickersUpdate(context.dispatch); }, []);

    return (
        <div className={"news-control"}>
            <img src={logoSrc} width={50} alt={"XRP"}/><br/>
            <PriceIndicator symbol={"XRP/USD"} price={tickers.xrp_usd_price} change={tickers.xrp_usb_change1d} changeLabel={"1d"}/>
            <PriceIndicator symbol={"XRP/BTC"} price={tickers.xrp_btc_price} change={tickers.xrp_btc_change1d} changeLabel={"1d"}/>
        </div>
    );
};
