package com.kuzmin.open.telemetry.downstream

import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Immutable

@Immutable
data class DownstreamEntity(
    @Id
    val id: Long? = null,
    val message: String
)