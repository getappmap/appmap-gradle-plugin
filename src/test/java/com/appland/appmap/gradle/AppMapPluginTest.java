package com.appland.appmap.gradle;

import org.gradle.api.Project;
import org.gradle.testfixtures.ProjectBuilder;
import org.junit.jupiter.api.Test;


public class AppMapPluginTest {
  @Test
  public void test() {
    // Given
    Project project = ProjectBuilder.builder().build();

    // When
    project.getPlugins().apply("com.appland.appmap");

  }
}