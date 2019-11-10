import {Fetcher, getNewsFetcher, getRedditFetcher, getTwitterFetcher, Tag} from "./fetcherFactory";

// TODO https://github.com/voho/xrpfi/blob/8f9950730c1424d9c616a455091b6c7033838701/fletcher/src/main/java/fi/xrp/fletcher/model/source/config/NewsSourceConfiguration.java

function getStakeholderTwitterFetcher(alias: string) {
    return getTwitterFetcher(alias, new Set([Tag.official, Tag.social, Tag.twitter]));
}

function getInstitutionTwitterFetcher(alias: string) {
    return getTwitterFetcher(alias, new Set([Tag.good, Tag.official, Tag.social, Tag.twitter, Tag.filter]));
}

function getCommunityTwitterFetcher(alias: string) {
    return getTwitterFetcher(alias, new Set([Tag.community, Tag.social, Tag.twitter, Tag.filter]));
}

function getExchangeTwitterFetcher(alias: string) {
    return getTwitterFetcher(alias, new Set([Tag.official, Tag.social, Tag.twitter, Tag.filter]));
}

function getXrpCommunityRedditFetcher(alias: string) {
    return getTwitterFetcher(alias, new Set([Tag.community, Tag.social, Tag.reddit]));
}

function getGeneralCommunityRedditFetcher(sub: string) {
    return getRedditFetcher(sub, new Set([Tag.community, Tag.social, Tag.reddit, Tag.filter]));
}

function getRippleNewsFetcher(feedUrl: string) {
    return getNewsFetcher(feedUrl, new Set([Tag.good, Tag.news]));
}

function getGeneralNewsFetcher(feedUrl: string) {
    return getNewsFetcher(feedUrl, new Set([Tag.news, Tag.filter]));
}

const STAKEHOLDERS_TWITTER: Fetcher[] = [
    getStakeholderTwitterFetcher("Ripple")
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
    getInstitutionTwitterFetcher("swiftcommunity"),
    getInstitutionTwitterFetcher("WorldBank"),
    getInstitutionTwitterFetcher("WeissRatings")
];

const COMMUNITY_TWITTER: Fetcher[] = [
    getCommunityTwitterFetcher("BankXRP"),
    getCommunityTwitterFetcher("c3_nik"),
    getCommunityTwitterFetcher("hodor7777"),
    getCommunityTwitterFetcher("XrpCenter"),
    getCommunityTwitterFetcher("XrpYoda"),
    getCommunityTwitterFetcher("xrptrump"),
    getCommunityTwitterFetcher("wietsewind"),
    getCommunityTwitterFetcher("stevevargas"),
    getCommunityTwitterFetcher("XrpMr"),
    getCommunityTwitterFetcher("xrpcryptowolf"),
    getCommunityTwitterFetcher("whale_alert"),
    getCommunityTwitterFetcher("lightningsignal"),
    getCommunityTwitterFetcher("Paisan26849860"),
    getCommunityTwitterFetcher("zerpslurp")
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
    getExchangeTwitterFetcher("binance"),
    getExchangeTwitterFetcher("cryptaldash"),
    getExchangeTwitterFetcher("DXdotExchange"),
    getExchangeTwitterFetcher("hitbtc"),
    getExchangeTwitterFetcher("HuobiGlobal"),
    getExchangeTwitterFetcher("krakenfx"),
    getExchangeTwitterFetcher("NYSE"),
    getExchangeTwitterFetcher("OKEX"),
    getExchangeTwitterFetcher("Poloniex"),
    getExchangeTwitterFetcher("LCX"),
    getExchangeTwitterFetcher("Ripple"),
    getExchangeTwitterFetcher("upbitexchange"),
    getExchangeTwitterFetcher("UpholdInc"),
    getExchangeTwitterFetcher("xrpunited"),
    getExchangeTwitterFetcher("dcexofficial")
];

const REDDIT_COMMUNITIES: Fetcher[] = [
    getXrpCommunityRedditFetcher("Ripple"),
    getXrpCommunityRedditFetcher("XRP"),
    getGeneralCommunityRedditFetcher("CryptoCurrency"),
    getGeneralCommunityRedditFetcher("CryptoMarkets"),
    getGeneralCommunityRedditFetcher("Crypto_Currency_News"),
    getGeneralCommunityRedditFetcher("CryptoCurrencyTrading"),
    getGeneralCommunityRedditFetcher("CryptoTechnology")
];

const GOOD_RIPPLE_NEWS: Fetcher[] = [
    getRippleNewsFetcher("http://xrpcommunity.blog/rss/"),
    getRippleNewsFetcher("http://ripple.com/category/insights/news/feed/"),
    getRippleNewsFetcher("http://thexrpdaily.com/index.php/feed/")
];

const GOOD_GENERAL_NEWS: Fetcher[] = [
    getGeneralNewsFetcher("http://decryptmedia.com/feed/"),
    getGeneralNewsFetcher("http://www.forbes.com/money/feed2/"),
    getGeneralNewsFetcher("http://feeds.finance.yahoo.com/rss/2.0/headline?s=xrp-usd&lang=en-US"),
    getGeneralNewsFetcher("http://feeds.finance.yahoo.com/rss/2.0/headline?s=xrp-btc&lang=en-US")
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
    getGeneralNewsFetcher("http://cryptocurrencynews.com/feed/"),
    getGeneralNewsFetcher("http://cryptodaily.co.uk/feed/"),
    getGeneralNewsFetcher("http://cryptoinsider.com/feed/"),
    getGeneralNewsFetcher("http://cryptoninjas.net/feed"),
    getGeneralNewsFetcher("http://cryptodailygazette.com/feed/"),
    getGeneralNewsFetcher("http://ethereumworldnews.com/feed/"),
    getGeneralNewsFetcher("http://feeds.feedburner.com/CoinDesk"),
    getGeneralNewsFetcher("http://forklog.net/feed"),
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
    ...REDDIT_COMMUNITIES,
    ...GOOD_RIPPLE_NEWS,
    ...GOOD_GENERAL_NEWS,
    ...GENERAL_NEWS
];
