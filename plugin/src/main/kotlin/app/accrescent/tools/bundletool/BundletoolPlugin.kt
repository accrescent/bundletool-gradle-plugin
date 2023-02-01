package app.accrescent.tools.bundletool

import org.gradle.api.Project
import org.gradle.api.Plugin

class BundletoolPlugin: Plugin<Project> {
    override fun apply(project: Project) {
        project.tasks.register("greeting") { task ->
            task.doLast {
                println("Hello from plugin 'app.accrescent.tools.bundletool.greeting'")
            }
        }
    }
}
