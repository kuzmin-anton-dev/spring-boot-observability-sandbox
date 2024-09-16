package com.kuzmin.open.telemetry.downstream

import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(
    path = ["/downstream"],
    produces = ["application/json"]
)
class DownstreamController(
    private val downstreamRepository: DownstreamRepository
) {

    private val logger = LoggerFactory.getLogger(DownstreamController::class.java)

    @PostMapping(consumes = ["application/json"])
    fun storeMessage(@RequestBody message: StoreMessageRequest): StoreMessageResponse {
        logger.info("Storing message: ${message.message}")
        val entity = DownstreamEntity(
            message = message.message
        )
        val savedEntity = downstreamRepository.save(entity)
        logger.info("Stored message: $savedEntity")
        return StoreMessageResponse(savedEntity.id!!)
    }

    @GetMapping("/{id}")
    fun getMessageById(@PathVariable id: Long): GetMessageResponse {
        logger.info("Getting message by id: $id")
        val entity = downstreamRepository.findById(id).orElseThrow()
        return GetMessageResponse(
            id = entity.id!!,
            message = entity.message
        )
    }
}