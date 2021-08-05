package com.appland.appmap;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.artifacts.Configuration;
import org.gradle.api.file.FileSystemOperations;
import org.gradle.api.internal.project.ProjectInternal;
import org.gradle.api.plugins.JavaPlugin;
import org.gradle.api.plugins.ReportingBasePlugin;
import org.gradle.language.base.plugins.LifecycleBasePlugin;

/**
 * The actual plugin definition for appmap plugin.
 */
public class AppMapPlugin implements Plugin<Project> {

  public static final String DEFAULT_AGENT_VERSION = "latest.release";
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
    config.setDescription("AppMap agent to generate app map data.");
    config.defaultDependencies(dependencies ->
        dependencies.add(
            project.getDependencies().create("com.appland:appmap-agent:" + DEFAULT_AGENT_VERSION)
        )
    );
    AppMapPluginExtension extension = project.getExtensions()
        .create(PLUGIN_EXTENSION_NAME, AppMapPluginExtension.class, project, config);
    //extension.setAgentVersion(DEFAULT_AGENT_VERSION);*/
    addAppMapGradleTasks(extension);
  }

  private void registerAgentConfiguration() {
    Configuration agentConf = project.getConfigurations().create(AGENT_CONFIGURATION_NAME);
    agentConf.setVisible(false);
    agentConf.setTransitive(true);
    agentConf.setDescription("AppMap agent to generate app map data.");
  }

  private void addAppMapGradleTasks(AppMapPluginExtension extension) {
    project.getPlugins().withType(JavaPlugin.class, javaPlugin -> {
      addAppMapTasks(extension);
    });
  }

  /**
   * This is the central method of the app map plugin, here the task gets registered, the load
   * appmap agent action gets associated to it, and the configuration is set.
   *
   * @param extension holds the config parameters for the plugin.
   */
  private void addAppMapTasks(AppMapPluginExtension extension) {
    project.getTasks().register(
        "appmap",
        AppMapTask.class,
        prepareAgentTask -> {
          prepareAgentTask.doFirst(
              new ValidateConfigAction(extension.getConfigFile().getAsFile())
          );
          prepareAgentTask.doLast(
              new CleanOutputDirectoryAction(
                  ((ProjectInternal) project).getServices().get(FileSystemOperations.class),
                  extension.getOutputDirectory().getAsFile())
          );
          prepareAgentTask.doLast(new LoadAppMapAgentAction(project, extension));
          prepareAgentTask.setGroup(LifecycleBasePlugin.BUILD_GROUP);
          prepareAgentTask.setDescription(
              String.format("Attaches AppMap Agent to the Test task")
          );
        });

    project.getTasks().register(
        "validate-config",
        validateConfigTask -> {
          validateConfigTask.doFirst(
              new ValidateConfigAction(extension.getConfigFile().getAsFile())
          );
          validateConfigTask.setGroup(LifecycleBasePlugin.BUILD_GROUP);
          validateConfigTask.setDescription(
              String.format("Searches AppMap Agent config file and validates it")
          );
        }
    );
  }
}
