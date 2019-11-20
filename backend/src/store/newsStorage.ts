import {News, NewsQueryByTags} from "@xrpfi/common/build/model";
import {MAX_RETURNED_NEWS, OLD_NEWS_CUTOFF_TIME_MS} from "../../../common/src/constants";
import {logInfo} from "../utils/logger";

const globalNews = new Map<string, News>();

export function getNews(query: NewsQueryByTags): News[] {
    removeOldNews();
    const news = Array.from(globalNews.values()).filter(n => filterNews(n, query));
    news.sort(sortNews);
    return news.splice(0, MAX_RETURNED_NEWS);
}

export function addNews(news: News[]) {
    news.forEach(news => globalNews.set(news.guid, news));
}

function filterNews(n: News, query: NewsQueryByTags) {
    if (query.whitelist != null) {
        if (!hasAnyTag(n.tags, query.whitelist)) {
            // whitelist active, but no news tags are on whitelist
            return false;
        }
    }
    if (query.blacklist != null) {
        if (hasAnyTag(n.tags, query.blacklist)) {
            // blacklist active, and one news tag is on blacklist
            return false;
        }
    }
    // all good
    return true;
}

function hasAnyTag(newsTags: string[], checkingTags: string[]) {
    return newsTags.some(n => checkingTags.includes(n));
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
    const o1 = -a.quality;
    const o2 = -b.quality;

    if (o1 < o2) return -1;
    if (o1 > o2) return 1;

    const p1 = a.date;
    const p2 = b.date;

    if (p1 < p2) return 1;
    if (p1 > p2) return -1;

    const t1 = a.title;
    const t2 = b.title;

    if (t1 < t2) return -1;
    if (t1 > t2) return 1;

    return 0;
}
