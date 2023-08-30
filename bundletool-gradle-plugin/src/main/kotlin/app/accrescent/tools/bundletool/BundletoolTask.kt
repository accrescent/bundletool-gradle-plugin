package app.accrescent.tools.bundletool

import com.android.sdklib.BuildToolInfo
import com.android.tools.build.bundletool.androidtools.Aapt2Command
import com.android.tools.build.bundletool.commands.BuildApksCommand
import com.android.tools.build.bundletool.model.Password
import com.android.tools.build.bundletool.model.SigningConfiguration
import org.gradle.api.DefaultTask
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import java.io.File
import java.util.Optional

abstract class BundletoolTask : DefaultTask() {
    @get:InputFile
    abstract val bundleProperty: RegularFileProperty

    @get:Internal
    abstract val buildToolInfo: Property<BuildToolInfo>

    @get:InputFile
    abstract val signingConfigStoreFile: RegularFileProperty

    @get:Input
    abstract val signingConfigStorePassword: Property<String?>

    @get:Input
    abstract val signingConfigKeyAlias: Property<String>

    @get:Input
    abstract val signingConfigKeyPassword: Property<String?>

    @get:OutputFile
    abstract val destination: RegularFileProperty

    @TaskAction
    fun buildApks() {
        val aapt2File = File(buildToolInfo.get().getPath(BuildToolInfo.PathId.AAPT2))

        val bundletoolSigningConfig = run {
            val storePassword = when (signingConfigStorePassword.get()) {
                null -> Optional.empty()
                else -> Optional.of(Password.createFromStringValue("pass:${signingConfigStorePassword.get()}"))
            }
            val keyPassword = when (signingConfigKeyPassword.get()) {
                null -> Optional.empty()
                else -> Optional.of(Password.createFromStringValue("pass:${signingConfigKeyPassword.get()}"))
            }
            SigningConfiguration.extractFromKeystore(
                signingConfigStoreFile.get().asFile.toPath(),
                signingConfigKeyAlias.get(),
                storePassword,
                keyPassword,
            )
        }

        BuildApksCommand.builder()
            .setBundlePath(bundleProperty.get().asFile.toPath())
            .setOutputFile(destination.get().asFile.toPath())
            .setAapt2Command(Aapt2Command.createFromExecutablePath(aapt2File.toPath()))
            .setSigningConfiguration(bundletoolSigningConfig)
            .build()
            .execute()
    }
}
