import express, {Request, Response} from "express";
import {getRoot} from "./rootApiHandler";

const PORT = process.env.HTTP_PORT || 4000;
const app = express();

app.get("/api/root", (req: Request, res: Response) => {
    res.json(getRoot());
});

app.listen(PORT, () => {
    console.log(`Server listening at port ${PORT}.`);
});
