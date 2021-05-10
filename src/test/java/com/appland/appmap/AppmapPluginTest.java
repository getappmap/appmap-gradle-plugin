package com.appland.appmap;

import org.gradle.api.Project;
import org.gradle.testfixtures.ProjectBuilder;
import org.junit.jupiter.api.Test;


public class AppmapPluginTest {
  @Test
  public void test() {
    // Given
    Project project = ProjectBuilder.builder().build();

    // When
    project.getPlugins().apply("com.appland.appmap");

  }
}