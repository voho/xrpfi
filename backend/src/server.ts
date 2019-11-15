import express, {Request, Response} from "express";
import * as path from "path";
import {scheduleFetcherRefresh} from "./fetcher/scheduler";
import {getStatus, getTags} from "./fetcher/status";
import {getNews} from "./store/newsStorage";
import {PORT} from "./utils/constants";
import {logInfo} from "./utils/logger";

const app = express();

scheduleFetcherRefresh();

app.get("/api/news", (req: Request, res: Response) => {
    res.json({root: getNews()});
});

app.get("/api/status", (req: Request, res: Response) => {
    res.json({root: getStatus(), tags: getTags()});
});

app.use(express.static(path.join(__dirname, "../../frontend/build")));

app.listen(PORT, () => {
    logInfo(`Server listening at port ${PORT}.`);
});
