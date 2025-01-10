package com.example.fraud.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import com.example.fraud.integration.SqsService;
import com.example.fraud.integration.CloudWatchService;
import com.example.fraud.model.Transaction;
import com.example.fraud.util.JsonUtils;
import software.amazon.awssdk.services.sqs.model.Message;


import java.util.List;

@Service
public class TransactionProcessor {
    private static final Logger logger = LoggerFactory.getLogger(TransactionProcessor.class);
    private final SqsService sqsService;
    private final FraudDetector fraudDetector;
    private final CloudWatchService cloudWatchService;

    public TransactionProcessor(SqsService sqsService, FraudDetector fraudDetector, CloudWatchService cloudWatchService) {
        this.sqsService = sqsService;
        this.fraudDetector = fraudDetector;
        this.cloudWatchService = cloudWatchService;
    }

    public void processTransactions(String queueUrl) {

        List<Message> messages = sqsService.receiveMessages(queueUrl, 10);
        
        for (Message message : messages) {
            Transaction transaction = sqsService.deserializeTransaction(message);
            if (fraudDetector.isFraudulent(transaction)) {
                logger.info("Fraudulent transaction detected: {}", transaction.getTransactionId());
                cloudWatchService.recordFraudMetric(transaction.getTransactionId());
            }

            sqsService.deleteMessage(queueUrl, message.receiptHandle());
        }
    }
}
