package com.example.fraud.integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import software.amazon.awssdk.services.cloudwatch.CloudWatchClient;
import software.amazon.awssdk.services.cloudwatch.model.PutMetricDataRequest;
import software.amazon.awssdk.services.cloudwatch.model.PutMetricDataResponse;

import static org.mockito.Mockito.*;

class CloudWatchServiceTest {

    @Mock
    private CloudWatchClient cloudWatchClient;

    private CloudWatchService cloudWatchService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        cloudWatchService = new CloudWatchService(cloudWatchClient);
    }

    @Test
    void testRecordFraudMetric() {
        cloudWatchService.recordFraudMetric("tx1");

        verify(cloudWatchClient).putMetricData(any(PutMetricDataRequest.class));
    }
}
