package com.appland.appmap;

import java.io.File;
import org.gradle.api.Action;
import org.gradle.api.GradleException;
import org.gradle.api.Task;
import org.gradle.api.file.FileSystemOperations;
import org.gradle.api.provider.Provider;

/**
 * The action executed for cleaning the output directory before each run.
 */
public class CleanOutputDirectoryAction implements Action<Task> {

  private final FileSystemOperations fs;
  private final Provider<File> outputDirectory;

  public CleanOutputDirectoryAction(FileSystemOperations fs, Provider<File> outputDirectory) {
    this.fs = fs;
    this.outputDirectory = outputDirectory;
  }

  @Override
  public void execute(Task task) {
    File outputDirectoryFile = outputDirectory.get();
    if (outputDirectoryFile == null) {
      throw new GradleException(" destination file must not be null if output type is FILE");
    }
    if (fs.delete(spec -> spec.delete(outputDirectoryFile)).getDidWork()) {
      task.getLogger().info("AppMap output directory cleared");
    } else {
      task.getLogger().info("Output directory already cleared or is read only.");
    }
  }
}