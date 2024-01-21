plugins {
    kotlin("jvm") version "1.9.21"
}

group = "com.cfelde"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven { url = uri("https://mvn.sigbla.app/repository") }
}

dependencies {
    implementation("sigbla.app:sigbla-app-all:1.+")
}

kotlin {
    jvmToolchain(17)
}