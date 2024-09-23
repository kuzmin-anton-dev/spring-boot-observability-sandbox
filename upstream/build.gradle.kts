plugins {
    id("org.jetbrains.kotlin.jvm")
    id("org.jetbrains.kotlin.plugin.spring") version "1.9.25"
    id("org.springframework.boot") version "3.4.0-M3"
    id("io.spring.dependency-management") version "1.1.6"
}

group = "com.kuzmin.open.telemetry"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

repositories {
    mavenCentral()
    maven { url = uri("https://repo.spring.io/milestone") }
}

dependencyManagement {
    imports {
        mavenBom("io.opentelemetry.instrumentation:opentelemetry-instrumentation-bom:2.6.0")
    }
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-aop")
    implementation("org.springframework.boot:spring-boot-starter-activemq")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-web")

    // Traces
    implementation("io.micrometer:micrometer-tracing")
    implementation("io.micrometer:micrometer-tracing-bridge-otel")
    implementation("io.opentelemetry:opentelemetry-exporter-otlp")

    // Metrics
    implementation("io.micrometer:micrometer-registry-prometheus")

    // Logs
    implementation("net.logstash.logback:logstash-logback-encoder:8.0")
    implementation("ch.qos.logback:logback-classic:1.5.6")
    runtimeOnly("io.opentelemetry.instrumentation:opentelemetry-logback-mdc-1.0:2.8.0-alpha")

    implementation(project(":downstream-api"))

    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")

    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

kotlin {
    compilerOptions {
        freeCompilerArgs.add("-Xjsr305=strict")
    }
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}