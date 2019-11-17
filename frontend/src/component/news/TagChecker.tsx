import React, {useContext} from "react";
import {ToggleTagEnabledAction, UseStatusReducerContext} from "../../service/StatusReducer";

const SingleTagChecker: React.FC<{ tag: string }> = (props) => {
    const context = useContext(UseStatusReducerContext);

    function toggleEnabled(): void {
        context.dispatch({type: "toggle_tag_visible", tag: props.tag} as ToggleTagEnabledAction);
    }

    function isEnabled(): boolean {
        return context.state.selectedTags.includes(props.tag);
    }

    return (
        <div className={"single-tag-checker"}>
            <label>
                <input type={"checkbox"} checked={isEnabled()} onClick={() => toggleEnabled()}/> {props.tag}
            </label>
        </div>
    );
};

export const TagChecker: React.FC = () => {
    const context = useContext(UseStatusReducerContext);

    return (
        <div className={"tag-checker"}>
            {context.state.tags && context.state.tags.map((tag: string) => {
                return <SingleTagChecker tag={tag}/>;
            })}
        </div>
    );
};
