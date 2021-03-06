/* User Experience */

export const MAX_RETURNED_NEWS = 150;
export const OLD_NEWS_CUTOFF_TIME_MS = 14 * 24 * 3600 * 1000;
export const RELEVANT_KEYWORDS = ["ripple", "xrp", "ilp", "dlt", "coil", "interledger"];

/* HTTP */

export const PORT = 8081;
export const HTTP_REQUEST_RETRY_COUNT = 3;
export const HTTP_REQUEST_TIMEOUT_MS = 20000;

/* Scheduling */

export const DIVIDER_FASTEST = 1;
export const DIVIDER_FAST = DIVIDER_FASTEST * 2;
export const DIVIDER_MEDIUM = DIVIDER_FAST * 2;
export const DIVIDER_SLOW = DIVIDER_MEDIUM * 2;
export const DIVIDER_SLOWEST = DIVIDER_SLOW * 2;

export const MAX_QUARTZ = DIVIDER_SLOWEST;
export const QUARTZ_INTERVAL_MS = 10000;

/* Update Intervals */

export const TICKERS_UPDATE_INTERVAL_MS = 10000;
export const NEWS_UPDATE_INTERVAL_MS = 10000;
export const STATUS_UPDATE_INTERVAL_MS = 30000;
