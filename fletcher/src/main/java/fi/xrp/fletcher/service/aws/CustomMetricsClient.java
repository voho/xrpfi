package fi.xrp.fletcher.service.aws;

import com.amazonaws.services.cloudwatch.AmazonCloudWatch;
import com.amazonaws.services.cloudwatch.model.Dimension;
import com.amazonaws.services.cloudwatch.model.MetricDatum;
import com.amazonaws.services.cloudwatch.model.PutMetricDataRequest;
import com.amazonaws.services.cloudwatch.model.StandardUnit;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CustomMetricsClient {
    private final AmazonCloudWatch cloudWatch;

    public void emitAfterUpdateNewsMetrics(final String tag, final String id, final long time, final long news) {
        final Dimension tagDimension = new Dimension()
                .withName("Tag")
                .withValue(tag);

        final MetricDatum timeDatum = new MetricDatum()
                .withMetricName("time")
                .withUnit(StandardUnit.None)
                .withValue((double) time)
                .withDimensions(tagDimension);

        final MetricDatum countDatum = new MetricDatum()
                .withMetricName("count")
                .withUnit(StandardUnit.None)
                .withValue((double) news)
                .withDimensions(tagDimension);

        final PutMetricDataRequest request = new PutMetricDataRequest()
                .withNamespace("fletcher/news")
                .withMetricData(timeDatum, countDatum);

        cloudWatch.putMetricData(request);
    }
}
