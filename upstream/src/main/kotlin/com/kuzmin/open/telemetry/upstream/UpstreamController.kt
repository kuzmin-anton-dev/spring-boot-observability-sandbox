package com.kuzmin.open.telemetry.upstream

import com.kuzmin.open.telemetry.downstream.GetMessageResponse
import com.kuzmin.open.telemetry.downstream.StoreMessageRequest
import com.kuzmin.open.telemetry.downstream.StoreMessageResponse
import com.kuzmin.open.telemetry.downstream.async.StoreMessageMessage
import com.kuzmin.open.telemetry.upstream.async.OutboxStorage
import com.kuzmin.open.telemetry.upstream.async.extractCurrentContext
import io.micrometer.tracing.Tracer
import org.slf4j.LoggerFactory
import org.springframework.jms.core.JmsTemplate
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.RestClient
import java.util.*

@RestController
@RequestMapping(
    path = ["/upstream"]
)
class UpstreamController(
    private val restClient: RestClient,
    private val jmsTemplate: JmsTemplate,
    private val outboxStorage: OutboxStorage,
    private val tracer: Tracer
) {

    private val logger = LoggerFactory.getLogger(UpstreamController::class.java)

    @PostMapping
    fun storeRandomMessage(): Long {
        logger.info("Sync sending random message")
        val request = StoreMessageRequest(
            message = generateRandomMessage()
        )
        val response = restClient.post()
            .body(request)
            .retrieve()
            .body(StoreMessageResponse::class.java)
        return response!!.id
    }

    @PostMapping("/async")
    fun storeRandomMessageAsync() {
        logger.info("Async sending a random message")
        val message = StoreMessageMessage(
            message = generateRandomMessage()
        )
        jmsTemplate.convertAndSend("store_message", message)
    }

    @PostMapping("/async/outbox")
    fun storeRandomMessageAsyncExecutor() {
        logger.info("Async sending a random message via outbox")
        val message = StoreMessageMessage(
            message = generateRandomMessage()
        )
        outboxStorage.store(tracer.extractCurrentContext()) {
            logger.info("Send message from outbox")
            jmsTemplate.convertAndSend("store_message", message)
        }
    }

    @GetMapping("/{id}")
    fun getMessageById(@PathVariable id: Long): String {
        logger.info("Getting message by id: $id")
        val response = restClient.get()
            .uri("/$id")
            .retrieve()
            .body(GetMessageResponse::class.java)
        return response!!.message
    }

    private fun generateRandomMessage() = UUID.randomUUID().toString()
}