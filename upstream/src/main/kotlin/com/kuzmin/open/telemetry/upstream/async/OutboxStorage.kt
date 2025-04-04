package com.kuzmin.open.telemetry.upstream.async

import org.springframework.stereotype.Component
import java.util.LinkedList

@Component
class OutboxStorage {

    private val messageQueue = LinkedList<Pair<OutboxTraceContext, () -> Unit>>()

    fun store(context: OutboxTraceContext, runnable: () -> Unit) {
        messageQueue.add(context to runnable)
    }

    fun poll(): Pair<OutboxTraceContext, () -> Unit>? = messageQueue.poll()
}
