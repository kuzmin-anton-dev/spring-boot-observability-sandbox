package com.kuzmin.open.telemetry.downstream

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class DownstreamApplication

fun main(args: Array<String>) {
    runApplication<DownstreamApplication>(*args)
}
