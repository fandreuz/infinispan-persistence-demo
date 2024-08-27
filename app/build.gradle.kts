plugins {
    id("com.diffplug.spotless") version "6.25.0"
    java
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.infinispan:infinispan-core:15.0.8.Final")
    implementation("org.slf4j:slf4j-simple:2.0.16")

    // Use JUnit Jupiter for testing.
    testImplementation(libs.junit.jupiter)
    testImplementation("org.junit-pioneer:junit-pioneer:2.2.0")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testImplementation("org.awaitility:awaitility:4.2.2")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}

allprojects {
    spotless {
        java {
            palantirJavaFormat()
            indentWithSpaces()
            removeUnusedImports()
            endWithNewline()
        }
    }
}