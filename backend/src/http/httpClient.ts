import request from "superagent";
import {logDebug} from "../utils/logger";

export function httpGet(url) {
    logDebug("Firing GET request: " + url);

    return request
        .get(url)
        .retry(3)
        .timeout({
            deadline: 10000
        });
}
