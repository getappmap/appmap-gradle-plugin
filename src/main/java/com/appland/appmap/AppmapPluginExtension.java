package com.appland.appmap;

import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.artifacts.Configuration;
import org.gradle.api.file.FileSystemOperations;
import org.gradle.api.internal.project.ProjectInternal;
import org.gradle.api.logging.Logger;
import org.gradle.api.logging.Logging;
import org.gradle.api.provider.ProviderFactory;
import org.gradle.process.JavaForkOptions;

public class AppmapPluginExtension {

    public static final String TASK_EXTENSION_NAME = "appmap";

    private static final Logger LOGGER = Logging.getLogger(AppmapPluginExtension.class);

    protected final Project project;

    private final ProviderFactory providers;
    private final FileSystemOperations fs;
    private final Configuration agentConf;

    private String agentVersion;

    public AppmapPluginExtension(Project project, Configuration agentConf) {
        this.project = project;
        this.agentConf = agentConf;
        this.providers = project.getProviders();
        this.fs = ((ProjectInternal) project).getServices().get(FileSystemOperations.class);
    }

    public String getAgentVersion() {
        return agentVersion;
    }

    public void setAgentVersion(String agentVersion) {
        this.agentVersion = agentVersion;
    }

    public <T extends Task & JavaForkOptions> void applyTo(final T task) {
        final String taskName = task.getName();
        LOGGER.debug("Applying Appmap to " + taskName);
        final AppmapAgentExtension extension = project.getExtensions()
                .create(TASK_EXTENSION_NAME, AppmapAgentExtension.class, project, agentConf, task);
        //TODO: correctly set the directory to delete
        task.getJvmArgumentProviders().add(new AppmapAgentCommandLineProvider(extension));
        task.doFirst(new CleanAppmapDirectoryAction(
                fs,
                providers.provider(() -> extension.isEnabled()),
                providers.provider(extension::getOutputDirectory)
        ));
    }

}