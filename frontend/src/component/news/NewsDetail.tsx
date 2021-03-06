import React, {useEffect, useState} from "react";
import {KNOWN_TAGS, News, TagMeta} from "../../common/model";
import {Button} from "../common/Button";
import {MediaRating} from "../common/MediaRating";
import "./NewsDetail.scss";
import {NewsDate, NewsFlags, NewsSource} from "./NewsItem";

interface NewsDetailProps {
    selectedNews?: News
}

function createMarkup(html: string) {
    return {__html: html};
}

function getTagById(tagId: string): TagMeta | null {
    const tagsFound = KNOWN_TAGS.filter(tag => tag.id === tagId);
    if (tagsFound.length === 1) {
        return tagsFound[0];
    }
    return null;
}


const NewsOpen: React.FC<{ news: News }> = (props) => {
    return <span><Button text={"Open in new window"} onClick={() => window.open(props.news.url)}/></span>;
};

const VideoEmbed: React.FC<{ videoId: string }> = (props) => {
    return (
        <div className="video-embed">
            <iframe className="embed-responsive-item" src={"https://www.youtube.com/embed/" + encodeURIComponent(props.videoId) + "?rel=0"} frameBorder="0"/>
        </div>
    );
};

const OEmbed: React.FC<{ oembedUrl: string }> = (props) => {
    const [html, setHtml] = useState("<p>Loading...</p>");

    useEffect(() => {
        fetch("https://cors-anywhere.herokuapp.com/" + props.oembedUrl)
            .then(function (response) {
                return response.json();
            })
            .then(function (json) {
                if (json && json.html) {
                    setHtml(json.html);
                }
            })
            .then(() => {
                // @ts-ignore
                twttr.widgets.load();
            })
            .catch(error => {
                setHtml("Error: " + error.message);
            });
    });

    return (
        <div dangerouslySetInnerHTML={createMarkup(html)}/>
    );
};

const TagLine: React.FC<NewsDetailProps> = (props) => {
    return (
        <p className={"flags"}>
            <NewsOpen news={props.selectedNews!}/>
            <NewsFlags news={props.selectedNews!}/>
            <NewsSource news={props.selectedNews!}/>
            <NewsDate news={props.selectedNews!}/>
            <NewsRating news={props.selectedNews!}/>
        </p>
    );
};

const FooterLine: React.FC<NewsDetailProps> = (props) => {
    return (
        <p className={"flags"}>
            <NewsTags news={props.selectedNews!}/>
        </p>
    );
};

const NewsRating: React.FC<{ news: News }> = (props) => {
    if (!props.news.rating) {
        return null;
    }
    return (
        <MediaRating
            min={props.news.rating!.min}
            max={props.news.rating!.max}
            avg={props.news.rating!.avg}
            count={props.news.rating!.count}/>
    );
};

const NewsTags: React.FC<{ news: News }> = (props) => {
    if (!props.news.tags) {
        return null;
    }
    return (
        <p>Tags: {props.news.tags.map(getTagById).map(tag => tag ? tag.title : "N/A").join(", ")}</p>
    );
};

const NewsSelected: React.FC<NewsDetailProps> = (props) => {
    if (!props.selectedNews) {
        return <p>Hello! Please select a news.</p>;
    }

    const news = props.selectedNews!;

    if (news.videoId) {
        return (
            <>
                <VideoEmbed videoId={news.videoId}/>
                <TagLine selectedNews={news}/>
                <div className={"external"} dangerouslySetInnerHTML={createMarkup(news.body)}/>
            </>
        );
    }

    if (news.oembedUrl) {
        return <OEmbed oembedUrl={news.oembedUrl}/>;
    }

    return (
        <div className={"inside"}>
            <h2>{news.title}</h2>
            <TagLine selectedNews={news}/>
            <div className={"external"} dangerouslySetInnerHTML={createMarkup(news.body)}/>
            <FooterLine selectedNews={news}/>
        </div>
    );
};

export const NewsDetail: React.FC<NewsDetailProps> = (props) => {
    return (
        <div className={"news-detail"}>
            <NewsSelected selectedNews={props.selectedNews}/>
        </div>
    );
};
