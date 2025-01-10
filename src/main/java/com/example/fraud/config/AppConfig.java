package com.example.fraud.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.cloudwatch.CloudWatchClient;

@Configuration
public class AppConfig {
    @Bean
    public SqsClient sqsClient() {
        return SqsClient.create();
    }

    @Bean
    public CloudWatchClient cloudWatchClient() {
        return CloudWatchClient.create();
    }
}
