export interface GlobalState {
    loading: boolean,
    selected?: string,
    error?: string,
    root?: Root
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
    tags: string[]
}
