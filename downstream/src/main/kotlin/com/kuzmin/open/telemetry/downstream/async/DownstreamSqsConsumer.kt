package com.kuzmin.open.telemetry.downstream.async

import com.kuzmin.open.telemetry.downstream.DownstreamEntity
import com.kuzmin.open.telemetry.downstream.DownstreamRepository
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import software.amazon.awssdk.services.sqs.SqsClient
import software.amazon.awssdk.services.sqs.model.DeleteMessageRequest
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest
import java.util.concurrent.TimeUnit

@Component
class DownstreamSqsConsumer(
    private val sqsClient: SqsClient,
    private val downstreamRepository: DownstreamRepository
) {

    private val logger = LoggerFactory.getLogger(javaClass)

    /**
     * Scheduled method to poll for messages periodically
     * Uncomment and adjust the cron expression as needed
     */
    @Scheduled(fixedDelay = 5, timeUnit = TimeUnit.SECONDS)
    fun pollMessages() {
        receiveMessages()
    }

    /**
     * Receives messages from the SQS queue and processes them
     */
    private fun receiveMessages() {
        logger.info("Polling for messages from SQS queue")

        val receiveMessageRequest = ReceiveMessageRequest.builder()
            .queueUrl("http://sqs.us-east-1.localhost.localstack.cloud:4566/000000000000/store-message-sqs")
            .maxNumberOfMessages(10)
            .waitTimeSeconds(5)
            .build()

        val messages = sqsClient.receiveMessage(receiveMessageRequest).messages()

        logger.info("Received ${messages.size} messages from SQS queue")

        messages.forEach { message ->
            try {
                logger.info("Processing message: ${message.messageId()}")

                val entity = DownstreamEntity(
                    message = message.body()
                )
                val storedEntity = downstreamRepository.save(entity)
                logger.info("Stored message: $storedEntity")

                deleteMessage(message.receiptHandle())
            } catch (e: Exception) {
                logger.error("Error processing message ${message.messageId()}: ${e.message}")
            }
        }
    }

    /**
     * Deletes a message from the SQS queue
     *
     * @param receiptHandle The receipt handle of the message to delete
     */
    private fun deleteMessage(receiptHandle: String) {
        val deleteRequest = DeleteMessageRequest.builder()
            .queueUrl("store-message-sqs")
            .receiptHandle(receiptHandle)
            .build()
        sqsClient.deleteMessage(deleteRequest)
        logger.info("Message deleted from queue")
    }
}