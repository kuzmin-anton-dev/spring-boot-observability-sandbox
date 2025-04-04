package com.kuzmin.open.telemetry.downstream

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
class DownstreamApplication

fun main(args: Array<String>) {
    runApplication<DownstreamApplication>(*args)
}
