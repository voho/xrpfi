import React, {useEffect, useState} from "react";
import {News} from "../../service/model";
import {Button} from "../common/Button";
import {SourcesStatus} from "../meta/SourcesStatus";
import "./NewsDetail.scss";
import {NewsDate, NewsFlags, NewsSource} from "./NewsItem";

interface NewsDetailProps {
    selectedNews?: News
}

function createMarkup(html: string) {
    return {__html: html};
}

const NewsOpen: React.FC<{ news: News }> = (props) => {
    return <Button text={"Open in new window"} onClick={() => window.open(props.news.url)}/>;
};

const NoNewsSelected = () => {
    return <p className={"empty-news"}>No News selected.</p>;
};

const VideoEmbed: React.FC<{ videoId: string }> = (props) => {
    return (
        <div className="video-embed">
            <iframe className="embed-responsive-item"
                    src={"https://www.youtube.com/embed/" + props.videoId + "&amp;feature=share?rel=0"}
                    frameBorder="0"/>
        </div>
    );
};

const OEmbed: React.FC<{ oembedUrl: string }> = (props) => {
    const [html, setHtml] = useState("<p>Loading...</p>");

    useEffect(() => {
        fetch(props.oembedUrl)
            .then(function (response) {
                return response.json();
            })
            .then(function (json) {
                if (json && json.html) {
                    setHtml(json.html);
                }
            })
            .catch(error => {
                setHtml("Error: " + error.message);
            });
    });

    return (
        <div dangerouslySetInnerHTML={createMarkup(html)}/>
    );
};

const NewsSelected: React.FC<NewsDetailProps> = (props) => {
    if (!props.selectedNews) {
        return <SourcesStatus/>;
    }

    const news = props.selectedNews!;

    return (
        <>
            {news.videoId && <VideoEmbed videoId={news.videoId}/>}
            {news.oembedUrl && <OEmbed oembedUrl={news.oembedUrl}/>}
            <h2>{props.selectedNews!.title}</h2>
            <p className={"flags"}>
                <NewsOpen news={news}/>
                <NewsFlags news={news}/>
                <NewsSource news={news}/>
                <NewsDate news={news}/>
            </p>
            <div className={"external"} dangerouslySetInnerHTML={createMarkup(news.body)}/>
        </>
    );
};

export const NewsDetail: React.FC<NewsDetailProps> = (props) => {
    return (
        <div className={"inside"}>
            <NewsSelected selectedNews={props.selectedNews}/>
        </div>
    );
};
