import {News} from "../model/model";
import {MAX_RETURNED_NEWS} from "../utils/constants";

let globalNews = new Map<String, News>();

function sortNews(a: News, b: News) {
    const o1 = -a.priority;
    const o2 = -b.priority;

    if (o1 < o2) return -1;
    if (o1 > o2) return 1;

    const p1 = a.date;
    const p2 = b.date;

    if (p1 < p2) return -1;
    if (p1 > p2) return 1;

    const t1 = a.title;
    const t2 = b.title;

    if (t1 < t2) return -1;
    if (t1 > t2) return 1;

    return 0;
}

export function addNews(news: News[]) {
    news.forEach(news => globalNews.set(news.guid, news));
}

export function getNews(): News[] {
    const news = Array.from(globalNews.values());
    news.sort(sortNews);
    return news.splice(0, MAX_RETURNED_NEWS);
}
