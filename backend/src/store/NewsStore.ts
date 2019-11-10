import {News} from "../model/model";

let globalNews = [];

export function addNews(news: News[]) {
    globalNews.push(...news);
    globalNews = globalNews.splice(0, 100);
    // TODO remove old ones
}

export function getNews(): News[] {
    return globalNews;
}
