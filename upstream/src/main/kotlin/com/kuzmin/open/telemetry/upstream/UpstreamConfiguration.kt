package com.kuzmin.open.telemetry.upstream

import io.opentelemetry.exporter.otlp.trace.OtlpGrpcSpanExporter
import jakarta.jms.ConnectionFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.jms.DefaultJmsListenerContainerFactoryConfigurer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jms.config.DefaultJmsListenerContainerFactory
import org.springframework.jms.config.JmsListenerContainerFactory
import org.springframework.jms.support.converter.MappingJackson2MessageConverter
import org.springframework.jms.support.converter.MessageConverter
import org.springframework.jms.support.converter.MessageType
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

    @Bean
    fun downstreamMessageFactory(
        connectionFactory: ConnectionFactory,
        configurer: DefaultJmsListenerContainerFactoryConfigurer
    ): JmsListenerContainerFactory<*> {
        val factory = DefaultJmsListenerContainerFactory()
        configurer.configure(factory, connectionFactory)
        return factory
    }

    @Bean
    fun jacksonJmsMessageConverter(): MessageConverter {
        val converter = MappingJackson2MessageConverter()
        converter.setTargetType(MessageType.TEXT)
        converter.setTypeIdPropertyName("_type")
        return converter
    }
}