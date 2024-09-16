package com.kuzmin.open.telemetry.upstream

import io.opentelemetry.exporter.otlp.trace.OtlpGrpcSpanExporter
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestClient

@Configuration
class UpstreamConfiguration {

    @Bean
    fun restClient(
        @Value("\${downstream.baseUrl}") downstreamBaseUrl: String,
        restClientBuilder: RestClient.Builder
    ): RestClient {
        return restClientBuilder
            .baseUrl(downstreamBaseUrl)
            .build()
    }

    @Bean
    fun otlpGrpcSpanExporter(): OtlpGrpcSpanExporter {
        return OtlpGrpcSpanExporter.builder()
            .setEndpoint("http://localhost:4317")
            .build()
    }
}