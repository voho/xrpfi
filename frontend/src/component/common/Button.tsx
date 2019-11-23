import React from "react";
import "./Button.scss";

interface ButtonProps {
    href?: string,
    text: string;
    onClick: () => void
}

export const Button: React.FC<ButtonProps> = (props) => {
    return <a className={"button"} href={props.href ? props.href : "#"} onClick={props.onClick}>{props.text}</a>;
};
