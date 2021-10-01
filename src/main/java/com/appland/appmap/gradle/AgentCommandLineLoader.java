package com.appland.appmap.gradle;

import static java.lang.String.format;

import com.google.common.base.Joiner;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.lang.StringEscapeUtils;
import org.gradle.api.GradleException;
import org.gradle.api.Named;
import org.gradle.api.logging.Logger;
import org.gradle.api.logging.Logging;
import org.gradle.api.tasks.Internal;
import org.gradle.process.CommandLineArgumentProvider;
import org.gradle.util.RelativePathUtil;

/**
 * This class is the actual responsible of building the JVM args to run the AppMap Agent.
 */
public class AgentCommandLineLoader implements CommandLineArgumentProvider, Named {

  static final String DEFAULT_CONFIG_FILE = "appmap.yml";

  private static final Logger LOGGER = Logging.getLogger(CommandLineArgumentProvider.class);
  private static final List<String> DEBUG_FLAGS = Arrays.asList("debug", "hooks", "locals", "http");

  private final AppMapPluginExtension appmap;

  public AgentCommandLineLoader(AppMapPluginExtension appmap) {
    this.appmap = appmap;
  }

  @Override
  public Iterable<String> asArguments() {
    return getAsJvmArg();
  }

  @SuppressWarnings("NullableProblems")
  @Internal
  @Override
  public String getName() {
    return "appmapAgent";
  }

  /**
   * This method is called by the internal Compiler options to get the command needed to run at
   * command line.
   *
   * @return JVM args needed to run the appmap agent.
   */
  @Internal
  public List<String> getAsJvmArg() {
    if (!appmap.isConfigFileValid()) {
      appmap.setSkip(true);
      throw new GradleException("Configuration file must exist and be readable: "
          + appmap.getConfigFilePath());
    }
    if (appmap.shouldSkip()) {
      LOGGER.warn("AppMap task was executed but but is disabled, 'skip' property set to " + appmap
          .shouldSkip());
      return new ArrayList<>();
    } else {
      String javaAgentArg = "-javaagent:"
          + RelativePathUtil.relativePath(
          appmap.project.getProjectDir(), appmap.getAgentConf().getSingleFile()
      );

      List<String> argumentLn = new ArrayList<>();
      argumentLn.add(javaAgentArg);

      if ( appmap.getConfigFile().isPresent() ) {
        argumentLn.add("-Dappmap.config.file=" + appmap.getConfigFilePath());
      }
      argumentLn.add("-Dappmap.output.directory=" + appmap.getOutputDirectoryPath());
      argumentLn.add("-Dappmap.event.valueSize=" + appmap.getEventValueSize());
      argumentLn.addAll(buildDebugParams());
      LOGGER.lifecycle("Arguments line set to " + Joiner.on(",").join(argumentLn));
      return argumentLn;
    }
  }

  private List<String> buildDebugParams() {
    List<String> debugArgs = new ArrayList<>();
    if (appmap.getDebug() != null && !appmap.getDebug().isEmpty()) {
      final List<String> debugTokens = new ArrayList<>(
          Arrays.asList(appmap.getDebug().split("[,|\\s]")));

      boolean hasDebug = false;
      for (String token : debugTokens) {
        if (DEBUG_FLAGS.contains(token)) {
          hasDebug = true;
          if (token.equals("debug")) {
            debugArgs.add("-Dappmap.debug");
          } else {
            debugArgs.add("-Dappmap.debug." + token);
          }
        }
      }

      if (hasDebug) {
        debugArgs.add(0, "-Dappmap.debug.file=" + StringEscapeUtils
            .escapeJava(format("%s", appmap.getDebugFilePath())));
      }
    }
    return debugArgs;
  }
}