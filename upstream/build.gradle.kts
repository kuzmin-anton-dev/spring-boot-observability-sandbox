plugins {
    id("org.jetbrains.kotlin.jvm")
    id("org.jetbrains.kotlin.plugin.spring") version "1.9.25"
    id("org.springframework.boot") version "3.4.4"
    id("io.spring.dependency-management") version "1.1.7"
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
}

dependencyManagement {
    imports {
        mavenBom("software.amazon.awssdk:bom:2.31.14")
        mavenBom("io.opentelemetry:opentelemetry-bom:1.48.0")
    }
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-aop")
    implementation("org.springframework.boot:spring-boot-starter-activemq")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-web")

    // Traces
    implementation("io.opentelemetry:opentelemetry-api")

    // Metrics
    implementation("io.micrometer:micrometer-registry-otlp")

    // Logs
    implementation("net.logstash.logback:logstash-logback-encoder:8.0")
    implementation("ch.qos.logback.access:logback-access-common:2.0.6")
    implementation("ch.qos.logback.access:logback-access-tomcat:2.0.6")
    implementation("ch.qos.logback:logback-classic")
    implementation("io.opentelemetry.instrumentation:opentelemetry-logback-appender-1.0:2.14.0-alpha")
    implementation("io.opentelemetry:opentelemetry-exporter-otlp")

    // AWS
    implementation("software.amazon.awssdk:sqs")

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