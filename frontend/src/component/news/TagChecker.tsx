import React, {useContext} from "react";
import {KNOWN_TAGS, TagMeta} from "../../common/model";
import {ToggleTagEnabledAction, UseNewsReducerContext} from "../../service/NewsReducer";

const SingleTagChecker: React.FC<{ tag: TagMeta }> = (props) => {
    const context = useContext(UseNewsReducerContext);

    function toggleEnabled(): void {
        context.dispatch({type: "toggle_tag_visible", tag: props.tag.id} as ToggleTagEnabledAction);
    }

    function isEnabled(): boolean {
        return context.state.selectedTagIds.includes(props.tag.id);
    }

    return (
        <div className={"single-tag-checker"}>
            <label>
                <input type={"checkbox"} checked={isEnabled()} onClick={() => toggleEnabled()}/> {props.tag.title}
            </label>
        </div>
    );
};

export const TagChecker: React.FC = () => {
    return (
        <div className={"tag-checker"}>
            {KNOWN_TAGS.filter(tag => tag.customer).map((tag: TagMeta) => {
                return <SingleTagChecker tag={tag}/>;
            })}
        </div>
    );
};
