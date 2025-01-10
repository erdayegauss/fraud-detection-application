package com.example.fraud.integration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.services.cloudwatch.CloudWatchClient;
import software.amazon.awssdk.services.cloudwatch.model.*;
import org.springframework.stereotype.Service;

@Service
public class CloudWatchService {
    private static final Logger logger = LoggerFactory.getLogger(CloudWatchService.class);
    private final CloudWatchClient cloudWatchClient;

    public CloudWatchService(CloudWatchClient cloudWatchClient) {
        this.cloudWatchClient = cloudWatchClient;
    }

    public void recordFraudMetric(String transactionId) {
        try {
            MetricDatum datum = MetricDatum.builder()
                .metricName("FraudulentTransaction")
                .unit(StandardUnit.COUNT)
                .value(1.0)
                .dimensions(d -> d.name("TransactionId").value(transactionId))
                .build();

            PutMetricDataRequest request = PutMetricDataRequest.builder()
                .namespace("FraudDetectionMetrics")
                .metricData(datum)
                .build();

            cloudWatchClient.putMetricData(request);
            logger.info("Fraud metric recorded for Transaction ID {}", transactionId);
        } catch (Exception e) {
            logger.error("Failed to record fraud metric for Transaction ID {}", transactionId, e);
        }
    }
}
