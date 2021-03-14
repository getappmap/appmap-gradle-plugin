package com.appland.appmap

import org.gradle.api.Plugin
import org.gradle.api.Project

open class AppmapPluginExtension {
    var message: String? = "default"
    var greeter: String? = "salutation"
}

class AppmapPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        val extension: AppmapPluginExtension = project.extensions
            .create("appmap", AppmapPluginExtension::class.java)

        project.task("appmap") {
            apply {
                println("${extension.greeter} from ${extension.message}")
            }
        }
    }
}