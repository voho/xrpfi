export interface Action {
    type: string
}

export interface NewsState {
    loading: boolean,
    error?: string,
    news: News[],
    selectedNewsGuid?: string
}

export interface StatusState {
    loading: boolean,
    error?: string,
    status: Meta[]
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

export interface News {
    date: number,
    guid: string,
    url: string,
    title: string,
    author: string | null,
    body: string,
    tags: string[],
    sourceUrls: string[],
    externalUrls: string[]
    imageUrls: string[],
    videoId: string | null,
    sourceId: string,
    sourceName: string,
    sourceHomeUrl: string,
    avatarImageUrls: string[] | null,
    quality: number,
    custom: any,
    rating: Rating | null,
    stats: Stats | null
}

interface Stats {
    views: number
}

interface Rating {
    avg: number,
    min: number,
    max: number,
    count: number
}

export interface Meta {
    feedUrl: string,
    homeUrl: string,
    lastError: string | null,
    lastUpdateStartDate: number,
    lastUpdateEndDate: number,
    lastUpdateNewsCount: number,
    status: string
    title: string
}
