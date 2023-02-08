package app.accrescent.tools.bundletool

import com.android.build.gradle.internal.signing.SigningConfigDataProvider
import com.android.sdklib.BuildToolInfo
import com.android.tools.build.bundletool.androidtools.Aapt2Command
import com.android.tools.build.bundletool.commands.BuildApksCommand
import com.android.tools.build.bundletool.model.Password
import com.android.tools.build.bundletool.model.SigningConfiguration
import org.gradle.api.DefaultTask
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.Nested
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import java.io.File
import java.util.Optional

abstract class BundletoolTask : DefaultTask() {
    @get:InputFile
    abstract val bundleProperty: RegularFileProperty

    @get:Nested
    lateinit var signingConfigDataProvider: SigningConfigDataProvider

    @get:Internal
    abstract val buildToolInfo: Property<BuildToolInfo>

    @get:OutputFile
    abstract val destination: RegularFileProperty

    @TaskAction
    fun buildApks() {
        val aapt2File = File(buildToolInfo.get().getPath(BuildToolInfo.PathId.AAPT2))

        val signingConfig = run {
            val data = signingConfigDataProvider.resolve()
            val storePassword = when (data?.storePassword) {
                null -> Optional.empty()
                else -> Optional.of(Password.createFromStringValue("pass:${data.storePassword}"))
            }
            val keyPassword = when (data?.keyPassword) {
                null -> Optional.empty()
                else -> Optional.of(Password.createFromStringValue("pass:${data.keyPassword}"))
            }
            SigningConfiguration.extractFromKeystore(
                data?.storeFile?.toPath(),
                data?.keyAlias,
                storePassword,
                keyPassword,
            )
        }

        BuildApksCommand.builder()
            .setBundlePath(bundleProperty.get().asFile.toPath())
            .setOutputFile(destination.get().asFile.toPath())
            .setAapt2Command(Aapt2Command.createFromExecutablePath(aapt2File.toPath()))
            .setSigningConfiguration(signingConfig)
            .build()
            .execute()
    }
}
