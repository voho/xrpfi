package fi.xrp.fletcher.service;

import fi.xrp.fletcher.model.api.News;
import fi.xrp.fletcher.model.source.NewsDatabase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public class NewsStorageService implements NewsDatabase {
    private final Duration maxNewsAge;
    private final int maxNewsCount;
    private final Map<String, News> guidToNews = new HashMap<>();

    @Override
    public List<News> getMergedNews() {
        final long now = System.currentTimeMillis();

        return guidToNews
                .values()
                .stream()
                .filter(a -> a.getDate() != null)
                .filter(a -> a.getDate() != 0)
                .filter(a -> now - a.getDate() <= maxNewsAge.toMillis())
                .sorted(Comparator.comparingLong(News::getDate).reversed())
                .limit(maxNewsCount)
                .collect(Collectors.toList());
    }

    @Override
    public void setNews(final List<News> news) {
        log.info("Adding {} news.", news.size());
        guidToNews.clear();
        news.forEach(n -> guidToNews.put(n.getGuid(), n));
    }
}
