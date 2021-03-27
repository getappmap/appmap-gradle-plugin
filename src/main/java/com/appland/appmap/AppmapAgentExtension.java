package com.appland.appmap;

import org.gradle.api.Project;
import org.gradle.api.artifacts.Configuration;
import org.gradle.api.file.ProjectLayout;
import org.gradle.api.tasks.Input;
import org.gradle.process.JavaForkOptions;

public class AppmapAgentExtension {

    protected final Project project;
    private final Configuration agentConf;
    private final JavaForkOptions task;
    private final ProjectLayout layout;
    private boolean enabled = true;
    private String configFile = "appmap.yml";
    private String outputDirectory = "tmp/appmap";
    private boolean skip = false;
    private String debug = "info"; //Enable debug flags as a comma separated list. Accepts: info, hooks, http, locals Default: info
    private String debugFile = "target/appmap/agent.log";
    private int eventValueSize = 1024; //Specifies the length of a value string before truncation. If 0, truncation is disabled.

    public AppmapAgentExtension(Project project, Configuration agentConf, JavaForkOptions task) {
        this.project = project;
        this.agentConf = agentConf;
        this.task = task;
        this.layout = project.getLayout();

    }

    public Configuration getAgentConf() {
        return agentConf;
    }

    public boolean isSkip() {
        return skip;
    }

    public void setSkip(boolean skip) {
        this.skip = skip;
    }

    public String getOutputDirectory() {
        return outputDirectory;
    }

    public void setOutputDirectory(String outputDirectory) {
        this.outputDirectory = outputDirectory;
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

    public String getConfigFile() {
        return configFile;
    }

    public void setConfigFile(String configFile) {
        this.configFile = configFile;
    }

    @Input
    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public JavaForkOptions getTask() {
        return task;
    }
}
