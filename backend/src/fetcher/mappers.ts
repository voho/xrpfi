import Parser from "rss-parser";
import {News} from "../model/model";
import {logError} from "../utils/logger";

const parser = new Parser();

export function twitterRssMapper(response: string): News[] {
    const news = genericRssMapper(response);
    news.forEach(n => n.sourceId = "twitter");
    return news;
}

export function redditRssMapper(response: string): News[] {
    const news = genericRssMapper(response);
    news.forEach(n => n.sourceId = "reddit");
    return news;
}

export function genericRssMapper(response: string): News[] {
    const news = [] as News[];
    rssToJson(response)
        .then(feed => {
            feed.items.forEach(item => {
                const itemAsNews = {
                    url: item.link,
                    title: item.title,
                    priority: 0,
                    body: item.content,
                    imageUrls: [],
                    avatarImageUrls: [],
                    date: Date.parse(item.pubDate),
                    guid: item.link,
                    tags: [],
                    sourceId: "",
                    sourceName: feed.title,
                    sourceHomeUrl: feed.link,
                    sourceUrls: [],
                    externalUrls: [],
                    videoId: null
                };

                if (feed.image) {
                    itemAsNews.avatarImageUrls.push(feed.image.url);
                }

                news.push(itemAsNews);
            });
        })
        .catch(error => {
            logError(error.message);
        });
    return news;
}

function rssToJson(response: string): Promise<any> {
    return parser.parseString(response);
}
