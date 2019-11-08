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

    public void emitAfterUpdateNewsMetrics(final String tag, final String id, final long time, final boolean success) {
        final Dimension tagDimension = new Dimension()
                .withName("Tag")
                .withValue(tag);

        final Dimension idDimension = new Dimension()
                .withName("ID")
                .withValue(id);

        final MetricDatum timeDatum = new MetricDatum()
                .withMetricName("time")
                .withUnit(StandardUnit.Milliseconds)
                .withValue((double) time)
                .withDimensions(tagDimension, idDimension);

        final MetricDatum faultDatum = new MetricDatum()
                .withMetricName("fault")
                .withUnit(StandardUnit.Count)
                .withValue(success ? 0.0 : 1.0)
                .withDimensions(tagDimension, idDimension);

        final PutMetricDataRequest request = new PutMetricDataRequest()
                .withNamespace("fletcher/update")
                .withMetricData(timeDatum, faultDatum);

        cloudWatch.putMetricData(request);
    }
}
