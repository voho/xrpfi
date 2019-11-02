package fi.xrp.fletcher.service;

import fi.xrp.fletcher.model.source.NewsListener;
import fi.xrp.fletcher.model.source.NewsProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;

@Slf4j
@RequiredArgsConstructor
public class NewsStorageService implements NewsListener {
    private final Duration maxNewsAge;
    private final int maxNewsCount;

    @Override
    public void onUpdateStarted(final NewsProducer newsProducer) {

    }

    @Override
    public void onUpdateCompleted(final NewsProducer newsProducer) {

    }

    @Override
    public void onUpdateFailed(final NewsProducer newsProducer, final Throwable error) {

    }
}
