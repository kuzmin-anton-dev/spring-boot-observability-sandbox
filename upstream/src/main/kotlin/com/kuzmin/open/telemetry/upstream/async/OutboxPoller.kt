package com.kuzmin.open.telemetry.upstream.async

import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationListener
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.stereotype.Component
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

@Component
class OutboxPoller(
    private val storage: OutboxStorage,
    private val outboxTracingService: OutboxTracingService,
) : ApplicationListener<ContextRefreshedEvent>, AutoCloseable {

    private val logger = LoggerFactory.getLogger(OutboxPoller::class.java)
    private val executor = Executors.newSingleThreadScheduledExecutor()

    override fun onApplicationEvent(event: ContextRefreshedEvent) {
        executor.scheduleAtFixedRate(::pollMessage, 0L, 5, TimeUnit.SECONDS)
    }

    private fun pollMessage() {
        logger.info("Polling for messages")
        val (context, runnable) = storage.poll() ?: return
        outboxTracingService.withSpan(
            spanName = "poll_messages",
            parent = context,
            block = runnable
        )
        logger.info("Message sent")
    }

    override fun close() {
        executor.close()
    }
}