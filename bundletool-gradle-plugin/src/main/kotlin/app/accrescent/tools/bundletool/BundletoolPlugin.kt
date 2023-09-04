package app.accrescent.tools.bundletool

import com.android.SdkConstants
import com.android.build.api.AndroidPluginVersion
import com.android.build.api.artifact.SingleArtifact
import com.android.build.api.variant.AndroidComponentsExtension
import com.android.repository.Revision
import com.android.sdklib.BuildToolInfo
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.PluginInstantiationException
import org.gradle.configurationcache.extensions.capitalized
import java.nio.file.Paths

class BundletoolPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        val bundletoolExtension = project
            .extensions
            .create(BundletoolExtension.NAME, BundletoolExtension::class.java)

        val androidComponents = project.extensions.getByType(AndroidComponentsExtension::class.java)
        val agpVer = androidComponents.pluginVersion
        if (agpVer < AndroidPluginVersion(7, 4)) {
            throw PluginInstantiationException("$agpVer not supported. Use 7.4.0 or higher.")
        }

        androidComponents.onVariants { variant ->
            val variantName = variant.name.capitalized()
            val bundle = variant.artifacts.get(SingleArtifact.BUNDLE)

            val buildToolsDir = Paths.get(
                androidComponents.sdkComponents.sdkDirectory.get().toString(),
                SdkConstants.FD_BUILD_TOOLS,
                SdkConstants.CURRENT_BUILD_TOOLS_VERSION,
            )
            val buildToolInfo = BuildToolInfo.fromStandardDirectoryLayout(
                Revision.parseRevision(SdkConstants.CURRENT_BUILD_TOOLS_VERSION),
                buildToolsDir,
            )

            val outPath = Paths.get("outputs", "apkset", variant.name, "app-${variant.name}.apks")
            val outFile = project
                .layout
                .buildDirectory
                .file(outPath.toString())

            project.tasks.register("buildApks${variantName}", BundletoolTask::class.java) {
                it.bundleProperty.set(bundle)
                it.buildToolInfo.set(buildToolInfo)
                it.signingConfigStoreFile.set(bundletoolExtension.signingConfig.storeFile)
                it.signingConfigStorePassword.set(bundletoolExtension.signingConfig.storePassword)
                it.signingConfigKeyAlias.set(bundletoolExtension.signingConfig.keyAlias)
                it.signingConfigKeyPassword.set(bundletoolExtension.signingConfig.keyPassword)
                it.destination.set(outFile)
            }
        }
    }
}
