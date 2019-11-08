package fi.xrp.fletcher.model.source;


import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static fi.xrp.fletcher.model.source.NewsProducer.Tag.*;

public class NewsSourceConfiguration {
    private final List<NewsProducer> sources = validate(Stream.of(
            topXrpNews(),
            topGeneralNews(),
            regularNews(),
            github(),
            xrpReddit(),
            communityReddit(),
            twitterExchanges(),
            twitterBots(),
            twitterCommunity(),
            twitterEmployees(),
            twitterStakeholders(),
            youtubeOfficial(),
            youtubeXrpCommunity(),
            youtubeCryptoCommunity(),
            trading()
    )
                                                              .flatMap(Function.identity())
                                                              .collect(Collectors.toList()));

    private List<NewsProducer> validate(final List<NewsProducer> sources) {
        validateDuplicates(sources, NewsProducer::getFeedUrl);
        validateDuplicates(sources, NewsProducer::getHomeUrl);
        return sources;
    }

    private void validateDuplicates(final List<NewsProducer> sources, final Function<NewsProducer, String> extractor) {
        final Set<String> set = new HashSet<>();

        sources.stream().map(extractor).forEach(value -> {
            if (set.contains(value)) {
                throw new IllegalStateException("Duplicate detected: " + value);
            } else {
                set.add(value);
            }
        });
    }

    private Stream<YouTubeRssNewsProducer> youtubeXrpCommunity() {
        return Stream.of(
                YouTubeRssNewsProducer.builder().channelId("UCg5GzcNQp5C6STqLP9vNAow").channelName("Alex Cobb"),
                YouTubeRssNewsProducer.builder().channelId("UCMLdiJ1we2qEQ2c-AspIYkg").channelName("XRP Anonymous"),
                YouTubeRssNewsProducer.builder().channelId("UCeB21F-fFdZYHQjNGRfy44g").channelName("DM Logic"),
                YouTubeRssNewsProducer.builder().channelId("UCtQycmSrKdJ0zE0bWumO4vA").channelName("Digital Asset Investor"),
                YouTubeRssNewsProducer.builder().channelId("UC9t0DRLy5_dQEpb8nPKAiQA").channelName("Jungle Inc"),
                YouTubeRssNewsProducer.builder().channelId("UCBUdJDd401Klt4vy1u07f9g").channelName("xrp coin crypto news"),
                YouTubeRssNewsProducer.builder().channelId("UCsJw8h9p26lU7Nea2eHdOsw").channelName("To The Lifeboats"),
                YouTubeRssNewsProducer.builder().channelId("UCJ7e5K5v1iQxUwMDiqZp5jg").channelName("Crypto Bear"),
                YouTubeRssNewsProducer.builder().channelId("UC73iepg_YbH8ZJvwasXIP8Q").channelName("C3|Nik"),
                YouTubeRssNewsProducer.builder().channelId("UC40BZgDu86OLrY1wtRKhOnQ").channelName("Ping Boy 87"),
                YouTubeRssNewsProducer.builder().channelId("UCnJjRjmthxPCoQaAL44tR6g").channelName("Alessio Rastani")
        )
                     .peek(a -> a.tag(MEDIA))
                     .peek(a -> a.tag(UNOFFICIAL))
                     .map(YouTubeRssNewsProducer.YouTubeRssNewsProducerBuilder::build);
    }

    private Stream<YouTubeRssNewsProducer> youtubeCryptoCommunity() {
        return Stream.of(
                YouTubeRssNewsProducer.builder().channelId("UCGyqEtcGQQtXyUwvcy7Gmyg").channelName("AltCoin Buzz"),
                YouTubeRssNewsProducer.builder().channelId("UCmexsZ6pFvmXa9hOnnyRz5A").channelName("CKJ Crypto News"),
                YouTubeRssNewsProducer.builder().channelId("UC4nXWTjZqK4bv7feoRntSog").channelName("Coin Mastery"),
                YouTubeRssNewsProducer.builder().channelId("UCdUSSt-IEUg2eq46rD7lu_g").channelName("Crypt0"),
                YouTubeRssNewsProducer.builder().channelId("UCkpt3vvZ0Y0wvTX2L-lkxsg").channelName("Crypto Coin News"),
                YouTubeRssNewsProducer.builder().channelId("UCEBRZGYDGPDUUDOJ9pX3tPg").channelName("Crypto Oracle"),
                YouTubeRssNewsProducer.builder().channelId("UCavTvSwEoRABvnPtLg0e6LQ").channelName("Crypto Tips"),
                YouTubeRssNewsProducer.builder().channelId("UCCatR7nWbYrkVXdxXb4cGXw").channelName("DataDash"),
                YouTubeRssNewsProducer.builder().channelId("UC70Q-2uXkC_5xk9-L5qhm1Q").channelName("Esoteric Trading Solutions"),
                YouTubeRssNewsProducer.builder().channelId("UC-5HLi3buMzdxjdTdic3Aig").channelName("The Modern Investor"),
                YouTubeRssNewsProducer.builder().channelId("UCjpkwsuHgYx9fBE0ojsJ_-w").channelName("Thinking Crypto"),
                YouTubeRssNewsProducer.builder().channelId("UCpwU7S8Y3KeOuShYy9ZZ1JQ").channelName("Working Money Channel"),
                YouTubeRssNewsProducer.builder().channelId("UCl2oCaw8hdR_kbqyqd2klIA").channelName("The Crypto Lark"),
                YouTubeRssNewsProducer.builder().channelId("UCLnQ34ZBSjy2JQjeRudFEDw").channelName("The Cryptoverse"),
                YouTubeRssNewsProducer.builder().channelId("UCc4Rz_T9Sb1w5rqqo9pL1Og").channelName("The Moon")
        )
                     .peek(a -> a.tag(UNOFFICIAL))
                     .peek(a -> a.tag(MEDIA))
                     .peek(a -> a.tag(NEEDS_FILTERING))
                     .map(YouTubeRssNewsProducer.YouTubeRssNewsProducerBuilder::build);
    }

    private Stream<YouTubeRssNewsProducer> youtubeOfficial() {
        return Stream.of(
                YouTubeRssNewsProducer.builder().channelId("UCjok1uTSBUgvRYQaASz6YWw").channelName("Ripple")
        )
                     .peek(a -> a.tag(OFFICIAL))
                     .peek(a -> a.tag(MEDIA))
                     .map(YouTubeRssNewsProducer.YouTubeRssNewsProducerBuilder::build);
    }

    private Stream<TwitterRssNewsProducer> twitterEmployees() {
        return Stream.of(
                TwitterRssNewsProducer.builder().alias("bgarlinghouse").position("CEO"),
                TwitterRssNewsProducer.builder().alias("chrislarsensf").position("Co-founder, Executive Chairman"),
                TwitterRssNewsProducer.builder().alias("CoryTV").position("Chief Market Strategist"),
                TwitterRssNewsProducer.builder().alias("RyanZagone").position("Director of Regulatory Relations"),
                TwitterRssNewsProducer.builder().alias("JoelKatz").position("CTO"),
                TwitterRssNewsProducer.builder().alias("nbougalis").position("C++ Lead"),
                TwitterRssNewsProducer.builder().alias("ericvanm").position("SVP of Business Operations"),
                TwitterRssNewsProducer.builder().alias("marcus_treacher").position("Global Head of Strategic Accounts"),
                TwitterRssNewsProducer.builder().alias("kahinavandyke").position("SVP of Business and Corporate Development"),
                TwitterRssNewsProducer.builder().alias("ashgoblue").position("SVP of Product"),
                TwitterRssNewsProducer.builder().alias("sagarsarbhai").position("Government & Regulatory Relations"),
                TwitterRssNewsProducer.builder().alias("danmorgan1").position("Head of EU Regulatory Relations")
        )
                     .peek(a -> a.company("Ripple"))
                     .peek(a -> a.tag(OFFICIAL))
                     .peek(a -> a.tag(SOCIAL))
                     .peek(a -> a.tag(MICROBLOG))
                     .map(TwitterRssNewsProducer.TwitterRssNewsProducerBuilder::build);
    }

    private Stream<TwitterRssNewsProducer> twitterStakeholders() {
        return Stream.of(
                TwitterRssNewsProducer.builder().alias("FedPayImprove"),
                TwitterRssNewsProducer.builder().alias("IMFNews"),
                TwitterRssNewsProducer.builder().alias("DigitalAssets"),
                TwitterRssNewsProducer.builder().alias("Bakkt"),
                TwitterRssNewsProducer.builder().alias("forbescrypto"),
                TwitterRssNewsProducer.builder().alias("PolySignInc"),
                TwitterRssNewsProducer.builder().alias("Temenos"),
                TwitterRssNewsProducer.builder().alias("crypto"),
                TwitterRssNewsProducer.builder().alias("swiftcommunity"),
                TwitterRssNewsProducer.builder().alias("WorldBank"),
                TwitterRssNewsProducer.builder().alias("WeissRatings")
        )
                     .peek(a -> a.tag(OFFICIAL))
                     .peek(a -> a.tag(FINANCIAL))
                     .peek(a -> a.tag(SOCIAL))
                     .peek(a -> a.tag(MICROBLOG))
                     .peek(a -> a.tag(NEEDS_FILTERING))
                     .map(TwitterRssNewsProducer.TwitterRssNewsProducerBuilder::build);
    }

    private Stream<TwitterRssNewsProducer> twitterBots() {
        return Stream.of(
                TwitterRssNewsProducer.builder().alias("xrpl_monitor")
        )
                     .peek(a -> a.tag(UNOFFICIAL))
                     .peek(a -> a.tag(SOCIAL))
                     .peek(a -> a.tag(MICROBLOG))
                     .map(TwitterRssNewsProducer.TwitterRssNewsProducerBuilder::build);
    }

    private Stream<TwitterRssNewsProducer> twitterCommunity() {
        return Stream.of(
                TwitterRssNewsProducer.builder().alias("BankXRP"),
                TwitterRssNewsProducer.builder().alias("c3_nik"),
                TwitterRssNewsProducer.builder().alias("hodor7777"),
                TwitterRssNewsProducer.builder().alias("XrpCenter"),
                TwitterRssNewsProducer.builder().alias("XrpYoda"),
                TwitterRssNewsProducer.builder().alias("xrptrump"),
                TwitterRssNewsProducer.builder().alias("xrp_news"),
                TwitterRssNewsProducer.builder().alias("XRPNews_"),
                TwitterRssNewsProducer.builder().alias("wietsewind"),
                TwitterRssNewsProducer.builder().alias("stevevargas"),
                TwitterRssNewsProducer.builder().alias("XrpMr"),
                TwitterRssNewsProducer.builder().alias("xrpcryptowolf"),
                TwitterRssNewsProducer.builder().alias("whale_alert"),
                TwitterRssNewsProducer.builder().alias("lightningsignal"),
                TwitterRssNewsProducer.builder().alias("Paisan26849860"),
                TwitterRssNewsProducer.builder().alias("zerpslurp")
        )
                     .peek(a -> a.tag(UNOFFICIAL))
                     .peek(a -> a.tag(SOCIAL))
                     .peek(a -> a.tag(MICROBLOG))
                     .peek(a -> a.tag(NEEDS_FILTERING))
                     .map(TwitterRssNewsProducer.TwitterRssNewsProducerBuilder::build);
    }

    private Stream<TwitterRssNewsProducer> twitterExchanges() {
        return Stream.of(
                TwitterRssNewsProducer.builder().alias("Aurora_dao"),
                TwitterRssNewsProducer.builder().alias("coinbase"),
                TwitterRssNewsProducer.builder().alias("CoinbasePro"),
                TwitterRssNewsProducer.builder().alias("coinfieldEX"),
                TwitterRssNewsProducer.builder().alias("bitfinex"),
                TwitterRssNewsProducer.builder().alias("BithumbOfficial"),
                TwitterRssNewsProducer.builder().alias("BitGo"),
                TwitterRssNewsProducer.builder().alias("BitMEXdotcom"),
                TwitterRssNewsProducer.builder().alias("Bitstamp"),
                TwitterRssNewsProducer.builder().alias("BittrexExchange"),
                TwitterRssNewsProducer.builder().alias("binance"),
                TwitterRssNewsProducer.builder().alias("cryptaldash"),
                TwitterRssNewsProducer.builder().alias("DXdotExchange"),
                TwitterRssNewsProducer.builder().alias("hitbtc"),
                TwitterRssNewsProducer.builder().alias("HuobiGlobal"),
                TwitterRssNewsProducer.builder().alias("krakenfx"),
                TwitterRssNewsProducer.builder().alias("NYSE"),
                TwitterRssNewsProducer.builder().alias("OKEx_"),
                TwitterRssNewsProducer.builder().alias("Poloniex"),
                TwitterRssNewsProducer.builder().alias("LCX"),
                TwitterRssNewsProducer.builder().alias("Ripple"),
                TwitterRssNewsProducer.builder().alias("upbitexchange"),
                TwitterRssNewsProducer.builder().alias("UpholdInc"),
                TwitterRssNewsProducer.builder().alias("xrpunited"),
                TwitterRssNewsProducer.builder().alias("dcexofficial")
        )
                     .peek(a -> a.tag(OFFICIAL))
                     .peek(a -> a.tag(EXCHANGE))
                     .peek(a -> a.tag(SOCIAL))
                     .peek(a -> a.tag(MICROBLOG))
                     .peek(a -> a.tag(NEEDS_FILTERING))
                     .map(TwitterRssNewsProducer.TwitterRssNewsProducerBuilder::build);
    }

    private Stream<RedditRssNewsProducer> xrpReddit() {
        return Stream.of(
                RedditRssNewsProducer.builder().sub("Ripple"),
                RedditRssNewsProducer.builder().sub("XRP")
        )
                     .peek(a -> a.tag(UNOFFICIAL))
                     .peek(a -> a.tag(SOCIAL))
                     .map(RedditRssNewsProducer.RedditRssNewsProducerBuilder::build);
    }

    private Stream<RedditRssNewsProducer> communityReddit() {
        return Stream.of(
                RedditRssNewsProducer.builder().sub("CryptoCurrency"),
                RedditRssNewsProducer.builder().sub("CryptoMarkets"),
                RedditRssNewsProducer.builder().sub("Crypto_Currency_News"),
                RedditRssNewsProducer.builder().sub("CryptoCurrencyTrading"),
                RedditRssNewsProducer.builder().sub("CryptoTechnology")
        )
                     .peek(a -> a.tag(UNOFFICIAL))
                     .peek(a -> a.tag(SOCIAL))
                     .peek(a -> a.tag(NEEDS_FILTERING))
                     .map(RedditRssNewsProducer.RedditRssNewsProducerBuilder::build);
    }

    private Stream<GitHubRssNewsProducer> github() {
        return Stream.of(
                GitHubRssNewsProducer.builder().author("ripple").project("rippled").branch("develop"),
                GitHubRssNewsProducer.builder().author("ripple").project("rippled").branch("master"),
                GitHubRssNewsProducer.builder().author("codius").project("codiusd").branch("master")
        )
                     .peek(a -> a.tag(OFFICIAL))
                     .peek(a -> a.tag(SOFTWARE))
                     .peek(a -> a.tag(DO_NOT_DELETE))
                     .map(GitHubRssNewsProducer.GitHubRssNewsProducerBuilder::build);
    }

    private Stream<TradingViewRssNewsProducer> trading() {
        return Stream.of(
                TradingViewRssNewsProducer.builder().sort("agreed").time("day").symbol("xrpusd"),
                TradingViewRssNewsProducer.builder().sort("agreed").time("day").symbol("xrpbtc")
        )
                     .peek(a -> a.tag(UNOFFICIAL))
                     .peek(a -> a.tag(SOCIAL))
                     .peek(a -> a.tag(TRADING))
                     .peek(a -> a.tag(DO_NOT_CLEANUP))
                     .map(TradingViewRssNewsProducer.TradingViewRssNewsProducerBuilder::build);
    }

    private Stream<GenericRssNewsProducer> topXrpNews() {
        return Stream.of(
                GenericRssNewsProducer.builder().feedUrl("https://xrpcommunity.blog/rss/"),
                GenericRssNewsProducer.builder().feedUrl("https://ripple.com/category/insights/news/feed/"),
                GenericRssNewsProducer.builder().feedUrl("https://thexrpdaily.com/index.php/feed/")
        )
                     .peek(a -> a.tag(NEWS))
                     .peek(a -> a.tag(ALWAYS_IMPORTANT))
                     .map(GenericRssNewsProducer.GenericRssNewsProducerBuilder::build);
    }

    private Stream<GenericRssNewsProducer> topGeneralNews() {
        return Stream.of(
                GenericRssNewsProducer.builder().feedUrl("http://decryptmedia.com/feed/"),
                GenericRssNewsProducer.builder().feedUrl("http://www.forbes.com/money/feed2/")
        )
                     .peek(a -> a.tag(NEWS))
                     .peek(a -> a.tag(ALWAYS_IMPORTANT))
                     .peek(a -> a.tag(NEEDS_FILTERING))
                     .map(GenericRssNewsProducer.GenericRssNewsProducerBuilder::build);
    }

    private Stream<GenericRssNewsProducer> regularNews() {
        return Stream.of(
                GenericRssNewsProducer.builder().feedUrl("https://ambcrypto.com/feed/"),
                GenericRssNewsProducer.builder().feedUrl("https://bitcoinist.com/feed"),
                GenericRssNewsProducer.builder().feedUrl("https://bitcoinmagazine.com/feed"),
                GenericRssNewsProducer.builder().feedUrl("https://blog.coinspectator.com/feed"),
                GenericRssNewsProducer.builder().feedUrl("https://btcmanager.com/feed/"),
                GenericRssNewsProducer.builder().feedUrl("https://coindoo.com/feed/"),
                GenericRssNewsProducer.builder().feedUrl("https://coinfunda.com/feed"),
                GenericRssNewsProducer.builder().feedUrl("https://coinpress.io/feed/"),
                GenericRssNewsProducer.builder().feedUrl("https://coinspeaker.com/feed"),
                GenericRssNewsProducer.builder().feedUrl("https://cointelegraph.com/rss"),
                GenericRssNewsProducer.builder().feedUrl("https://cryptobriefing.com/feed/"),
                GenericRssNewsProducer.builder().feedUrl("https://cryptocoinspy.com/feed/"),
                GenericRssNewsProducer.builder().feedUrl("https://cryptocrimson.com/feed/"),
                GenericRssNewsProducer.builder().feedUrl("https://cryptocurrencyfacts.com/blog/feed/"),
                GenericRssNewsProducer.builder().feedUrl("https://cryptocurrencynews.com/feed/"),
                GenericRssNewsProducer.builder().feedUrl("https://cryptodaily.co.uk/feed/"),
                GenericRssNewsProducer.builder().feedUrl("https://cryptoinsider.com/feed/"),
                GenericRssNewsProducer.builder().feedUrl("https://cryptoninjas.net/feed"),
                GenericRssNewsProducer.builder().feedUrl("https://cryptodailygazette.com/feed/"),
                GenericRssNewsProducer.builder().feedUrl("https://ethereumworldnews.com/feed/"),
                GenericRssNewsProducer.builder().feedUrl("https://feeds.feedburner.com/CoinDesk"),
                GenericRssNewsProducer.builder().feedUrl("https://forklog.net/feed"),
                GenericRssNewsProducer.builder().feedUrl("https://icoinblog.com/feed/"),
                GenericRssNewsProducer.builder().feedUrl("https://news.bitcoin.com/feed/"),
                GenericRssNewsProducer.builder().feedUrl("https://nulltx.com/feed/"),
                GenericRssNewsProducer.builder().feedUrl("https://unhashed.com/feed/"),
                GenericRssNewsProducer.builder().feedUrl("https://usethebitcoin.com/feed"),
                GenericRssNewsProducer.builder().feedUrl("https://www.bitcoinmarketjournal.com/feed/"),
                GenericRssNewsProducer.builder().feedUrl("https://www.blockchain24.co/feed/"),
                GenericRssNewsProducer.builder().feedUrl("https://www.ccn.com/feed/"),
                GenericRssNewsProducer.builder().feedUrl("https://www.coininsider.com/feed/"),
                GenericRssNewsProducer.builder().feedUrl("https://www.coinspeaker.com/feed/"),
                GenericRssNewsProducer.builder().feedUrl("https://www.crypto-news.net/feed/"),
                GenericRssNewsProducer.builder().feedUrl("https://www.cryptocurrencyfreak.com/feed/"),
                GenericRssNewsProducer.builder().feedUrl("https://www.cryptoglobe.com/latest/feed/"),
                GenericRssNewsProducer.builder().feedUrl("https://www.cryptoground.com/feeds.xml?format=xml"),
                GenericRssNewsProducer.builder().feedUrl("https://www.newsbtc.com/feed/"),
                GenericRssNewsProducer.builder().feedUrl("https://blokt.com/feed"),
                GenericRssNewsProducer.builder().feedUrl("https://u.today/rss_feed"),
                GenericRssNewsProducer.builder().feedUrl("https://abacusjournal.com/feed/"),
                GenericRssNewsProducer.builder().feedUrl("https://coinfomania.com/feed/"),
                GenericRssNewsProducer.builder().feedUrl("https://ripple-guide.com/category/xrp-news/feed"),
                GenericRssNewsProducer.builder().feedUrl("https://unchained.libsyn.com/unchained"),
                GenericRssNewsProducer.builder().feedUrl("https://feeds.finance.yahoo.com/rss/2.0/headline?s=xrp-usd&lang=en-US"),
                GenericRssNewsProducer.builder().feedUrl("https://feeds.finance.yahoo.com/rss/2.0/headline?s=xrp-btc&lang=en-US")
        )
                     .peek(a -> a.tag(NEWS))
                     .peek(a -> a.tag(NEEDS_FILTERING))
                     .map(GenericRssNewsProducer.GenericRssNewsProducerBuilder::build);
    }

    public List<NewsProducer> getSources() {
        return sources;
    }
}
