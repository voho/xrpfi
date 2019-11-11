import express, {Request, Response} from "express";
import {getStatus} from "./fetcher/fetcherLoader";
import {scheduleFetcherRefresh} from "./fetcher/fetcherScheduler";
import {getNews} from "./store/newsStorage";
import {logInfo} from "./utils/logger";

const PORT = process.env.HTTP_PORT || 4000;
const app = express();

scheduleFetcherRefresh();

app.get("/api/news", (req: Request, res: Response) => {
    res.json({root: getNews()});
});

app.get("/api/status", (req: Request, res: Response) => {
    res.json({root: getStatus()});
});

app.listen(PORT, () => {
    logInfo(`Server listening at port ${PORT}.`);
});
