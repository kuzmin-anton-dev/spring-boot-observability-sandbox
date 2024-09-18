package com.kuzmin.open.telemetry.upstream.async

import io.micrometer.tracing.annotation.NewSpan
import org.springframework.stereotype.Component
import java.util.LinkedList

@Component
class OutboxStorage {

    private val messageQueue = LinkedList<Pair<OutboxTraceContext, () -> Unit>>()

    @NewSpan("store_outbox_message")
    fun store(
        traceContext: OutboxTraceContext,
        runnable: () -> Unit,
    ) {
        messageQueue.add(Pair(traceContext, runnable))
    }

    fun poll(): Pair<OutboxTraceContext, () -> Unit>? = messageQueue.poll()
}