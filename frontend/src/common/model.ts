export interface Action {
    type: string
}

export interface NewsState {
    loading: boolean,
    error?: string,
    news: News[],
    selectedNews?: News,
    selectedTagIds: TagId[]
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
    tags: TagId[],
    sourceUrls: string[],
    externalUrls: string[]
    imageUrls: string[],
    oembedUrl: string | null,
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

export interface TagMeta {
    id: TagId,
    title: string
}

export type TagId = "twitter" | "social" | "news" | "good" | "official" | "community" | "bot" | "reddit" | "filter" | "youtube";

export const KNOWN_TAGS: TagMeta[] = [
    {id: "twitter", title: "Twitter"},
    {id: "social", title: "social"},
    {id: "news", title: "news"},
    {id: "good", title: "good"},
    {id: "official", title: "official"},
    {id: "community", title: "community"},
    {id: "bot", title: "bot"},
    {id: "reddit", title: "reddit"},
    {id: "filter", title: "filter"},
    {id: "youtube", title: "youtube"}
];

export interface NewsQueryByTags {
    whitelist: TagId[] | null,
    blacklist: TagId[] | null
}
