# AppMap Gradle plugin

- [AppMap Gradle plugin](#appmap-gradle-plugin)
  - [Quickstart](#quickstart)
  - [About](#about)
  - [Plugin Tasks](#plugin-tasks)
  - [Plugin configuration](#plugin-configuration)
  - [Troubleshooting](#troubleshooting)

## Quickstart

First, ensure you have a
[properly configured `appmap.yml`](https://github.com/applandinc/appmap-java#configuration)
in your root project directory. A basic configuration may look like:

```yml
# appmap.yml
name: my_organization/my_application

packages:
  # List the packages you'd like to record here.
  - path: com.myorganization.myapplication

  # Individual classes or methods can be chosen as well.
  #
  # - path: com.myorganization.myapplication.MyClass
  # - path: com.myorganization.myapplication.MyClass#myInstanceMethod
  # - path: com.myorganization.myapplication.MyClass.myStaticMethod
  #
  # Optionally, include paths of packages, classes
  # or methods that you'd like to exclude from recording
  #
  # - exclude: com.myorganization.myapplication.util
  # - exclude: com.myorganization.myapplication.MyClass
  # - exclude: com.myorganization.myapplication.MyClass#myInstanceMethod
  # - exclude: com.myorganization.myapplication.MyClass.myStaticMethod
```

Next, add the following plugin definition and configuration to your `build.gradle`:

1. Add `com.appland.appmap` to plugins
    ```groovy
     plugins {
      // other plugins here

      id 'com.appland.appmap' version '1.0.1'
     }
    ```
2. Make sure the Maven repository is listed in repositories. Example:
    ``` groovy
     repositories {
          mavenCentral()
     }
    ```
3. Add appmap configuration
    ```groovy
    // Set up AppMap agent, default parameters
    appmap {
        configFile = file("$projectDir/appmap.yml")
        outputDirectory = file("$buildDir/appmap")
        skip = false
        debug = "info"
        debugFile = file("$buildDir/appmap/agent.log")
        eventValueSize = 1024
    }
    ```
    
That's all! The AppMap agent will automatically record your tests when you run
`gradle appmap test`. By default, AppMap files are output to `$buildDir/appmap`.

Using Visual Studio Code?
[Download the AppMap extension](https://marketplace.visualstudio.com/items?itemName=appland.appmap)
to view AppMap files in your IDE.

## About

The AppMap Gradle Plugin provides simple method for recording AppMaps in running
tests in Gradle projects, and a seamless integration into CI/CD pipelines. The
recording agent requires `appmap.yml` configuration file, see
[appmap-java](https://github.com/applandinc/appmap-java/blob/master/README.md)
for details.

## Plugin Tasks

- `appmap` - adds the AppMap Java agent to the JVM, must be explicitly called
- `validate-config` - Validates if appmap.yml file exist, and it's readable

## Plugin configuration

- `configFile` Path to the `appmap.yml` config file. Default: _./appmap.yml_
- `outputDirectory` Output directory for `.appmap.json` files. Default:
  _.$buildDir/appmap_
- `skip` Agent won't record tests when set to true. Default: _false_
- `debug` Enable debug flags as a comma separated list. Accepts: `info`,
  `hooks`, `http`, `locals` Default: _info_
- `debugFile` Specify where to output debug logs. Default:
  _$buildDir/appmap/agent.log_
- `eventValueSize` Specifies the length of a value string before truncation
  occurs. If set to 0, truncation is disabled. Default: _1024_

## Troubleshooting

**I have no `$buildDir/appmap` directory**  
It's likely that the agent is not running. Double check the `appmap` task is
being explicitly called and if the JVM is being forked at any point, make sure
the `javaagent` argument is being propagated to the new process.

**`*.appmap.json` files are present, but appear empty or contain little data**  
Double check your `appmap.yml`. This usually indicates that the agent is
functioning as expected, but no classes or methods referenced in the
`appmap.yml` configuration are being executed. You may need to adjust the
packages being recorded. Follow this link for more information:
https://github.com/applandinc/appmap-java#configuration

**My tests aren't running, or I'm seeing
`The forked VM terminated without properly saying goodbye.`**  
Check the agent log (defaults to `target/appmap/agent.log`) This is typically
indicative of an invalid `appmap.yml` configuration.

**I have a test failure that only occurs while the agent is attached**  
Please open an issue at
[applandinc/appmap-java](https://github.com/applandinc/appmap-java/issues).
Attach a link to the source code or repository (if available), as well as any
other relevant information including:

- the contents of `appmap.yml`
- the run command used (such as `gradle appmap test`)
- output of the run command
