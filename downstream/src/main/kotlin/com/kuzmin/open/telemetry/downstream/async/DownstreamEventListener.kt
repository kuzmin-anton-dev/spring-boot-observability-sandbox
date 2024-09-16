package com.kuzmin.open.telemetry.downstream.async

import com.kuzmin.open.telemetry.downstream.DownstreamEntity
import com.kuzmin.open.telemetry.downstream.DownstreamRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.jms.annotation.JmsListener
import org.springframework.stereotype.Component

@Component
class DownstreamEventListener(
    private val downstreamRepository: DownstreamRepository,
) {

    private val logger: Logger = LoggerFactory.getLogger(DownstreamEventListener::class.java)

    @JmsListener(destination = "store_message", containerFactory = "downstreamMessageFactory")
    fun receiveMessage(message: StoreMessageMessage) {
        logger.info("Received message: message={}", message)
        val entity = DownstreamEntity(
            message = message.message
        )
        val storedEntity = downstreamRepository.save(entity)
        logger.info("Stored message: $storedEntity")
    }
}