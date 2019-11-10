import {Fetcher, getRedditFetcher, getTwitterFetcher, Tag} from "./fetcherFactory";

// TODO https://github.com/voho/xrpfi/blob/8f9950730c1424d9c616a455091b6c7033838701/fletcher/src/main/java/fi/xrp/fletcher/model/source/config/NewsSourceConfiguration.java

function getStakeholderTwitterFetcher(alias: string) {
    return getTwitterFetcher(alias, new Set([Tag.official, Tag.social, Tag.twitter]));
}

function getInstitutionTwitterFetcher(alias: string) {
    return getTwitterFetcher(alias, new Set([Tag.official, Tag.social, Tag.twitter, Tag.filter]));
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

export const ALL_FETCHERS: Fetcher[] = [
    ...STAKEHOLDERS_TWITTER,
    ...INSTITUTIONS_TWITTER,
    ...EXCHANGES_TWITTER,
    ...COMMUNITY_TWITTER,
    ...REDDIT_COMMUNITIES
];
