plugins {
    id("com.diffplug.spotless") version "6.25.0"
    java
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.infinispan:infinispan-core:15.0.8.Final")

    // Use JUnit Jupiter for testing.
    testImplementation(libs.junit.jupiter)
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
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
            googleJavaFormat()
            indentWithSpaces()
            removeUnusedImports()
            endWithNewline()
        }
    }
}