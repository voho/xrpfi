import React, {useContext} from "react";
import logoSrc from "../../image/logo.svg";
import {UseNewsReducerContext} from "../../service/NewsReducer";
import {UseTickersReducerContext} from "../../service/TickersReducer";
import {LoadingIndicator} from "../common/LoadingIndicator";
import {PriceIndicator} from "./PriceIndicator";
//import {TagChecker} from "./TagChecker";

export const NewsControl = () => {
    const tickerContext = useContext(UseTickersReducerContext);
    const newsContext = useContext(UseNewsReducerContext);

    const tickers = tickerContext.state.tickers || {xrp_btc_change1d: 0, xrp_btc_price: 0, xrp_usb_change1d: 0, xrp_usd_price: 0};

    return (
        <div className={"news-control"}>
            <img src={logoSrc} width={30} alt={"XRP"}/><br/>
            {/*<TagChecker/>*/}
            <PriceIndicator symbol={"XRP/USD"} price={tickers.xrp_usd_price} change={tickers.xrp_usb_change1d} changeLabel={"1d"}/>
            <PriceIndicator symbol={"XRP/BTC"} price={tickers.xrp_btc_price} change={tickers.xrp_btc_change1d} changeLabel={"1d"}/>
            <LoadingIndicator title={"Tickers"} loading={tickerContext.state.loading} error={tickerContext.state.error}/>
            <LoadingIndicator title={"News"} loading={newsContext.state.loading} error={newsContext.state.error}/>
        </div>
    );
};
