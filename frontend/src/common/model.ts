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
    title: string,
    customer: boolean
}

export type TagId = "twitter" | "social" | "news" | "good" | "official" | "community" | "bot" | "reddit" | "filter" | "youtube";

export const KNOWN_TAGS: TagMeta[] = [
    {id: "twitter", title: "Twitter", customer: true},
    {id: "social", title: "Social", customer: true},
    {id: "news", title: "News", customer: true},
    {id: "good", title: "Good", customer: false},
    {id: "official", title: "Official", customer: true},
    {id: "community", title: "Community", customer: true},
    {id: "bot", title: "Bot", customer: true},
    {id: "reddit", title: "Reddit", customer: true},
    {id: "filter", title: "Filtered", customer: false},
    {id: "youtube", title: "YouTube", customer: true}
];

export interface NewsQueryByTags {
    whitelist: TagId[] | null,
    blacklist: TagId[] | null
}
