import request from "superagent";
import {HTTP_REQUEST_RETRY_COUNT, HTTP_REQUEST_TIMEOUT_MS} from "../utils/constants";
import {logDebug} from "../utils/logger";

export function httpGet(url) {
    logDebug("Firing GET request: " + url);

    return request
        .get(url)
        .set("Referer", "http://google.com")
        .set("User-Agent", "xrp.fi")
        .buffer(false)
        .retry(HTTP_REQUEST_RETRY_COUNT)
        .timeout({
            deadline: HTTP_REQUEST_TIMEOUT_MS
        });
}
