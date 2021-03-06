import express, {Request, Response} from "express";
import * as path from "path";
import {PORT} from "../../frontend/src/common/constants";
import {NewsQueryByTags, TagId} from "../../frontend/src/common/model";
import {scheduleFetcherRefresh} from "./fetcher/scheduler";
import {getStatus, getTags} from "./fetcher/status";
import {getNews} from "./store/newsStorage";
import {logInfo} from "./utils/logger";

const app = express();

scheduleFetcherRefresh();

app.get("/api/news", (req: Request, res: Response) => {
    function splitOrNull(value: any): TagId[] | null {
        if (value) {
            return value.toString().split("-");
        }
        return null;
    }

    function getQuery(req: Request): NewsQueryByTags {
        return {
            whitelist: splitOrNull(req.query["whitelist"]),
            blacklist: splitOrNull(req.query["blacklist"])
        };
    }

    res.json({root: getNews(getQuery(req))});
});

app.get("/api/status", (req: Request, res: Response) => {
    res.json({root: getStatus(), tags: getTags()});
});

function getFrontendRootDir() {
    if (__dirname.includes("build")) {
        return path.join(__dirname, "../../../../frontend/build");
    }
    return path.join(__dirname, "../../frontend/build");
}

const frontendBuildRoot = getFrontendRootDir();
logInfo("Backend build root: " + __dirname);
logInfo("Frontend build root: " + frontendBuildRoot);
app.use(express.static(frontendBuildRoot));

app.listen(PORT, () => {
    logInfo(`Server listening at port ${PORT}.`);
});
