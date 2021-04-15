package com.appland.appmap;

import org.gradle.api.Action;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.logging.Logger;
import org.gradle.api.logging.Logging;
import org.gradle.api.tasks.testing.Test;
import org.gradle.process.JavaForkOptions;

/**
 * This action if called sets a JvmArgumentProvider to the "test" task of the build
 */
public class LoadAppmapAgentAction implements Action<Task> {
    private static final Logger LOGGER = Logging.getLogger(LoadAppmapAgentAction.class);

    private final Project project;
    private final AppmapPluginExtension extension;

    public LoadAppmapAgentAction(Project project, AppmapPluginExtension extension) {
        this.project = project;
        this.extension = extension;
    }

    @Override
    public void execute(Task task) {
        project.getTasks().withType(Test.class).configureEach(this::applyTo);
    }

    private <T extends Task & JavaForkOptions> void applyTo(final T task) {
        if (task instanceof AppmapTask) return;
        final String taskName = task.getName();
        LOGGER.lifecycle("Attaching Appmap Agent to task: " + taskName);
        task.getJvmArgumentProviders().add(new AgentCommandLineLoader(extension));
        //TODO: set a do first task that deletes the directory
       /* task.doFirst(new CleanAppmapDirectoryAction(
                fs,
                providers.provider(extension::getOutputDirectoryAsString)
        ));*/
    }

}