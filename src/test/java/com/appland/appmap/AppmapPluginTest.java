package com.appland.appmap;

import static org.assertj.core.api.Assertions.assertThat;

import org.gradle.api.Project;
import org.gradle.testfixtures.ProjectBuilder;
import org.junit.Test;


public class AppmapPluginTest {
  @Test
  public void test() {
    // Given
    Project project = ProjectBuilder.builder().build();

    // When
    project.getPlugins().apply("com.appland.appmap");

    // Then
   // assertThat(project.getTasks().findByName("appmap")).isNotNull();
  }
}