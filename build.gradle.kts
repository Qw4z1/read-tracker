val ktorVersion: String by project
val kotlinVersion: String by project
val exposedVersion: String by project

plugins {
    application
    kotlin("jvm") version "1.9.0"
    id("io.ktor.plugin") version "2.3.2"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.8.0"
}

kotlin {
    jvmToolchain(17)
}

application {
    mainClass.set("io.ktor.server.netty.EngineMain")
}

group = "io.nyblom"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven { url = uri("https://maven.pkg.jetbrains.space/public/p/ktor/eap") }
}
// Gradle plugin Docker config saved for later
//ktor {
//    docker {
//        jreVersion.set(io.ktor.plugin.features.JreVersion.JRE_17)
//        localImageName.set("reads-tracker")
//        imageTag.set("0.0.1-preview")
//        portMappings.set(
//            listOf(
//                io.ktor.plugin.features.DockerPortMapping(
//                    80,
//                    8080,
//                    io.ktor.plugin.features.DockerPortMappingProtocol.TCP
//                )
//            )
//        )
//        val env = File(".env")
//        env.forEachLine {
//            val line = it.split('=')
//            environmentVariable(line[0], line[1])
//        }
//    }
//}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlinVersion")
    implementation("ch.qos.logback:logback-classic:1.4.8")
    implementation("mysql:mysql-connector-java:8.0.33")

    implementation("io.ktor:ktor-server-core:$ktorVersion")
    implementation("io.ktor:ktor-server-netty:$ktorVersion")
    implementation("io.ktor:ktor-server-auth:$ktorVersion")
    implementation("io.ktor:ktor-server-cors:$ktorVersion")

    implementation("io.ktor:ktor-server-content-negotiation:$ktorVersion")
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")

    implementation("org.jetbrains.exposed:exposed-core:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-dao:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposedVersion")
}