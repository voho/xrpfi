import express, {Request, Response} from "express";
import {getMeta} from "./fetcher/fetcherLoader";
import {scheduleFetcherRefresh} from "./fetcher/fetcherScheduler";
import {getNews, scheduleNewsCleanupRemoval} from "./store/newsStorage";
import {logInfo} from "./utils/logger";

const PORT = process.env.HTTP_PORT || 4000;
const app = express();

scheduleFetcherRefresh();
scheduleNewsCleanupRemoval();

app.get("/api/root", (req: Request, res: Response) => {
    res.json({news: getNews(), meta: getMeta()});
});

app.listen(PORT, () => {
    logInfo(`Server listening at port ${PORT}.`);
});
