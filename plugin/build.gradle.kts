import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `java-gradle-plugin`
    id("org.jetbrains.kotlin.jvm") version "1.8.0"
}

repositories {
    mavenCentral()
}

gradlePlugin {
    val greeting by plugins.creating {
        id = "app.accrescent.tools.bundletool.greeting"
        implementationClass = "app.accrescent.tools.bundletool.BundletoolPlugin"
    }
}

tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions {
        jvmTarget = "11"
    }
}
