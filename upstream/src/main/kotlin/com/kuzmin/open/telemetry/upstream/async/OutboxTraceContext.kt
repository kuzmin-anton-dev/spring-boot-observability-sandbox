package com.kuzmin.open.telemetry.upstream.async

data class OutboxTraceContext(
    val traceId: String,
    val spanId: String,
    val parentSpanId: String?,
    val sampled: Boolean,
)