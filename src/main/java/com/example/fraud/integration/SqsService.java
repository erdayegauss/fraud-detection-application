package com.example.fraud.integration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.*;
import com.example.fraud.model.Transaction;
import com.example.fraud.util.JsonUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SqsService {
    private static final Logger logger = LoggerFactory.getLogger(SqsService.class);
    private final SqsClient sqsClient;

    public SqsService(SqsClient sqsClient) {
        this.sqsClient = sqsClient;
    }

    public List<Message> receiveMessages(String queueUrl, int maxMessages) {
        try {
            ReceiveMessageRequest receiveMessageRequest = ReceiveMessageRequest.builder()
                .queueUrl(queueUrl)
                .maxNumberOfMessages(maxMessages)
                .build();

            List<Message> messages = sqsClient.receiveMessage(receiveMessageRequest).messages();
            // logger.info("Received {} messages from SQS queue {}", messages.size(), queueUrl);
            return messages;
        } catch (Exception e) {
            logger.error("Failed to receive messages from SQS queue {}", queueUrl, e);
            return List.of();
        }
    }

    public void deleteMessage(String queueUrl, String receiptHandle) {
        try {
            DeleteMessageRequest deleteMessageRequest = DeleteMessageRequest.builder()
                .queueUrl(queueUrl)
                .receiptHandle(receiptHandle)
                .build();

            sqsClient.deleteMessage(deleteMessageRequest);
            // logger.info("Deleted message from SQS queue {}", queueUrl);
        } catch (Exception e) {
            logger.error("Failed to delete message from SQS queue {}", queueUrl, e);
        }
    }

    public Transaction deserializeTransaction(Message message) {
        return JsonUtils.deserialize(message.body(), Transaction.class);
    }
}
