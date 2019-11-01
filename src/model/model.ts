export interface GlobalState {
    newsLoading: boolean,
    newsLoadingError?: string,
    news?: News[]
    selectedNewsGuid?: string,
    tickers?: Tickers,
    tickersLoading: boolean,
    tickersLoadingError?: string
}

export interface Tickers {
    xrp_btc_price: number,
    xrp_usd_price: number,
    xrp_btc_change1d: number,
    xrp_usb_change1d: number
}

export interface Root {
    news: News[]
}

export interface News {
    hash: number,
    date: number,
    guid: string,
    url: string,
    title: string,
    body: string,
    tags: string[],
    sourceUrls: string[],
    externalUrls: string[]
    imageUrls: string[],
    videoId: string | null,
    sourceId: string,
    sourceName: string,
    sourceHomeUrl: string
}
