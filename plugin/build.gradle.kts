import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `java-gradle-plugin`
    id("org.jetbrains.kotlin.jvm") version "1.8.10"
}

repositories {
    mavenCentral()
}

gradlePlugin {
    plugins {
        create("bundletool-gradle-plugin") {
            id = "app.accrescent.tools.bundletool"
            implementationClass = "app.accrescent.tools.bundletool.BundletoolPlugin"
        }
    }
}

tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions {
        jvmTarget = "11"
    }
}
