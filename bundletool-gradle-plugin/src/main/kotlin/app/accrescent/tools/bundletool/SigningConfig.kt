package app.accrescent.tools.bundletool

import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property

abstract class SigningConfig {
    abstract val storeFile: RegularFileProperty
    abstract val storePassword: Property<String?>
    abstract val keyAlias: Property<String>
    abstract val keyPassword: Property<String?>
}