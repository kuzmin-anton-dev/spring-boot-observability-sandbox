pluginManagement {
    plugins {
        kotlin("jvm") version "1.9.25"
    }
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}
rootProject.name = "spring-boot-observability-sandbox"

include(
    "upstream",
    "downstream",
    "downstream-api"
)
