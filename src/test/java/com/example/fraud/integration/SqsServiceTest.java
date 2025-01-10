package com.example.fraud.integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import static org.mockito.Mockito.*;

class SqsServiceTest {

    @Mock
    private SqsClient sqsClient;

    private SqsService sqsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        sqsService = new SqsService(sqsClient);
    }

    @Test
    void testReceiveMessages() {
        ReceiveMessageResponse response = ReceiveMessageResponse.builder()
            .messages(List.of(Message.builder().body("messageBody").build()))
            .build();
        when(sqsClient.receiveMessage(any(ReceiveMessageRequest.class))).thenReturn(response);

        List<Message> messages = sqsService.receiveMessages("queueUrl", 10);

        assertNotNull(messages);
        assertEquals(1, messages.size());
        verify(sqsClient).receiveMessage(any(ReceiveMessageRequest.class));
    }

    @Test
    void testDeleteMessage() {
        sqsService.deleteMessage("queueUrl", "receiptHandle");
        verify(sqsClient).deleteMessage(any(DeleteMessageRequest.class));
    }
}
