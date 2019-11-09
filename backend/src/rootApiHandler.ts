export interface NewsX {
    news: any[],
    meta: any[]
}

export function getRoot(): NewsX {
    return {
        news: [],
        meta: []
    };
}
