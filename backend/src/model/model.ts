export interface Action {
    type: string
}

export interface NewsState {
    loading: boolean,
    error?: string,
    root?: Root,
    selectedNewsGuid?: string
}

export interface TickersState {
    tickers?: Tickers,
    loading: boolean,
    error?: string
}

export interface Tickers {
    xrp_btc_price: number,
    xrp_usd_price: number,
    xrp_btc_change1d: number,
    xrp_usb_change1d: number
}

export interface Root {
    news: News[],
    meta: Meta[]
}

export interface News {
    priority: number,
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
    sourceHomeUrl: string,
    avatarImageUrls: string[] | null
}

export interface Meta {
    feedUrl: string,
    homeUrl: string,
    lastError: string | null,
    lastUpdateStartDate: number,
    lastUpdateEndDate: number,
    status: string
    title: string
}
