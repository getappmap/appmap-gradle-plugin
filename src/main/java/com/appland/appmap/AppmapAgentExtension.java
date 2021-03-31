package com.appland.appmap;

import org.gradle.api.GradleException;
import org.gradle.api.Project;
import org.gradle.api.artifacts.Configuration;
import org.gradle.api.file.DirectoryProperty;
import org.gradle.api.file.ProjectLayout;
import org.gradle.api.file.RegularFileProperty;
import org.gradle.api.tasks.Input;
import org.gradle.process.JavaForkOptions;

import java.io.File;
import java.nio.file.Files;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AppmapAgentExtension {

    public static final String DEFAULT_OUTPUT_DIRECTORY = "target/appmap";
    private final Logger LOGGER = Logger.getLogger("com.appland.appmap");
    protected final Project project;
    private final Configuration agentConf;
    private final JavaForkOptions task;
    private final ProjectLayout layout;
    private final RegularFileProperty configFile;
    private final DirectoryProperty outputDirectory;
    private boolean skip = false;
    private String debug = "info"; //Enable debug flags as a comma separated list. Accepts: info, hooks, http, locals Default: info
    private String debugFile = "target/appmap/agent.log";
    private int eventValueSize = 1024; //Specifies the length of a value string before truncation. If 0, truncation is disabled.

    public AppmapAgentExtension(Project project, Configuration agentConf, JavaForkOptions task) {
        this.project = project;
        this.agentConf = agentConf;
        this.task = task;
        this.layout = project.getLayout();
        this.configFile = project.getObjects().fileProperty().fileValue(new File("appmap.yml"));
        this.outputDirectory = project.getObjects().directoryProperty().fileValue(new File(DEFAULT_OUTPUT_DIRECTORY));
        try {
            LOGGER.setLevel(Level.parse(debug.toUpperCase()));
        } catch (Exception e) {
            throw new GradleException("Debug level is not recognize: " + debug);
        }
        LOGGER.info( "Appmap Plugin Initialized.");
    }

    public Configuration getAgentConf() {
        return agentConf;
    }

    @Input
    public boolean isSkip() {
        return skip;
    }

    public void setSkip(boolean skip) {
        this.skip = skip;
    }

    public String getDebug() {
        return debug;
    }

    public void setDebug(String debug) {
        this.debug = debug;
    }

    public String getDebugFile() {
        return debugFile;
    }

    public void setDebugFile(String debugFile) {
        this.debugFile = debugFile;
    }

    public int getEventValueSize() {
        return eventValueSize;
    }

    public void setEventValueSize(int eventValueSize) {
        this.eventValueSize = eventValueSize;
    }

    public JavaForkOptions getTask() {
        return task;
    }

    public DirectoryProperty getOutputDirectory() {
        return outputDirectory;
    }

    public String getOutputDirectoryAsString() {
        return outputDirectory.toString();
    }

    public RegularFileProperty getConfigFile() {
        return configFile;
    }

    public boolean isConfigFileValid() {
        return configFile.get().getAsFile().exists() && Files.isReadable(configFile.get().getAsFile().toPath());
    }
}