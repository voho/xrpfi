import request from "superagent";
import {HTTP_REQUEST_RETRY_COUNT, HTTP_REQUEST_TIMEOUT_MS} from "../utils/constants";

export function httpGet(url: string) {
    return request
        .get(url)
        .buffer(true)
        .accept("xml")
        .set("Referer", "http://google.com")
        .set("User-Agent", "Mozilla/5.0")
        .set("Connection", "keep-alive")
        .set("Accept", "*/*")
        .set("Host", new URL(url).hostname)
        .set("Cache-Control", "no-cache")
        .retry(HTTP_REQUEST_RETRY_COUNT)
        .timeout({deadline: HTTP_REQUEST_TIMEOUT_MS});
}
