package com.kuzmin.open.telemetry.upstream.async

import io.micrometer.tracing.SpanAndScope
import io.micrometer.tracing.Tracer
import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationListener
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.stereotype.Component
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

@Component
class OutboxPoller(
    private val storage: OutboxStorage,
    private val tracer: Tracer
) : ApplicationListener<ContextRefreshedEvent>, AutoCloseable {

    private val logger = LoggerFactory.getLogger(OutboxPoller::class.java)
    private val executor = Executors.newSingleThreadScheduledExecutor()

    override fun onApplicationEvent(event: ContextRefreshedEvent) {
        executor.scheduleAtFixedRate(::pollMessage, 0L, 5, TimeUnit.SECONDS)
    }

    private fun pollMessage() {
        logger.info("Polling for messages")
        val (context, runnable) = storage.poll() ?: return
        val span = tracer.spanBuilder()
            .name("poll_messages")
            .setParent(tracer.restoreContext(context))
            .start()
        SpanAndScope(span, tracer.withSpan(span)).use { runnable.invoke() }
        logger.info("Message sent")
    }

    override fun close() {
        executor.close()
    }
}