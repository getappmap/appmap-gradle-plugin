package com.appland.appmap;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.artifacts.Configuration;
import org.gradle.api.plugins.JavaPlugin;
import org.gradle.api.plugins.ReportingBasePlugin;
import org.gradle.language.base.plugins.LifecycleBasePlugin;

/**
 * The actual plugin definition for appmap plugin.
 */
public class AppmapPlugin implements Plugin<Project> {

    public static final String DEFAULT_AGENT_VERSION = "latest";
    public static final String AGENT_CONFIGURATION_NAME = "appmapAgent";
    public static final String PLUGIN_EXTENSION_NAME = "appmap";
    private Project project;

    @Override
    public void apply(Project project) {
        project.getPluginManager().apply(ReportingBasePlugin.class);
        this.project = project;
        registerAgentConfiguration();
        final Configuration config = project.getConfigurations().getAt(AGENT_CONFIGURATION_NAME);
        config.setVisible(false);
        config.setTransitive(true);
        config.setDescription("Appmap agent to generate app map data.");
        config.defaultDependencies(dependencies ->
                dependencies.add(
                        project.getDependencies().create("com.appland:appmap-agent:1.0.3")// + extension.getToolVersion())
                )
        );
        AppmapPluginExtension extension = project.getExtensions().create(PLUGIN_EXTENSION_NAME, AppmapPluginExtension.class, project, config);
        //extension.setAgentVersion(DEFAULT_AGENT_VERSION);*/
        addAppmapGradleTasks(extension);
    }

    private void registerAgentConfiguration() {
        Configuration agentConf = project.getConfigurations().create(AGENT_CONFIGURATION_NAME);
        agentConf.setVisible(false);
        agentConf.setTransitive(true);
        agentConf.setDescription("Appmap agent to generate app map data.");
    }


    private void addAppmapGradleTasks(AppmapPluginExtension extension) {
        project.getPlugins().withType(JavaPlugin.class, javaPlugin -> {
            addAppmapTask(extension);
        });
    }

    /**
     * This is the central method of the app map plugin, here the task gets registered, the load appmap agent action
     * gets associated to it, and the configuration is set.
     *
     * @param extension
     */
    private void addAppmapTask(AppmapPluginExtension extension) {
        project.getTasks().register(
                "appmap",
                AppmapTask.class,
                prepareAgentTask -> {
                    prepareAgentTask.doFirst(new LoadAppmapAgentAction(project, extension));
                    prepareAgentTask.setGroup(LifecycleBasePlugin.BUILD_GROUP);
                    prepareAgentTask.setDescription(
                            String.format("Attaches Appmap Agent to the Test task")
                    );
                });
    }

}