import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `java-gradle-plugin`
    id("org.jetbrains.kotlin.jvm") version "1.8.10"
}

repositories {
    google()
    mavenCentral()
}

group = "app.accrescent.tools"

gradlePlugin {
    plugins {
        create("bundletool-gradle-plugin") {
            id = "app.accrescent.tools.bundletool"
            implementationClass = "app.accrescent.tools.bundletool.BundletoolPlugin"
        }
    }
}

dependencies {
    compileOnly("com.android.tools:sdklib:30.4.0")
    compileOnly("com.android.tools.build:bundletool:1.14.0")
    compileOnly("com.android.tools.build:gradle:7.4.0")
}

tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions {
        jvmTarget = "11"
    }
}
