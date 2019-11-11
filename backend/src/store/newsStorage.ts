import {News} from "../model/model";
import {MAX_RETURNED_NEWS, OLD_NEWS_CUTOFF_TIME_MS} from "../utils/constants";
import {logInfo} from "../utils/logger";

const globalNews = new Map<string, News>();

export function getNews(): News[] {
    removeOldNews();
    const news = Array.from(globalNews.values());
    news.sort(sortNews);
    return news.splice(0, MAX_RETURNED_NEWS);
}

export function addNews(news: News[]) {
    news.forEach(news => globalNews.set(news.guid, news));
}

function removeOldNews() {
    const cutoffDate = new Date().getTime() - OLD_NEWS_CUTOFF_TIME_MS;
    const keysToRemove = new Set<string>();

    logInfo(`Removing news with cutoff date ${cutoffDate}...`);

    globalNews.forEach((key, value) => {
        if (key.date < cutoffDate) {
            keysToRemove.add(value);
        }
    });

    keysToRemove.forEach(key => globalNews.delete(key));
    logInfo(`Removed ${keysToRemove.size} old news.`);
}

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