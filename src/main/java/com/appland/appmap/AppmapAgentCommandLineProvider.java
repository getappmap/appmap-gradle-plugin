package com.appland.appmap;

import com.google.common.collect.ImmutableList;
import org.gradle.api.Named;
import org.gradle.api.tasks.Internal;
import org.gradle.api.tasks.Nested;
import org.gradle.api.tasks.Optional;
import org.gradle.process.CommandLineArgumentProvider;
import org.gradle.util.RelativePathUtil;

import javax.annotation.Nullable;
import java.io.File;
import java.util.Collections;

public class AppmapAgentCommandLineProvider implements CommandLineArgumentProvider, Named {

    private final AppmapAgentExtension appmap;

    public AppmapAgentCommandLineProvider(AppmapAgentExtension appmap) {
        this.appmap = appmap;
    }

    @Nullable
    @Optional
    @Nested
    public AppmapAgentExtension getAppMapAgent() {
        return appmap.isEnabled() ? appmap : null;
    }

    @Override
    public Iterable<String> asArguments() {
        return appmap.isEnabled() ? getAsJvmArg() : Collections.emptyList();
    }

    @Internal
    @Override
    public String getName() {
        return "appmapAgent";
    }

    @Internal
    public ImmutableList getAsJvmArg() {
        if (appmap.isEnabled()) {
            StringBuilder builder = new StringBuilder();
            builder.append("-javaagent:");
            builder.append(RelativePathUtil.relativePath(appmap.getTask().getWorkingDir(), appmap.getAgentConf().getSingleFile()));
            return ImmutableList.of(
                    builder.toString(),
                    "-Dappmap.config.file=" + appmap.getTask().getWorkingDir() + File.separator + appmap.getConfigFile(),
                    "-Dappmap.output.directory=" + appmap.getOutputDirectory(),
                    "-Dappmap.event.valueSize=1024",
                    "-Dappmap.debug=true"
            );
        } else {
            return (ImmutableList) Collections.EMPTY_LIST;
        }
    }
}