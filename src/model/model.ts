export interface GlobalState {
    loading: boolean,
    error?: string,
    root?: Root
}

export interface Root {
    news: News[]
}

export interface News {
    title: string
}
