package com.appland.appmap.gradle;

import org.gradle.api.Action;
import org.gradle.api.GradleException;
import org.gradle.api.Task;
import org.gradle.api.file.RegularFile;
import org.gradle.api.file.RegularFileProperty;
import org.gradle.api.provider.Provider;

/**
 * Action to validates the configuration for appmap exists and is readable.
 */
public class ValidateConfigAction implements Action<Task> {

  private final Provider<RegularFile> configFile;

  public ValidateConfigAction(RegularFileProperty regularFileProperty) {
    this.configFile = regularFileProperty;
  }

  @Override
  public void execute(Task task) {
    if (!isConfigFileValid()) {
      throw new GradleException(
          "Config file " + configFile.get().getAsFile().getPath() + " not found or not readable."
      );
    }
  }

  protected boolean isConfigFileValid() {
    return AppMapPluginExtension.isConfigFileValid(configFile.getOrNull());
  }
}
