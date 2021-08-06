package com.appland.appmap.gradle;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.artifacts.Configuration;
import org.gradle.api.plugins.JavaPlugin;
import org.gradle.api.plugins.ReportingBasePlugin;
import org.gradle.language.base.plugins.LifecycleBasePlugin;

/**
 * The actual plugin definition for appmap plugin.
 */
public class AppMapPlugin implements Plugin<Project> {
  public static final String DEFAULT_AGENT_VERSION = "[1.3, 2.0)";
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
    project.getPlugins().withType(JavaPlugin.class, javaPlugin -> addAppMapTasks(extension));
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
          prepareAgentTask.doLast(new LoadAppMapAgentAction(project, extension));
          prepareAgentTask.setGroup(LifecycleBasePlugin.BUILD_GROUP);
          prepareAgentTask.setDescription(
              String.format("Injects AppMap Agent JVM settings to the 'test' task")
          );
        });

    project.getTasks().register(
        "appmap-validate-config",
        validateConfigTask -> {
          validateConfigTask.doFirst(
              new ValidateConfigAction(extension.getConfigFile().getAsFile())
          );
          validateConfigTask.setGroup(LifecycleBasePlugin.BUILD_GROUP);
          validateConfigTask.setDescription(
              String.format("Validates the AppMap Agent configuration")
          );
        }
    );

      project.getTasks().register(
          "appmap-print-jar-path",
          agentJarPathTask -> {
              agentJarPathTask.doFirst(
                  new PrintJarPathAction(extension)
              );
              agentJarPathTask.setGroup(LifecycleBasePlugin.BUILD_GROUP);
              agentJarPathTask.setDescription(
                  String.format("Prints the file path of the AppMap Agent JAR")
              );
          }
      );
  }
}
