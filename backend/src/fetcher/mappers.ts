import {News} from "../model/model";
import {RELEVANT_KEYWORDS} from "../utils/constants";
import {Fetcher, Tag} from "./fetcherFactory";

let Parser = require("rss-parser");

export function twitterRssMapper(fetcher: Fetcher, response: string): Promise<News[]> {
    return genericRssMapper(fetcher, response)
        .then(news => {
            news.forEach(n => n.sourceId = "twitter");
            return news;
        });
}

export function redditRssMapper(fetcher: Fetcher, response: string): Promise<News[]> {
    return genericRssMapper(fetcher, response)
        .then(news => {
            news.forEach(n => n.sourceId = "reddit");
            return news;
        });
}

export function youtubeRssMapper(fetcher: Fetcher, response: string): Promise<News[]> {
    return genericRssMapper(fetcher, response)
        .then(news => {
            news.forEach(n => {
                n.sourceId = "youtube";
                n.avatarImageUrls = [
                    `http://img.youtube.com/vi/${n.videoId}/default.jpg`,
                    `http://img.youtube.com/vi/${n.videoId}/mqdefault.jpg`,
                    `http://img.youtube.com/vi/${n.videoId}/hqdefault.jpg`
                ];
                if (n.custom["yt:videoId"]) {
                    n.videoId = n.custom["yt:videoId"];
                }
                if (n.custom["media:group"]) {
                    n.body = n.custom["media:group"]["media:description"][0];
                    const community = n.custom["media:group"]["media:community"][0];
                    const rating = community["media:starRating"][0].$;
                    const stats = community["media:statistics"][0].$;
                    n.stats = {
                        views: stats.views
                    };
                    n.rating = {
                        avg: rating.average,
                        min: rating.min,
                        max: rating.max,
                        count: rating.count
                    };
                }
            });

            return news;
        });
}

export function genericRssMapper(fetcher: Fetcher, response: string): Promise<News[]> {
    const parser = new Parser({customFields: fetcher.customFields});

    return parser.parseString(response)
        .then(meta => {
            const news = [] as News[];

            meta.items.forEach(item => {
                const itemAsNews = {
                    author: item.author,
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
                    sourceName: meta.title,
                    sourceHomeUrl: meta.link,
                    sourceUrls: [],
                    externalUrls: [],
                    videoId: null,
                    quality: 0,
                    custom: {},
                    rating: null,
                    stats: null
                };

                if (meta.image) {
                    itemAsNews.avatarImageUrls.push(meta.image.url);
                }

                if (item.categories && item.categories.length > 0) {
                    itemAsNews.tags = item.categories;
                }

                fetcher.customFields.forEach(customField => {
                    if (item[customField]) {
                        itemAsNews.custom[customField] = item[customField];
                    }
                });

                news.push(itemAsNews);
            });

            return Promise.resolve(news);
        });
}

export function getTagBasedFilter(tags: Set<Tag>) {
    if (tags.has(Tag.filter)) {
        return (news) => isRelevant(news);
    }
    return (() => true);
}

function isRelevant(news: News): boolean {
    if (!news.title) {
        return false;
    }

    const newsTitleKeywords = news.title
        .trim()
        .replace("\\s+", " ")
        .split(" ")
        .map(a => a.toLocaleLowerCase())
        .filter(a => a.length > 2);

    const titleKeywords = new Set<string>(newsTitleKeywords);

    return RELEVANT_KEYWORDS
        .map(a => a.toLocaleLowerCase())
        .filter(a => titleKeywords.has(a))
        .length > 0;
}
