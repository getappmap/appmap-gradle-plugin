package com.appland.appmap;

import org.gradle.api.Action;
import org.gradle.api.Task;
import org.gradle.api.file.FileSystemOperations;
import org.gradle.api.provider.Provider;

import java.io.File;

public class CleanAppmapDirectoryAction implements Action<Task> {
    private final FileSystemOperations fs;
    private final Provider<String> outputDirectory;

    public CleanAppmapDirectoryAction(FileSystemOperations fs, Provider<String> outputDirectory) {
        this.fs = fs;
        this.outputDirectory = outputDirectory;
    }

    @Override
    public void execute(Task task) {
            //TODO: borrar todo el directorio no el archivo.
            File coverageFile = new File(outputDirectory.get());
                /*if (coverageFile == null) {
                    throw new GradleException(" destination file must not be null if output type is FILE");
                }
                fs.delete(spec -> spec.delete(coverageFile));*/
    }
}