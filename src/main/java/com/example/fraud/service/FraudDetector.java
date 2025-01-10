package com.example.fraud.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.cloudwatch.CloudWatchClient;
import software.amazon.awssdk.services.cloudwatch.model.*;

import com.example.fraud.model.Transaction;

import java.util.List;

@Service
public class FraudDetector {
    private static final Logger logger = LoggerFactory.getLogger(FraudDetector.class);
    private final CloudWatchClient cloudWatchClient;
    private static final double MAX_AMOUNT_THRESHOLD = 10000.0;
    private static final List<String> SUSPICIOUS_ACCOUNTS = List.of("12345", "67890");

    public FraudDetector(CloudWatchClient cloudWatchClient) {
        this.cloudWatchClient = cloudWatchClient;
    }

    public boolean isFraudulent(Transaction transaction) {
        boolean isFraud = false;

        if (transaction.getAmount() > MAX_AMOUNT_THRESHOLD) {
            logFraud(transaction, "Transaction exceeds maximum amount threshold.");
            isFraud = true;
        }

        if (SUSPICIOUS_ACCOUNTS.contains(transaction.getSenderAccount())) {
            logFraud(transaction, "Transaction involves a suspicious account.");
            isFraud = true;
        }

        return isFraud;
    }

    private void logFraud(Transaction transaction, String reason) {
        logger.warn("Fraud detected for Transaction ID {}: {}", transaction.getTransactionId(), reason);
        recordFraudMetric(transaction);
    }

    private void recordFraudMetric(Transaction transaction) {
        MetricDatum datum = MetricDatum.builder()
            .metricName("FraudulentTransaction")
            .unit(StandardUnit.COUNT)
            .value(1.0)
            .dimensions(d -> d.name("TransactionId").value(transaction.getTransactionId()))
            .build();

        PutMetricDataRequest request = PutMetricDataRequest.builder()
            .namespace("FraudDetectionMetrics")
            .metricData(datum)
            .build();

        cloudWatchClient.putMetricData(request);
    }
}
