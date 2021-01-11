import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.4.21"
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
}

tasks.test {
    useJUnit()
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "1.8"
}