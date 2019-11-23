import {faYoutube} from "@fortawesome/free-brands-svg-icons";
import {faGithub} from "@fortawesome/free-brands-svg-icons/faGithub";
import {faRedditAlien} from "@fortawesome/free-brands-svg-icons/faRedditAlien";
import {faTwitter} from "@fortawesome/free-brands-svg-icons/faTwitter";
import {faVideo} from "@fortawesome/free-solid-svg-icons/faVideo";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import moment from "moment";
import React, {useContext} from "react";
import {News} from "../../../../common/src/model";
import {NewsSelectAction, UseNewsReducerContext} from "../../service/NewsReducer";
import "./NewsItem.scss";

interface NewsItemProps {
    news: News
}

function getFirstUsableImage(news: News): string | undefined {
    const urls: string[] = [];
    (news.avatarImageUrls || []).forEach(imageUrl => urls.push(imageUrl));
    (news.imageUrls || []).forEach(imageUrl => urls.push(imageUrl));
    if (urls.length > 0) {
        return urls[0];
    }
    return undefined;
}

const Image: React.FC<NewsItemProps> = (props) => {
    const imageUrl = getFirstUsableImage(props.news);
    return <img alt={""} src={imageUrl}/>;
};

const Title: React.FC<NewsItemProps> = (props) => {
    return <a onClick={(e) => {e.preventDefault();}} href={props.news.url}>{props.news.title}</a>;
};

export const NewsFlags: React.FC<NewsItemProps> = (props) => {
    const flags = [] as JSX.Element[];

    if (props.news.videoId) {
        flags.push(<FontAwesomeIcon icon={faVideo}/>);
    }

    switch (props.news.sourceId) {
        case "youtube":
            flags.push(<FontAwesomeIcon color={"#ff0000"} icon={faYoutube}/>);
            break;
        case "reddit":
            flags.push(<FontAwesomeIcon color={"#ff4500"} icon={faRedditAlien}/>);
            break;
        case "twitter":
            flags.push(<FontAwesomeIcon color={"#1da1f2"} icon={faTwitter}/>);
            break;
        case "github":
            flags.push(<FontAwesomeIcon icon={faGithub}/>);
            break;
    }

    if (!flags || flags.length < 1) {
        return null;
    }

    return <span>{flags}</span>;
};

export const NewsSource: React.FC<NewsItemProps> = (props) => {
    return <span><a href={props.news.sourceHomeUrl} target={"_blank"}>{props.news.sourceName}</a></span>;
};

export const NewsDate: React.FC<NewsItemProps> = (props) => {
    return <span>{moment(props.news.date).fromNow()}</span>;
};

export const NewsItem: React.FC<NewsItemProps> = (props) => {
    const context = useContext(UseNewsReducerContext);

    const classes = ["news-item"];

    if (context.state.selectedNewsGuid === props.news.guid) {
        classes.push("active");
    }

    return (
        <div className={classes.join(" ")} onClick={() => context.dispatch(({type: "news_select", selected: props.news.guid} as NewsSelectAction))}>
            <div className={"news-item-main"}>
                <p className={"news-item-flags"}>
                    <NewsFlags news={props.news}/>
                    <NewsSource news={props.news}/>
                    <NewsDate news={props.news}/>
                </p>
                <p className={"news-item-title"}>
                    <Title news={props.news}/>
                </p>
            </div>
            <div className={"news-item-image"}>
                <Image news={props.news}/>
            </div>
        </div>
    );
};
