package com.kuzmin.open.telemetry.upstream.async

import io.micrometer.tracing.TraceContext
import io.micrometer.tracing.Tracer

data class OutboxTraceContext(
    val traceId: String,
    val spanId: String,
    val parentSpanId: String?,
    val sampled: Boolean,
)

fun Tracer.extractCurrentContext(): OutboxTraceContext =
    currentTraceContext().context().let {
        OutboxTraceContext(
            traceId = it?.traceId() ?: "",
            spanId = it?.spanId() ?: "",
            parentSpanId = it?.parentId(),
            sampled = it?.sampled() == true
        )
    }

fun Tracer.restoreContext(context: OutboxTraceContext): TraceContext =
    traceContextBuilder()
        .traceId(context.traceId)
        .spanId(context.spanId)
        .parentId(context.parentSpanId ?: "")
        .sampled(context.sampled)
        .build()
