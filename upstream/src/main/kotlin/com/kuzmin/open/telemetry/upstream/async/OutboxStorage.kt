package com.kuzmin.open.telemetry.upstream.async

import org.springframework.stereotype.Component
import java.util.LinkedList

@Component
class OutboxStorage {

    private val messageQueue = LinkedList<() -> Unit>()

    fun store(runnable: () -> Unit) {
        messageQueue.add(runnable)
    }

    fun poll(): () -> Unit = messageQueue.poll()
}
