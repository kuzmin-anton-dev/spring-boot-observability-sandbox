package com.kuzmin.open.telemetry.downstream

import io.opentelemetry.exporter.otlp.trace.OtlpGrpcSpanExporter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class DownstreamConfiguration {

    @Bean
    fun otlpGrpcSpanExporter() : OtlpGrpcSpanExporter {
        return OtlpGrpcSpanExporter.builder()
            .setEndpoint("http://localhost:4317")
            .build()
    }
}