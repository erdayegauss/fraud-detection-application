# Real-Time Fraud Detection System

## Objective
The Real-Time Fraud Detection System is designed to detect fraudulent financial transactions in real-time. This service is implemented in Java using Spring Boot, deployed on a Kubernetes (K8s) cluster on a cloud platform (AWS EKS, GCP GKE, or Alibaba ACK), and leverages AWS services such as SQS for message queuing and CloudWatch for metrics.

![Fraud Detection Diagram](diagram.png)
## Features

### 1. **Core Functionality**
- **Transaction Analysis**: Analyzes financial transactions in real-time to detect potential fraud.
- **Fraud Detection Mechanism**:
  - **Amount-Based Rule**: Flags transactions exceeding a predefined threshold (e.g., $10,000).
  - **Suspicious Account Rule**: Flags transactions involving suspicious accounts (e.g., predefined account IDs).
- **Fraud Notification**: Logs fraudulent transactions and records metrics in AWS CloudWatch.

### 2. **High Availability and Resilience**
- **Kubernetes Deployment**: Deployed on a Kubernetes cluster using AWS EKS, GCP GKE, or Alibaba ACK.
- **Message Queuing**: Uses AWS SQS for handling incoming transaction data.
- **High Availability**: The service leverages Kubernetes' Horizontal Pod Autoscaler (HPA) for scalability.

### 3. **Performance**
- Optimized for real-time transaction processing with low latency.
- Distributed logging is enabled via AWS CloudWatch to monitor transaction data and fraud detection metrics.

### 4. **Testing**
- **Unit Testing**: Core functionality is tested using JUnit.
- **Integration Testing**: Verifies the interaction between the service and AWS services (SQS and CloudWatch).
- **Fraud Simulation**: Simulates fraudulent transactions for accurate fraud detection.
- **Resilience Testing**: Verifies the system’s ability to recover from failures (e.g., pod restarts, node failures).

### 5. **Documentation**
- Comprehensive documentation for deployment and testing is provided in this README.
- Architecture diagrams and design decisions are included.

## Architecture Diagram
_Include your architecture diagram here, showcasing the interactions between the different components (fraud detection service, Kubernetes, cloud services, message queues, logging)._

## Design Choices

### 1. **Spring Boot Framework**
Spring Boot provides a robust, production-ready framework that simplifies Java application development. Its built-in dependency injection and scheduling capabilities allow for efficient processing of SQS messages and integration with cloud services like AWS SQS and CloudWatch.

### 2. **Rule-Based Fraud Detection**
A simple rule-based approach is used for fraud detection, ensuring ease of implementation and clarity in detecting anomalies. The system checks for transactions exceeding a defined threshold and monitors predefined suspicious accounts.

### 3. **AWS SQS for Message Queuing**
AWS SQS is chosen for its scalability, reliability, and ease of integration with cloud-native applications. It ensures the system can handle a high volume of real-time transactions without loss.

### 4. **AWS CloudWatch for Logging and Metrics**
AWS CloudWatch enables centralized logging and metrics collection, allowing for real-time monitoring and analysis of fraudulent transactions. It provides actionable insights for operational efficiency and debugging.

### 5. **Kubernetes Deployment**
Kubernetes ensures high availability, scalability, and resilience through features like Deployments, Services, and Horizontal Pod Autoscalers (HPA). The service is deployed in a cloud-agnostic way, allowing flexibility to use AWS, GCP, or Alibaba Cloud.

### 6. **Distributed Architecture**
The system uses a distributed architecture to ensure resilience and scalability. Each component (fraud detection logic, transaction processing, and logging) operates independently, reducing the impact of failures.

### 7. **Gradle Build Tool**
Gradle is used for its fast and flexible build process, enabling efficient dependency management and testing workflows.

## Prerequisites
- Kubernetes (AWS EKS, GCP GKE, Alibaba ACK)
- AWS SQS (or other message queuing services like GCP Pub/Sub, Alibaba Message Service)
- AWS CloudWatch (or alternative cloud-native logging services like GCP Stackdriver, Alibaba Cloud Log Service)
- Java (JDK 11 or higher)
- Gradle for building the project

## Setup and Deployment

### 1. Clone the Repository
```bash
git clone https://github.com/your-username/fraud-detection-system.git
cd fraud-detection-system
```

### 2. Build the Project
```bash
gradle build
```

### 3. Kubernetes Deployment
- Ensure you have a Kubernetes cluster running on AWS EKS, GCP GKE, or Alibaba ACK.
- Create deployment and service manifests or use Helm charts for deployment.
- Apply Kubernetes manifests to deploy the service:
  ```bash
  kubectl apply -f k8s-deployment.yaml
  ```

### 4. AWS SQS Configuration
- Create an SQS queue (e.g., `fraud-detection-queue`).
- Ensure that the fraud detection service can access and process messages from the queue.

### 5. CloudWatch Logging
- Set up AWS CloudWatch to collect logs and metrics.
- The system will log fraudulent transactions and record metrics related to fraud detection.

## Code Overview

### `FraudDetectionApplication.java`
The main entry point of the application, responsible for initializing and running the fraud detection service. It is configured to run on a scheduled task that processes messages from an AWS SQS queue.

### `FraudDetector.java`
This component contains the fraud detection logic, including predefined rules for detecting fraud:
- Transactions exceeding the `MAX_AMOUNT_THRESHOLD`.
- Transactions involving suspicious accounts.

It also records fraud metrics to AWS CloudWatch.

### `Transaction.java`
Represents a financial transaction with properties like `transactionId`, `amount`, `senderAccount`, `receiverAccount`, and `timestamp`.

### `TransactionProcessor.java`
Responsible for processing transactions from the SQS queue, deserializing them, and passing them to the `FraudDetector` for fraud detection. If a transaction is flagged as fraudulent, it logs the event and deletes the message from the queue.

## Testing

### 1. Unit Testing
Unit tests are written using JUnit. To run the tests, execute:
```bash
./gradlew test
```

### 2. Integration Testing
Integration tests verify the interaction between the fraud detection service and AWS SQS/CloudWatch. Simulate fraudulent transactions and verify if the system correctly detects and logs them.

### 3. Resilience Testing
Test the system's ability to recover from various failures, such as pod restarts and node failures, using Kubernetes' features like self-healing and horizontal scaling.

## Contributing
Feel free to fork this repository and submit pull requests. Please ensure any changes are well-documented and thoroughly tested before submitting.

## License
This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

