import {Root} from "../model/model";

export function fetchRoot(): Promise<Root> {
    return Promise.resolve({
        news: [
            {title: "Article 1"},
            {title: "Article 2"},
            {title: "Article 3"}
        ]
    });
}
