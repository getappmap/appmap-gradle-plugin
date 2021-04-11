package com.appland.appmap;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;
import org.gradle.api.GradleException;
import org.gradle.api.Named;
import org.gradle.api.logging.Logger;
import org.gradle.api.logging.Logging;
import org.gradle.api.tasks.Internal;
import org.gradle.process.CommandLineArgumentProvider;
import org.gradle.util.RelativePathUtil;

import java.util.ArrayList;
import java.util.List;

public class AgentCommandLineLoader implements CommandLineArgumentProvider, Named {
    private static final Logger LOGGER = Logging.getLogger(CommandLineArgumentProvider.class);

    private final AppmapPluginExtension appmap;

    public AgentCommandLineLoader(AppmapPluginExtension appmap) {
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

    @Internal
    public List<String> getAsJvmArg() {
        if (!appmap.isConfigFileValid()) {
            appmap.setSkip(true);
            throw new GradleException("Configuration file must exist and be readable: "
                    + appmap.getConfigFile().get().getAsFile().getPath());
        }
        if (appmap.shouldSkip()) {
            LOGGER.warn("Appmap task was executed but but is disable, skip property set to " + appmap.shouldSkip());
            return new ArrayList<>();
        } else {
            String builder = "-javaagent:" +
                    RelativePathUtil.relativePath(
                            appmap.project.getProjectDir(), appmap.getAgentConf().getSingleFile()
                    );
            List<String> argumentLn = ImmutableList.of(
                    builder,
                    "-Dappmap.config.file=" + appmap.getConfigFile().get().toString(),
                    "-Dappmap.output.directory=" + appmap.getOutputDirectory().get().toString(),
                    "-Dappmap.event.valueSize=" + appmap.getEventValueSize(),
                    "-Dappmap.debug=" + appmap.getDebug()
            );
            LOGGER.lifecycle("Arguments line set to " + Joiner.on(",").join(argumentLn));
            return argumentLn;
        }
    }
}