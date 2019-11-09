import express, {Request, Response} from "express";
import {ALL_FETCHERS} from "./fetcher/fetcherConfiguration";
import {scheduleFetcher} from "./fetcher/fetcherScheduler";
import {getNews} from "./store/NewsStore";
import {logInfo} from "./utils/logger";

const PORT = process.env.HTTP_PORT || 4000;
const app = express();

ALL_FETCHERS.forEach(fetcher => {
    scheduleFetcher(fetcher);
});

app.get("/api/root", (req: Request, res: Response) => {
    res.json({news: getNews(), meta: []});
});

app.listen(PORT, () => {
    logInfo(`Server listening at port ${PORT}.`);
});
