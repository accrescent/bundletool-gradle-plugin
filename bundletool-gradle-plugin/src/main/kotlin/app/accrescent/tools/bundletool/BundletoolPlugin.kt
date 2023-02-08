package app.accrescent.tools.bundletool

import com.android.build.api.artifact.SingleArtifact
import com.android.build.api.variant.AndroidComponentsExtension
import com.android.build.api.variant.impl.ApplicationVariantImpl
import com.android.build.gradle.internal.plugins.AppPlugin
import com.android.build.gradle.internal.plugins.BasePlugin
import com.android.build.gradle.internal.services.VersionedSdkLoaderService
import com.android.build.gradle.internal.signing.SigningConfigDataProvider
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.configurationcache.extensions.capitalized

class BundletoolPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        val androidComponents = project.extensions.getByType(AndroidComponentsExtension::class.java)

        androidComponents.onVariants { variant ->
            val variantName = variant.name.capitalized()
            val bundle = variant.artifacts.get(SingleArtifact.BUNDLE)
            val signingConfigDataProvider = SigningConfigDataProvider.create(variant as ApplicationVariantImpl)

            val buildToolInfo = run {
                // Forgive me
                val plugin = project.plugins.getPlugin(AppPlugin::class.java)
                val field = BasePlugin::class.java
                    .declaredFields
                    .filter { it.name == "versionedSdkLoaderService\$delegate" }[0]
                    .apply { isAccessible = true }
                val sdkLoaderService = (field.get(plugin) as Lazy<*>).value as VersionedSdkLoaderService
                sdkLoaderService.versionedSdkLoader.get().buildToolInfoProvider
            }

            val outFile = project
                .layout
                .buildDirectory
                .file("outputs/apkset/${variant.name}/app-${variant.name}.apks")

            project.tasks.register("buildApks${variantName}", BundletoolTask::class.java) {
                it.bundleProperty.set(bundle)
                it.signingConfigDataProvider = signingConfigDataProvider
                it.buildToolInfo.set(buildToolInfo)
                it.destination.set(outFile)
            }
        }
    }
}
