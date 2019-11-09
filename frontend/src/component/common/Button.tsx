import React from "react";
import "./Button.scss";

interface ButtonProps {
    text: string;
    onClick: () => void
}

export const Button: React.FC<ButtonProps> = (props) => {
    return <button onClick={props.onClick}>{props.text}</button>;
};
