package app.accrescent.tools.bundletool

import org.gradle.api.Action
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Nested

abstract class BundletoolExtension {
    abstract val stripStandalones: Property<Boolean>

    @get:Nested
    abstract val signingConfig: SigningConfig

    fun signingConfig(action: Action<SigningConfig>) {
        action.execute(signingConfig)
    }

    companion object {
        const val NAME = "bundletool"
    }
}