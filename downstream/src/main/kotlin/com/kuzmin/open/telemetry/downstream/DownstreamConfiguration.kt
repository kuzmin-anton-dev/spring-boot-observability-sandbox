package com.kuzmin.open.telemetry.downstream

import io.opentelemetry.api.GlobalOpenTelemetry
import io.opentelemetry.api.OpenTelemetry
import io.opentelemetry.api.trace.Tracer
import io.opentelemetry.context.propagation.ContextPropagators
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class DownstreamConfiguration {

    @Bean
    fun openTelemetry(): OpenTelemetry = GlobalOpenTelemetry.get()

    @Bean
    fun tracer(openTelemetry: OpenTelemetry): Tracer = openTelemetry.getTracer("")

    @Bean
    fun contextPropagators(openTelemetry: OpenTelemetry): ContextPropagators = openTelemetry.propagators
}
