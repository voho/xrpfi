import {PORT} from "@xrpfi/common/build/constants";
import {NewsQueryByTags, TagId} from "@xrpfi/common/build/model";
import express, {Request, Response} from "express";
import * as path from "path";
import {scheduleFetcherRefresh} from "./fetcher/scheduler";
import {getStatus, getTags} from "./fetcher/status";
import {getNews} from "./store/newsStorage";
import {logInfo} from "./utils/logger";

const app = express();

scheduleFetcherRefresh();

app.get("/api/news", (req: Request, res: Response) => {
    function splitOrNull(value: any): TagId[] | null {
        if (value) {
            return value.toString().split(",");
        }
        return null;
    }

    function getQuery(req: Request): NewsQueryByTags {
        return {
            whitelist: splitOrNull(req.params["whitelist"]),
            blacklist: splitOrNull(req.params["blacklist"])
        };
    }

    res.json({root: getNews(getQuery(req))});
});

app.get("/api/status", (req: Request, res: Response) => {
    res.json({root: getStatus(), tags: getTags()});
});

app.use(express.static(path.join(__dirname, "../../frontend/build")));

app.listen(PORT, () => {
    logInfo(`Server listening at port ${PORT}.`);
});
