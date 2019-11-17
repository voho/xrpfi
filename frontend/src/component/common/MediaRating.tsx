import {faStar as faStarRegular} from "@fortawesome/free-regular-svg-icons/faStar";
import {faStar as faStarSolid} from "@fortawesome/free-solid-svg-icons/faStar";
import {faStarHalfAlt} from "@fortawesome/free-solid-svg-icons/faStarHalfAlt";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import React from "react";

const NONE = <FontAwesomeIcon icon={faStarRegular}/>;
const HALF = <FontAwesomeIcon icon={faStarHalfAlt}/>;
const FULL = <FontAwesomeIcon icon={faStarSolid}/>;

interface MediaRatingProps {
    min: number,
    max: number,
    avg: number,
    count: number
}

function normalize(min: number, max: number, value: number) {
    return (value - min) / (max - min);
}

function starts(normalized: number) {
    if (normalized < 0.5) {
        return <span>{NONE}{NONE}{NONE}{NONE}{NONE}</span>;
    } else if (normalized < 1) {
        return <span>{HALF}{NONE}{NONE}{NONE}{NONE}</span>;
    } else if (normalized < 1.5) {
        return <span>{FULL}{NONE}{NONE}{NONE}{NONE}</span>;
    } else if (normalized < 2) {
        return <span>{FULL}{HALF}{NONE}{NONE}{NONE}</span>;
    } else if (normalized < 2.5) {
        return <span>{FULL}{FULL}{NONE}{NONE}{NONE}</span>;
    } else if (normalized < 3) {
        return <span>{FULL}{FULL}{HALF}{NONE}{NONE}</span>;
    } else if (normalized < 3.5) {
        return <span>{FULL}{FULL}{FULL}{NONE}{NONE}</span>;
    } else if (normalized < 4) {
        return <span>{FULL}{FULL}{FULL}{HALF}{NONE}</span>;
    } else if (normalized < 4.5) {
        return <span>{FULL}{FULL}{FULL}{FULL}{NONE}</span>;
    } else if (normalized < 5) {
        return <span>{FULL}{FULL}{FULL}{FULL}{HALF}</span>;
    } else {
        return <span>{FULL}{FULL}{FULL}{FULL}{FULL}</span>;
    }
}

export const MediaRating: React.FC<MediaRatingProps> = (props) => {
    const normalized = normalize(props.min, props.max, props.avg);

    return <span className={"media-rating"}>{starts(normalized)} rated {props.count}x</span>;
};
