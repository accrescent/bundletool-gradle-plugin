import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("com.gradle.plugin-publish") version "1.2.1"
    id("org.jetbrains.kotlin.jvm") version "1.9.10"
}

repositories {
    google()
    mavenCentral()
}

group = "app.accrescent.tools"
version = "0.2.1"

gradlePlugin {
    website.set("https://github.com/accrescent/bundletool-gradle-plugin")
    vcsUrl.set("https://github.com/accrescent/bundletool-gradle-plugin")

    plugins {
        create("bundletool-gradle-plugin") {
            id = "app.accrescent.tools.bundletool"
            displayName = "Bundletool Gradle plugin for Android apps"
            description = "Generates APK sets for Android apps with bundletool"
            tags.set(listOf("android"))

            implementationClass = "app.accrescent.tools.bundletool.BundletoolPlugin"
        }
    }
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(11))
    }
}

dependencies {
    compileOnly("com.android.tools:common:31.1.2")
    compileOnly("com.android.tools:sdklib:31.1.1")
    compileOnly("com.android.tools.build:bundletool:1.15.4")
    compileOnly("com.android.tools.build:gradle-api:8.1.1")
}

tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions {
        jvmTarget = "11"
    }
}
