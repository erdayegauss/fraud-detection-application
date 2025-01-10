package com.example.fraud.application;

import com.example.fraud.service.TransactionProcessor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.mockito.Mockito.*;

@SpringBootTest
class FraudDetectionApplicationTest {

    @MockBean
    private TransactionProcessor transactionProcessor;

    @BeforeEach
    void setUp() {
        // Initialization before each test
    }

    @Test
    void testScheduledTask() {
        // Act
        transactionProcessor.processTransactions("mock-queue-url");

        // Assert
        verify(transactionProcessor, times(1)).processTransactions("mock-queue-url");
    }
}
