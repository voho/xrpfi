import {DIVIDER_SLOWEST} from "../../../frontend/src/common/constants";
import {TagId} from "../../../frontend/src/common/model";
import {Fetcher, FetcherStatus} from "../model/fetcher";
import {genericRssMapper, getTagBasedFilter, redditRssMapper, twitterRssMapper, youtubeRssMapper} from "./mappers";

// TODO https://github.com/voho/xrpfi/blob/8f9950730c1424d9c616a455091b6c7033838701/fletcher/src/main/java/fi/xrp/fletcher/model/source/config/NewsSourceConfiguration.java

const OFFICIAL_YOUTUBE: Fetcher[] = [
    getOfficialYouTubeFetcher("UCjok1uTSBUgvRYQaASz6YWw")
];

const COMMUNITY_YOUTUBE: Fetcher[] = [
    getUnofficialYouTubeFetcher("UCg5GzcNQp5C6STqLP9vNAow"),
    getUnofficialYouTubeFetcher("UC40BZgDu86OLrY1wtRKhOnQ"),
    getUnofficialYouTubeFetcher("UC73iepg_YbH8ZJvwasXIP8Q"),
    getUnofficialYouTubeFetcher("UCtQycmSrKdJ0zE0bWumO4vA"),
    getUnofficialYouTubeFetcher("UC9t0DRLy5_dQEpb8nPKAiQA"),
    getUnofficialYouTubeFetcher("UCGyqEtcGQQtXyUwvcy7Gmyg"),
    getUnofficialYouTubeFetcher("UCmexsZ6pFvmXa9hOnnyRz5A"),
    getUnofficialYouTubeFetcher("UC4nXWTjZqK4bv7feoRntSog"),
    getUnofficialYouTubeFetcher("UCdUSSt-IEUg2eq46rD7lu_g"),
    getUnofficialYouTubeFetcher("UCkpt3vvZ0Y0wvTX2L-lkxsg"),
    getUnofficialYouTubeFetcher("UCEBRZGYDGPDUUDOJ9pX3tPg"),
    getUnofficialYouTubeFetcher("UCavTvSwEoRABvnPtLg0e6LQ"),
    getUnofficialYouTubeFetcher("UCCatR7nWbYrkVXdxXb4cGXw"),
    getUnofficialYouTubeFetcher("UC70Q-2uXkC_5xk9-L5qhm1Q"),
    getUnofficialYouTubeFetcher("UC-5HLi3buMzdxjdTdic3Aig"),
    getUnofficialYouTubeFetcher("UCjpkwsuHgYx9fBE0ojsJ_-w"),
    getUnofficialYouTubeFetcher("UCpwU7S8Y3KeOuShYy9ZZ1JQ"),
    getUnofficialYouTubeFetcher("UCl2oCaw8hdR_kbqyqd2klIA"),
    getUnofficialYouTubeFetcher("UCLnQ34ZBSjy2JQjeRudFEDw"),
    getUnofficialYouTubeFetcher("UCc4Rz_T9Sb1w5rqqo9pL1Og")
];

const STAKEHOLDERS_TWITTER: Fetcher[] = [
    getStakeholderTwitterFetcher("Ripple", 100)
];

const INSTITUTIONS_TWITTER: Fetcher[] = [
    getInstitutionTwitterFetcher("FedPayImprove"),
    getInstitutionTwitterFetcher("IMFNews"),
    getInstitutionTwitterFetcher("DigitalAssets"),
    getInstitutionTwitterFetcher("Bakkt"),
    getInstitutionTwitterFetcher("forbescrypto"),
    getInstitutionTwitterFetcher("PolySignInc"),
    getInstitutionTwitterFetcher("Temenos"),
    getInstitutionTwitterFetcher("crypto"),
    getInstitutionTwitterFetcher("GrayscaleInvest"),
    getInstitutionTwitterFetcher("swiftcommunity"),
    getInstitutionTwitterFetcher("WorldBank"),
    getInstitutionTwitterFetcher("WeissRatings")
];

const COMMUNITY_TWITTER: Fetcher[] = [
    /*getCommunityTwitterFetcher("BankXRP"),
    getCommunityTwitterFetcher("c3_nik"),
    getCommunityTwitterFetcher("hodor7777"),
    getCommunityTwitterFetcher("XrpCenter"),
    getCommunityTwitterFetcher("XrpYoda"),
    getCommunityTwitterFetcher("xrptrump"),
    getCommunityTwitterFetcher("wietsewind"),
    getCommunityTwitterFetcher("stevevargas"),
    getCommunityTwitterFetcher("XrpMr"),
    getCommunityTwitterFetcher("XRPNews_"),
    getCommunityTwitterFetcher("xrpcryptowolf"),
    getCommunityTwitterFetcher("TheCryptHawk"),
    getCommunityTwitterFetcher("lightningsignal"),
    getCommunityTwitterFetcher("Paisan26849860"),
    getCommunityTwitterFetcher("zerpslurp")*/
];

const COMMUNITY_TWITTER_BOTS: Fetcher[] = [
    getCommunityTwitterBotFetcher("whale_alert"),
    getCommunityTwitterBotFetcher("xrpl_monitor"),
    getCommunityTwitterBotFetcher("LiquidityB")
];

const EXCHANGES_TWITTER: Fetcher[] = [
    getExchangeTwitterFetcher("Aurora_dao"),
    getExchangeTwitterFetcher("coinbase"),
    getExchangeTwitterFetcher("CoinbasePro"),
    getExchangeTwitterFetcher("coinfieldEX"),
    getExchangeTwitterFetcher("bitfinex"),
    getExchangeTwitterFetcher("BithumbOfficial"),
    getExchangeTwitterFetcher("BitGo"),
    getExchangeTwitterFetcher("BitMEXdotcom"),
    getExchangeTwitterFetcher("Bitstamp"),
    getExchangeTwitterFetcher("BittrexExchange"),
    getExchangeTwitterFetcher("BitrueOfficial"),
    getExchangeTwitterFetcher("binance"),
    getExchangeTwitterFetcher("cryptaldash"),
    getExchangeTwitterFetcher("DXdotExchange"),
    getExchangeTwitterFetcher("hitbtc"),
    getExchangeTwitterFetcher("HuobiGlobal"),
    getExchangeTwitterFetcher("krakenfx"),
    getExchangeTwitterFetcher("NYSE"),
    getExchangeTwitterFetcher("OKEx"),
    getExchangeTwitterFetcher("Poloniex"),
    getExchangeTwitterFetcher("LCX"),
    getExchangeTwitterFetcher("upbitexchange"),
    getExchangeTwitterFetcher("UpholdInc"),
    getExchangeTwitterFetcher("xrpunited"),
    getExchangeTwitterFetcher("dcexofficial")
];

const REDDIT_COMMUNITIES: Fetcher[] = [
    getXrpCommunityRedditFetcher("Ripple", 50),
    getXrpCommunityRedditFetcher("XRP", 50),
    getXrpCommunityRedditFetcher("RippleTalk", 50),
    getGeneralCommunityRedditFetcher("CryptoCurrency"),
    getGeneralCommunityRedditFetcher("CryptoMarkets"),
    getGeneralCommunityRedditFetcher("Crypto_Currency_News"),
    getGeneralCommunityRedditFetcher("CryptoCurrencyTrading"),
    getGeneralCommunityRedditFetcher("CryptoTechnology")
];

const GOOD_RIPPLE_NEWS: Fetcher[] = [
    getRippleNewsFetcher("http://xrpcommunity.blog/rss/", 80),
    getRippleNewsFetcher("http://ripple.com/category/insights/news/feed/", 100),
    getRippleNewsFetcher("http://thexrpdaily.com/feed/", 10)
];

const GOOD_GENERAL_NEWS: Fetcher[] = [
    getGeneralNewsFetcher("http://decryptmedia.com/feed/"),
    getGeneralNewsFetcher("http://nakamoto.com/rss/", 80),
    getGeneralNewsFetcher("http://www.forbes.com/money/feed2/", 80),
    getGeneralNewsFetcher("http://feeds.finance.yahoo.com/rss/2.0/headline?s=xrp-usd&lang=en-US", 20),
    getGeneralNewsFetcher("http://feeds.finance.yahoo.com/rss/2.0/headline?s=xrp-btc&lang=en-US", 20)
];

const GENERAL_NEWS: Fetcher[] = [
    getGeneralNewsFetcher("http://coindiligent.com/feed"),
    getGeneralNewsFetcher("http://ambcrypto.com/feed/"),
    getGeneralNewsFetcher("http://bitcoinchaser.com/feed/"),
    getGeneralNewsFetcher("http://bitcoinist.com/feed"),
    getGeneralNewsFetcher("http://bitcoinmagazine.com/feed"),
    getGeneralNewsFetcher("http://bitpinas.com/feed/"),
    getGeneralNewsFetcher("http://blog.coinspectator.com/feed"),
    getGeneralNewsFetcher("http://btcmanager.com/feed/"),
    getGeneralNewsFetcher("http://coindoo.com/feed/"),
    getGeneralNewsFetcher("http://coinfunda.com/feed"),
    getGeneralNewsFetcher("http://coinjournal.net/feed/"),
    getGeneralNewsFetcher("http://coinpress.io/feed/"),
    getGeneralNewsFetcher("http://coinspeaker.com/feed"),
    getGeneralNewsFetcher("http://cointelegraph.com/rss"),
    getGeneralNewsFetcher("http://cryptobriefing.com/feed/"),
    getGeneralNewsFetcher("http://cryptocoinspy.com/feed/"),
    getGeneralNewsFetcher("http://cryptocrimson.com/feed/"),
    getGeneralNewsFetcher("http://cryptocurrencyfacts.com/blog/feed/"),
    getGeneralNewsFetcher("http://cryptodaily.co.uk/feed/"),
    getGeneralNewsFetcher("http://cryptoninjas.net/feed"),
    getGeneralNewsFetcher("http://cryptodailygazette.com/feed/"),
    getGeneralNewsFetcher("http://ethereumworldnews.com/feed/"),
    getGeneralNewsFetcher("http://feeds.feedburner.com/CoinDesk"),
    getGeneralNewsFetcher("http://forklog.media/category/news/feed/"),
    getGeneralNewsFetcher("http://icoinblog.com/feed/"),
    getGeneralNewsFetcher("http://masterthecrypto.com/feed/"),
    getGeneralNewsFetcher("http://news.bitcoin.com/feed/"),
    getGeneralNewsFetcher("http://nulltx.com/feed/"),
    getGeneralNewsFetcher("http://unhashed.com/feed/"),
    getGeneralNewsFetcher("http://usethebitcoin.com/feed"),
    getGeneralNewsFetcher("http://www.bitcoinmarketjournal.com/feed/"),
    getGeneralNewsFetcher("http://www.blockchain24.co/feed/"),
    getGeneralNewsFetcher("http://www.ccn.com/feed/"),
    getGeneralNewsFetcher("http://www.coininsider.com/feed/"),
    getGeneralNewsFetcher("http://www.coinspeaker.com/feed/"),
    getGeneralNewsFetcher("http://www.crypto-news.net/feed/"),
    getGeneralNewsFetcher("http://www.cryptocurrencyfreak.com/feed/"),
    getGeneralNewsFetcher("http://www.cryptoglobe.com/latest/feed/"),
    getGeneralNewsFetcher("http://www.cryptoground.com/feeds.xml?format=xml"),
    getGeneralNewsFetcher("http://www.newsbtc.com/feed/"),
    getGeneralNewsFetcher("http://blokt.com/feed"),
    getGeneralNewsFetcher("http://u.today/rss_feed"),
    getGeneralNewsFetcher("http://abacusjournal.com/feed/"),
    getGeneralNewsFetcher("http://coinfomania.com/feed/"),
    getGeneralNewsFetcher("http://ripple-guide.com/category/xrp-news/feed"),
    getGeneralNewsFetcher("http://unchained.libsyn.com/unchained")
];

export const ALL_FETCHERS: Fetcher[] = [
    ...STAKEHOLDERS_TWITTER,
    ...INSTITUTIONS_TWITTER,
    ...EXCHANGES_TWITTER,
    ...COMMUNITY_TWITTER,
    ...COMMUNITY_TWITTER_BOTS,
    ...REDDIT_COMMUNITIES,
    ...GOOD_RIPPLE_NEWS,
    ...GOOD_GENERAL_NEWS,
    ...GENERAL_NEWS,
    ...OFFICIAL_YOUTUBE,
    ...COMMUNITY_YOUTUBE
];


function getStakeholderTwitterFetcher(alias: string, quality = 1) {
    return getTwitterFetcher(alias, ["official", "social", "twitter"], quality);
}

function getInstitutionTwitterFetcher(alias: string, quality = 1) {
    return getTwitterFetcher(alias, ["good", "official", "social", "twitter", "filter"], quality);
}

function getCommunityTwitterFetcher(alias: string, quality = 1) {
    return getTwitterFetcher(alias, ["community", "social", "twitter", "filter"], quality);
}

function getCommunityTwitterBotFetcher(alias: string, quality = 2) {
    return getTwitterFetcher(alias, ["community", "social", "twitter", "bot"], quality);
}

function getExchangeTwitterFetcher(alias: string, quality = 10) {
    return getTwitterFetcher(alias, ["official", "social", "twitter", "filter"], quality);
}

function getXrpCommunityRedditFetcher(alias: string, quality = 2) {
    return getTwitterFetcher(alias, ["community", "social", "reddit"], quality);
}

function getGeneralCommunityRedditFetcher(sub: string, quality = 1) {
    return getRedditFetcher(sub, ["community", "social", "reddit", "filter"], quality);
}

function getRippleNewsFetcher(feedUrl: string, quality = 100) {
    return getNewsFetcher(feedUrl, ["good", "news"], quality);
}

function getGeneralNewsFetcher(feedUrl: string, quality = 5) {
    return getNewsFetcher(feedUrl, ["news", "filter"], quality);
}

function getOfficialYouTubeFetcher(channelId: string, quality = 100) {
    return getYouTubeFetcher(channelId, ["official", "social", "youtube", "good"], quality);
}

function getUnofficialYouTubeFetcher(channelId: string, quality = 5) {
    return getYouTubeFetcher(channelId, ["community", "social", "youtube"], quality);
}


export function getTwitterFetcher(alias: string, tags: TagId[], quality = 1): Fetcher {
    return {
        tags: new Set<TagId>(tags),
        customFields: [],
        title: `@${alias} at twitter`,
        homeUrl: `https://twitter.com/${alias}`,
        fetchUrl: `https://twitrss.me/twitter_user_to_rss/?user=${alias}`,
        updateFrequencyDivider: DIVIDER_SLOWEST,
        status: getInitialStatus(),
        mapper: twitterRssMapper,
        filter: getTagBasedFilter(tags),
        limit: 10,
        quality
    };
}

export function getRedditFetcher(sub: string, tags: TagId[], quality = 2): Fetcher {
    return {
        tags: new Set<TagId>(tags),
        customFields: [],
        title: `/r/${sub}`,
        homeUrl: `https://www.reddit.com/r/${sub}/`,
        fetchUrl: `https://www.reddit.com/r/${sub}/.rss`,
        updateFrequencyDivider: DIVIDER_SLOWEST,
        status: getInitialStatus(),
        mapper: redditRssMapper,
        filter: getTagBasedFilter(tags),
        limit: 20,
        quality
    };
}

export function getNewsFetcher(feedUrl: string, tags: TagId[], quality = 2): Fetcher {
    return {
        tags: new Set<TagId>(tags),
        customFields: [],
        title: `news://${feedUrl}`,
        homeUrl: feedUrl,
        fetchUrl: feedUrl,
        updateFrequencyDivider: DIVIDER_SLOWEST,
        status: getInitialStatus(),
        mapper: genericRssMapper,
        filter: getTagBasedFilter(tags),
        limit: 20,
        quality
    };
}

export function getYouTubeFetcher(channelId: string, tags: TagId[], quality = 2): Fetcher {
    return {
        tags: new Set<TagId>(tags),
        customFields: ["yt:videoId", "media:group"],
        title: `youtube://${channelId}`,
        homeUrl: `http://www.youtube.com/channel/${channelId}`,
        fetchUrl: `http://www.youtube.com/feeds/videos.xml?channel_id=${channelId}`,
        updateFrequencyDivider: DIVIDER_SLOWEST,
        status: getInitialStatus(),
        mapper: youtubeRssMapper,
        filter: getTagBasedFilter(tags),
        limit: 10,
        quality
    };
}

function getInitialStatus(): FetcherStatus {
    return {
        status: "INITIALIZED",
        lastUpdateStartTime: 0,
        lastUpdateEndTime: 0,
        lastErrorTime: 0,
        lastNewsCount: 0,
        lastErrorMessage: null
    };
}
