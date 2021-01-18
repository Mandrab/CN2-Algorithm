import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    kotlin("jvm") version "1.4.21"
    application
    id("com.github.johnrengelman.shadow") version "5.1.0"
}

group = "dev.mandrab"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://jcenter.bintray.com")
    maven("https://jitpack.io")
}

dependencies {
    testImplementation(kotlin("test-junit"))
    implementation("com.github.ajalt.clikt:clikt:3.1.0")
    implementation("com.github.holgerbrandl:krangl:v0.14")
    implementation("org.apache.commons:commons-csv:1.8")
    implementation("com.google.guava:guava:30.1-jre")
}

// Required by the 'shadowJar' task
project.setProperty("mainClassName", "controller.MainKt")

application {
    mainClass.set("controller.MainKt")
}

tasks.withType<ShadowJar> {
    manifest {
        attributes["Main-Class"] = "controller.MainKt"
    }
}

tasks.test {
    useJUnit()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}
