import FeedMe from "feedme";
import {News} from "../model/model";

export function twitterRssMapper(response: string): News[] {
    const news = [] as News[];
    const parser = new FeedMe();
    parser.on("item", (item) => {
        news.push({
            url: item.link,
            guid: item.link,
            sourceName: "Twitter",
            sourceHomeUrl: "http://twitter.com",
            sourceId: "twitter",
            hash: 0,
            title: item.title,
            date: item.date ? parseInt(item.date) : 0,
            body: item.description,
            tags: [],
            sourceUrls: [],
            externalUrls: [],
            avatarImageUrls: [],
            oembedUrl: null,
            videoId: null,
            imageUrls: [],
            priority: 0
        });
    });
    parser.write(response);
    return news;
}

export function redditRssMapper(response: string): News[] {
    const news = [] as News[];
    const parser = new FeedMe();
    parser.on("item", (item) => {
        news.push({
            url: item.link,
            guid: item.link,
            sourceName: "Reddit",
            sourceHomeUrl: "https://www.reddit.com/",
            sourceId: "reddit",
            hash: 0,
            title: item.title,
            date: item.date ? parseInt(item.date) : 0,
            body: item.description,
            tags: [],
            sourceUrls: [],
            externalUrls: [],
            avatarImageUrls: [],
            oembedUrl: null,
            videoId: null,
            imageUrls: [],
            priority: 1
        });
    });
    parser.write(response);
    return news;
}

export function genericRssMapper(response: string): News[] {
    const news = [] as News[];
    const parser = new FeedMe();
    // TODO
    let sourceName = "News";
    parser.on("title", (title) => {
        sourceName = title.toString();
    });
    parser.on("item", (item) => {
        news.push({
            url: item.link,
            guid: item.link,
            sourceName: sourceName,
            sourceHomeUrl: ":)",
            sourceId: "news",
            hash: 0,
            title: item.title,
            date: item.date ? parseInt(item.date) : 0,
            body: item.description,
            tags: [],
            sourceUrls: [],
            externalUrls: [],
            avatarImageUrls: [],
            oembedUrl: null,
            videoId: null,
            imageUrls: [],
            priority: 1
        });
    });
    parser.write(response);
    return news;
}
