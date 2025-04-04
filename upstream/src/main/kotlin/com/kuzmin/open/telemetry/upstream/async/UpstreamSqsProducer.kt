package com.kuzmin.open.telemetry.upstream.async

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import software.amazon.awssdk.services.sqs.SqsClient
import software.amazon.awssdk.services.sqs.model.SendMessageRequest

@Component
class UpstreamSqsProducer(private val sqsClient: SqsClient) {

    private val logger = LoggerFactory.getLogger(javaClass)

    /**
     * Sends a message to the SQS queue
     *
     * @param message The message to send
     * @return The message ID if successful
     */
    fun sendMessage(message: String): String {
        logger.info("Sending message to SQS queue: $message")

        val sendMessageRequest = SendMessageRequest.builder()
            .queueUrl("http://sqs.us-east-1.localhost.localstack.cloud:4566/000000000000/store-message-sqs")
            .messageBody(message)
            .build()

        try {
            val result = sqsClient.sendMessage(sendMessageRequest)
            logger.info("Message sent with ID: ${result.messageId()}")
            return result.messageId()
        } catch (e: Exception) {
            logger.error("Error sending message to SQS queue: ${e.message}")
            throw e
        }
    }
}