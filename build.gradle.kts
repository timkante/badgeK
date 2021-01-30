import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.jetbrains.kotlin.config.KotlinCompilerVersion
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val kotlinx_serialization_version: String by project
val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project

plugins {
    application
    kotlin("jvm")
    kotlin("plugin.serialization") version "1.4.21"
    id("com.github.johnrengelman.shadow") version "6.0.0"
}

group = "dev.timkante"
version = "0.0.1"

application {
    mainClassName = "io.ktor.server.netty.EngineMain"
}

kotlin.sourceSets["main"].kotlin.srcDirs("src")
kotlin.sourceSets["test"].kotlin.srcDirs("test")

sourceSets["main"].resources.srcDirs("resources")
sourceSets["test"].resources.srcDirs("testresources")

repositories {
    mavenLocal()
    jcenter()
    maven { url = uri("https://kotlin.bintray.com/ktor") }
    maven { url = uri("https://kotlin.bintray.com/kotlin-js-wrappers") }
}

tasks {
    withType<Jar> {
        manifest {
            attributes(
                mapOf("Main-Class" to application.mainClassName)
            )
        }
    }

    withType<KotlinCompile> {
        kotlinOptions {
            jvmTarget = "11"
        }
    }

    withType<ShadowJar> {
        archiveFileName.set("server.${archiveExtension.get()}")
    }

    "build" {
        dependsOn("shadowJar")
    }
}

dependencies {
    implementation(kotlin("stdlib", KotlinCompilerVersion.VERSION))
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-core-jvm:$kotlinx_serialization_version")
    implementation("io.ktor:ktor-server-netty:$ktor_version")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    implementation("io.ktor:ktor-server-core:$ktor_version")
    implementation("io.ktor:ktor-html-builder:$ktor_version")
    implementation("org.jetbrains:kotlin-css-jvm:1.0.0-pre.31-kotlin-1.2.41")
    implementation("io.ktor:ktor-locations:$ktor_version")
    implementation("io.ktor:ktor-client-core:$ktor_version")
    implementation("io.ktor:ktor-client-core-jvm:$ktor_version")
    implementation("io.ktor:ktor-client-apache:$ktor_version")
    testImplementation("io.ktor:ktor-server-tests:$ktor_version")
}
