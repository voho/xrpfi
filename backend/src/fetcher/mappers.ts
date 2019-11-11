import Parser from "rss-parser";
import {News} from "../model/model";

const parser = new Parser();

export function twitterRssMapper(response: string): Promise<News[]> {
    return genericRssMapper(response)
        .then(news => {
            news.forEach(n => n.sourceId = "twitter");
            return news;
        });
}

export function redditRssMapper(response: string): Promise<News[]> {
    return genericRssMapper(response)
        .then(news => {
            news.forEach(n => n.sourceId = "reddit");
            return news;
        });
}

export function youtubeRssMapper(response: string): Promise<News[]> {
    return genericRssMapper(response)
        .then(news => {
            news.forEach(n => n.sourceId = "youtube");
            return news;
        });
}

export function genericRssMapper(response: string): Promise<News[]> {
    return parser.parseString(response)
        .then(feed => {
            const news = [] as News[];

            feed.items.forEach(item => {
                const itemAsNews = {
                    author: item.creator,
                    url: item.link,
                    title: item.title,
                    priority: 0,
                    body: item.content,
                    imageUrls: [],
                    avatarImageUrls: [],
                    date: Date.parse(item.pubDate),
                    guid: item.link,
                    tags: [],
                    sourceId: "generic",
                    sourceName: feed.title,
                    sourceHomeUrl: feed.link,
                    sourceUrls: [],
                    externalUrls: [],
                    videoId: null
                };

                if (feed.image) {
                    itemAsNews.avatarImageUrls.push(feed.image.url);
                }

                if (item.categories && item.categories.length > 0) {
                    itemAsNews.tags = item.categories;
                }

                news.push(itemAsNews);
            });

            return Promise.resolve(news);
        });
}