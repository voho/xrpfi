import React, {useState} from "react";

export const TagChecker: React.FC<{ tag: string, initialState: boolean }> = (props) => {
    const [enabled, setEnabled] = useState(props.initialState);

    return (
        <div className={"tag-checker"}>
            <label>
                <input type={"check"} checked={enabled} onClick={() => setEnabled(!enabled)}/> {props.tag}
            </label>
        </div>
    );
};
