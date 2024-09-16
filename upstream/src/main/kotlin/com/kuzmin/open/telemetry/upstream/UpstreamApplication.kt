package com.kuzmin.open.telemetry.upstream

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class UpstreamApplication

fun main(args: Array<String>) {
    runApplication<UpstreamApplication>(*args)
}
