package com.appland.appmap

import org.gradle.api.Project
import org.junit.Assert.*
import org.junit.Test
import org.gradle.testfixtures.*

class AppmapPluginTest {

    @Test
    fun apply() {
        val project: Project = ProjectBuilder.builder().build()
        project.pluginManager.apply("com.appland.appmap-plugin")
        assertTrue(project.pluginManager.hasPlugin("com.appland.appmap-plugin"))
        assertNotNull(project.tasks.getByName("appmap"))
    }
}