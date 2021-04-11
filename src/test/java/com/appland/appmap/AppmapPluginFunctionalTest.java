package com.appland.appmap;

import org.gradle.testkit.runner.BuildResult;
import org.gradle.testkit.runner.GradleRunner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static org.gradle.testkit.runner.TaskOutcome.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AppmapPluginFunctionalTest {

    @TempDir File testProjectDir;
    private File settingsFile;
    private File buildFile;
    private File appmapConfigFile;

    @BeforeEach
    public void setup() {
        settingsFile = new File(testProjectDir, "settings.gradle");
        buildFile = new File(testProjectDir, "build.gradle");
        appmapConfigFile = new File(testProjectDir, "appmap.yml");
    }

    @Test
    public void testValidateConfigGoalFailsOnNonExistentFile() throws IOException {
        writeFile(settingsFile, SETTINGS_GRADLE_CONTENT);
        writeFile(buildFile, BUILD_FILE_CONTENT);

        BuildResult result = GradleRunner.create()
                .withProjectDir(testProjectDir)
                .withArguments("validate-config")
                .buildAndFail();

        assertTrue(result.getOutput().contains("not found or not readable."));
        assertEquals(FAILED, result.task(":validate-config").getOutcome());
    }

    @Test
    public void testValidateConfigGoalSucceed() throws IOException {
        writeFile(settingsFile, SETTINGS_GRADLE_CONTENT);
        writeFile(buildFile, BUILD_FILE_CONTENT);
        writeFile(appmapConfigFile, APPMAP_CONFIGFILE_CONTENT);

        BuildResult result = GradleRunner.create()
                .withProjectDir(testProjectDir)
                .withArguments("validate-config")
                .build();

        assertTrue(result.getOutput().contains("BUILD SUCCESSFUL"));
        assertEquals(SUCCESS, result.task(":validate-config").getOutcome());
    }

    @Test
    public void testAppmapGoalSucceed() throws IOException {
        writeFile(settingsFile, SETTINGS_GRADLE_CONTENT);
        writeFile(buildFile, BUILD_FILE_CONTENT);
        writeFile(appmapConfigFile, APPMAP_CONFIGFILE_CONTENT);

        BuildResult result = GradleRunner.create()
                .withProjectDir(testProjectDir)
                .withArguments("appmap")
                .build();

        assertTrue(result.getOutput().contains("BUILD SUCCESSFUL"));
        assertEquals(NO_SOURCE, result.task(":appmap").getOutcome());
    }

    private void writeFile(File destination, String content) throws IOException {
        BufferedWriter output = null;
        try {
            output = new BufferedWriter(new FileWriter(destination));
            output.write(content);
        } finally {
            if (output != null) {
                output.close();
            }
        }
    }

    public static final String SETTINGS_GRADLE_CONTENT = "rootProject.name = 'com.appland.testproject'";

    public static final String APPMAP_CONFIGFILE_CONTENT = "name: domain\npackages:\n  - path: lead";

    public static final String BUILD_FILE_CONTENT = "buildscript {\n" +
            "    repositories {\n" +
            "        mavenLocal()\n" +
            "    }\n" +
            "    dependencies {\n" +
            "        classpath \"com.appland:appmap-gradle-plugin:1.0.3\"\n" +
            "    }\n" +
            "}\n" +
            "\n" +
            "plugins {\n" +
            "    id 'org.jetbrains.kotlin.jvm' version '1.4.31'\n" +
            "}\n" +
            "\n" +
            "group 'com.appland.testproject'\n" +
            "version '1.0-SNAPSHOT'\n" +
            "\n" +
            "repositories {\n" +
            "    mavenCentral()\n" +
            "}\n" +
            "\n" +
            "apply plugin: 'com.appland.appmap'\n" +
            "\n" +
            "appmap {\n" +
            "    configFile = file(\"$projectDir/appmap.yml\")\n" +
            "    outputDirectory = file(\"$projectDir/build/appmap3\")\n" +
            "    skip = false\n" +
            "    debug = \"info\"\n" +
            "    debugFile = \"build/appmap/agent.log\"\n" +
            "    eventValueSize = 1024\n" +
            "}";

}