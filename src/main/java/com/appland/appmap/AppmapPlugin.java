package com.appland.appmap;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.artifacts.Configuration;
import org.gradle.api.internal.project.ProjectInternal;
import org.gradle.api.plugins.JavaPlugin;
import org.gradle.api.plugins.ReportingBasePlugin;
import org.gradle.api.tasks.TaskProvider;
import org.gradle.api.tasks.testing.Test;
import org.gradle.internal.reflect.Instantiator;

import javax.inject.Inject;

public class AppmapPlugin implements Plugin<Project> {

    public static final String DEFAULT_AGENT_VERSION = "latest";
    public static final String AGENT_CONFIGURATION_NAME = "appmapAgent";
    public static final String PLUGIN_EXTENSION_NAME = "appmapP";
    private final Instantiator instantiator;
    private Project project;

    @Inject
    public AppmapPlugin(Instantiator instantiator) {
        this.instantiator = instantiator;
    }

    @Override
    public void apply(Project project) {
        project.getPluginManager().apply(ReportingBasePlugin.class);
        this.project = project;
        createConfigurations();
        final Configuration config = project.getConfigurations().getAt(AGENT_CONFIGURATION_NAME);
        AppmapPluginExtension extension = project.getExtensions().create(PLUGIN_EXTENSION_NAME, AppmapPluginExtension.class, project,  config);
        extension.setAgentVersion(DEFAULT_AGENT_VERSION);
        configureAgentDependencies(config, extension);
        applyToDefaultTasks(extension);
        addAppmapGradleTasks(extension);
    }

    private void createConfigurations() {
        Configuration agentConf = project.getConfigurations().create(AGENT_CONFIGURATION_NAME);
        agentConf.setVisible(false);
        agentConf.setTransitive(true);
        agentConf.setDescription("Appmap agent to generate app map data.");
    }

    private void configureAgentDependencies(Configuration agentConf, final AppmapPluginExtension extension) {
        agentConf.defaultDependencies(dependencies ->
                dependencies.add(
                        project.getDependencies().create("com.appland:appmap-agent:1.0.3")// + extension.getToolVersion())
                )
        );
    }

    private void applyToDefaultTasks(final AppmapPluginExtension extension)  {
        project.getTasks().withType(Test.class).configureEach(extension::applyTo);
    }


    private void addAppmapGradleTasks(final AppmapPluginExtension extension) {
        project.getPlugins().withType(JavaPlugin.class, javaPlugin -> {
            TaskProvider<Task> testTaskProvider = project.getTasks().named(JavaPlugin.TEST_TASK_NAME);
            addDefaultReportTask(extension, testTaskProvider);
        });
    }

    private void addDefaultReportTask(final AppmapPluginExtension extension, final TaskProvider<Task> testTaskProvider) {
        final String testTaskName = testTaskProvider.getName();
       /* project.getTasks().register(
                "prepare-agent",
                AbstractTestTask.class,
                reportTask -> {
                    reportTask.setGroup(LifecycleBasePlugin.BUILD_TASK_NAME);
                    reportTask.setDescription(String.format("Generates mapping data for the Appmap plugin gather in the %s task.", testTaskName));
                });*/
    }

}