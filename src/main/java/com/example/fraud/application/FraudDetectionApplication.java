package com.example.fraud.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import com.example.fraud.service.TransactionProcessor;
import org.springframework.beans.factory.annotation.Autowired;


@SpringBootApplication(scanBasePackages = "com.example.fraud")  // Ensure it scans the correct package
@EnableScheduling
public class FraudDetectionApplication {

    // Injecting the TransactionProcessor service
    private final TransactionProcessor transactionProcessor;

    @Autowired
    public FraudDetectionApplication(TransactionProcessor transactionProcessor) {
        this.transactionProcessor = transactionProcessor;
    }    

    public static void main(String[] args) {
        SpringApplication.run(FraudDetectionApplication.class, args);
        System.out.println("Fraud Detection Service is running...");
    }

    // A simple scheduled task to keep the application alive
    @Scheduled(fixedRateString = "5000")  // Use the fixed rate from YAML
    public void processSqsMessages() {
        // // Directly create the TransactionProcessor and pass the required dependencies
        String queueUrl = "https://sqs.ap-northeast-2.amazonaws.com/216717620097/fraud-detection-queue";  
        transactionProcessor.processTransactions(queueUrl);

    }

}

