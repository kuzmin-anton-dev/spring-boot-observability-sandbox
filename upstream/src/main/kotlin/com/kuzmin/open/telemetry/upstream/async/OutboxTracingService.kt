package com.kuzmin.open.telemetry.upstream.async

import io.opentelemetry.api.trace.SpanKind
import io.opentelemetry.api.trace.StatusCode
import io.opentelemetry.api.trace.Tracer
import io.opentelemetry.context.Context
import io.opentelemetry.context.propagation.ContextPropagators
import io.opentelemetry.context.propagation.TextMapGetter
import io.opentelemetry.context.propagation.TextMapSetter
import org.springframework.stereotype.Component
import kotlin.use

@Component
class OutboxTracingService(
    private val tracer: Tracer,
    private val contextPropagators: ContextPropagators
) {

    fun extractCurrentContext() = OutboxTraceContext(
        mutableMapOf<String, String>()
            .also { contextPropagators.textMapPropagator.inject(Context.current(), it, OutboxTextMapSetter) }
            .toMap()
    )

    fun <T> withSpan(
        spanName: String,
        parent: OutboxTraceContext? = null,
        block: () -> T
    ): T {
        val parentContext = parent?.let {
            contextPropagators.textMapPropagator.extract(
                Context.current(), it,
                OutboxTextMapGetter
            )
        }

        val span = tracer
            .spanBuilder(spanName)
            .setSpanKind(SpanKind.PRODUCER)
            .apply { if (parentContext == null) setNoParent() else setParent(parentContext) }
            .startSpan()

        return try {
            span.makeCurrent().use { block() }
        } catch (e: Throwable) {
            span.setStatus(StatusCode.ERROR)
            span.recordException(e)
            throw e
        } finally {
            span.end()
        }
    }

    private object OutboxTextMapSetter : TextMapSetter<MutableMap<String, String>> {
        override fun set(carrier: MutableMap<String, String>?, key: String, value: String) {
            carrier?.set(key, value)
        }
    }

    private object OutboxTextMapGetter : TextMapGetter<OutboxTraceContext> {
        override fun keys(carrier: OutboxTraceContext) = carrier.context.keys

        override fun get(carrier: OutboxTraceContext?, key: String): String? = carrier?.context?.get(key)
    }
}
