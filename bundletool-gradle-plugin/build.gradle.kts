import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("com.gradle.plugin-publish") version "1.2.1"
    id("org.jetbrains.kotlin.jvm") version "1.9.21"
}

repositories {
    google()
    mavenCentral()
}

group = "app.accrescent.tools"
version = "0.2.3"

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

kotlin {
    jvmToolchain(11)
}

dependencies {
    compileOnly("com.android.tools:common:31.1.4")
    compileOnly("com.android.tools:sdklib:31.2.0")
    compileOnly("com.android.tools.build:bundletool:1.15.6")
    compileOnly("com.android.tools.build:gradle-api:8.2.0")
}

tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions {
        jvmTarget = "11"
    }
}
