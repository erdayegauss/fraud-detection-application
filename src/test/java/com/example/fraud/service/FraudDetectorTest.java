package com.example.fraud.service;

import static org.junit.jupiter.api.Assertions.*;
import com.example.fraud.integration.CloudWatchService;
import com.example.fraud.model.Transaction;
import com.example.fraud.service.FraudDetector;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import software.amazon.awssdk.services.cloudwatch.CloudWatchClient;

import static org.mockito.Mockito.*;

class FraudDetectorTest {

    @Mock
    private CloudWatchClient cloudWatchClient; // Mocked CloudWatchClient

    @Mock
    private CloudWatchService cloudWatchService; // Mocked CloudWatchService (if used)

    @InjectMocks
    private FraudDetector fraudDetector; // The class under test

    @BeforeEach
    void setUp() {
        // Initialize mocks
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testIsFraudulent_transactionExceedsThreshold() {
        // Given
        Transaction transaction = new Transaction("txn123", 15000, "12345", "67890", System.currentTimeMillis());
        
        // When
        boolean isFraudulent = fraudDetector.isFraudulent(transaction);
        
        // Then
        assertTrue(isFraudulent);  // Transaction should be fraudulent due to amount exceeding threshold
    }

    @Test
    void testIsFraudulent_transactionWithinThreshold() {
        // Given
        Transaction transaction = new Transaction("txn123", 5000, "23450", "67891", System.currentTimeMillis());
        
        // When
        boolean isFraudulent = fraudDetector.isFraudulent(transaction);
        
        // Then
        assertFalse(isFraudulent); // Transaction should not be fraudulent as amount is below threshold
    }
}
