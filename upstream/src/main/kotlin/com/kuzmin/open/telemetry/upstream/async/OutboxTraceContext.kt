package com.kuzmin.open.telemetry.upstream.async

data class OutboxTraceContext(
    val context: Map<String, String>,
)
