package com.appland.appmap.gradle;

import org.gradle.api.Action;
import org.gradle.api.Task;

import java.util.logging.Logger;

public class PrintJarPathAction implements Action<Task>  {
    private final Logger logger = Logger.getLogger("com.appland.appmap.gradle");

    private final AppMapPluginExtension extension;

    public PrintJarPathAction(AppMapPluginExtension extension) {
        this.extension = extension;
    }

    @Override
    public void execute(Task task) {
        System.out.println("java.home=" + System.getProperty("java.home"));
        System.out.println("com.appland:appmap-agent.jar.path=" + extension.project.getProjectDir().toPath().resolve(extension.getAgentConf().getSingleFile().toPath()).toAbsolutePath());
    }
}

