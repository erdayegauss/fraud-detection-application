package com.example.fraud.service;

import com.example.fraud.integration.SqsService;
import com.example.fraud.integration.CloudWatchService;
import com.example.fraud.model.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import software.amazon.awssdk.services.sqs.model.Message;

import java.util.Collections;

import static org.mockito.Mockito.*;

class TransactionProcessorTest {

    @Mock
    private SqsService sqsService;
    
    @Mock
    private FraudDetector fraudDetector;
    
    @Mock
    private CloudWatchService cloudWatchService;

    private TransactionProcessor transactionProcessor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);  // Initialize mocks
        transactionProcessor = new TransactionProcessor(sqsService, fraudDetector, cloudWatchService);
    }

    @Test
    void testProcessTransactions_FraudulentTransaction() {
        // Arrange
        Message mockMessage = mock(Message.class);
        Transaction mockTransaction = new Transaction("1", 15000.0,"12345", "67890", 1736340588);
        
        when(sqsService.receiveMessages(anyString(), eq(10))).thenReturn(Collections.singletonList(mockMessage));
        when(sqsService.deserializeTransaction(mockMessage)).thenReturn(mockTransaction);
        when(fraudDetector.isFraudulent(mockTransaction)).thenReturn(true);  // Fraudulent transaction

        // Act
        transactionProcessor.processTransactions("some-queue-url");

        // Assert
        verify(sqsService).receiveMessages(anyString(), eq(10));
        verify(sqsService).deserializeTransaction(mockMessage);
        verify(fraudDetector).isFraudulent(mockTransaction);
        verify(cloudWatchService).recordFraudMetric(mockTransaction.getTransactionId());
    }

    @Test
    void testProcessTransactions_NonFraudulentTransaction() {
        // Arrange
        Message mockMessage = mock(Message.class);
        Transaction mockTransaction = new Transaction("1", 1500.0,"23450", "67891", 1736340588);

        when(sqsService.receiveMessages(anyString(), eq(10))).thenReturn(Collections.singletonList(mockMessage));
        when(sqsService.deserializeTransaction(mockMessage)).thenReturn(mockTransaction);
        when(fraudDetector.isFraudulent(mockTransaction)).thenReturn(false);  // Non-fraudulent transaction

        // Act
        transactionProcessor.processTransactions("some-queue-url");

        // Assert
        verify(sqsService).receiveMessages(anyString(), eq(10));
        verify(sqsService).deserializeTransaction(mockMessage);
        verify(fraudDetector).isFraudulent(mockTransaction);
        verify(cloudWatchService, never()).recordFraudMetric(anyString());  // No fraud metric recorded
    }
}
