package com.appland.appmap;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;
import org.gradle.api.GradleException;
import org.gradle.api.Named;
import org.gradle.api.tasks.Internal;
import org.gradle.api.tasks.Nested;
import org.gradle.api.tasks.Optional;
import org.gradle.process.CommandLineArgumentProvider;
import org.gradle.util.RelativePathUtil;

import java.util.List;
import java.util.logging.Logger;

import javax.annotation.Nullable;
import java.util.Collections;

public class AppmapAgentCommandLineProvider implements CommandLineArgumentProvider, Named {
    private static final Logger LOGGER = java.util.logging.Logger.getLogger("com.appland.appmap");

    private final AppmapAgentExtension appmap;

    public AppmapAgentCommandLineProvider(AppmapAgentExtension appmap) {
        this.appmap = appmap;
    }

    @Nullable
    @Optional
    @Nested
    public AppmapAgentExtension getAppMapAgent() {
        return appmap.isSkip() ? appmap : null;
    }

    @Override
    public Iterable<String> asArguments() {
        Iterable<String> response = null;
        if (appmap.isSkip()) {
            response = getAsJvmArg();
            LOGGER.info("argLine set to " + Joiner.on(",").join(response));
        } else {
            response = Collections.emptyList();
        }
        return response;
    }

    @Internal
    @Override
    public String getName() {
        return "appmapAgent";
    }

    @Internal
    public List getAsJvmArg() {

        if (!appmap.isConfigFileValid()) {
            appmap.setSkip(false);
            throw new GradleException("Configuration file must exist and be readable: "
                    + appmap.getConfigFile().get().getAsFile().getPath());
        }
        if (!appmap.isSkip()) {
            StringBuilder builder = new StringBuilder();
            builder.append("-javaagent:");
            builder.append(RelativePathUtil.relativePath(appmap.getTask().getWorkingDir(), appmap.getAgentConf().getSingleFile()));
            return ImmutableList.of(
                    builder.toString(),
                    "-Dappmap.config.file=" + appmap.getConfigFile().get().toString(),
                    "-Dappmap.output.directory=" + appmap.getOutputDirectory().get().toString(),
                    "-Dappmap.event.valueSize=" + appmap.getEventValueSize(),
                    "-Dappmap.debug=" + appmap.getDebug()
            );
        } else {
            LOGGER.info("Appmap plugin is disable, skip property set to " + appmap.isSkip());
            return Collections.EMPTY_LIST;
        }
    }
}