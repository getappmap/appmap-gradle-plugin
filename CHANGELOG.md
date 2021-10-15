## [1.1.1](https://github.com/applandinc/appmap-gradle-plugin/compare/v1.1.0...v1.1.1) (2021-10-15)


### Bug Fixes

* Add backward compatibility for Gradle 5.0 ([5fad30c](https://github.com/applandinc/appmap-gradle-plugin/commit/5fad30c377583c5d8063511bc76d07df1f6d3137))

# [1.1.0](https://github.com/applandinc/appmap-gradle-plugin/compare/v1.0.2...v1.1.0) (2021-08-09)


### Bug Fixes

* Don't clean the output directory on build ([3cf48c3](https://github.com/applandinc/appmap-gradle-plugin/commit/3cf48c33fef1bfb88fb4608c2b278d8f184d75b1))
* gsub Appmap -> AppMap ([9286a9f](https://github.com/applandinc/appmap-gradle-plugin/commit/9286a9f416d9f8ac3985516b5a41c113c9f8687e))


### Features

* Add task to print the appmap jar file path and java.home ([7e0cefe](https://github.com/applandinc/appmap-gradle-plugin/commit/7e0cefedf0c4227d9113f7f49392352eb61335c9))
* Agent version ranges from 1.3 to 2.0 (not inclusive) ([e32f238](https://github.com/applandinc/appmap-gradle-plugin/commit/e32f238c18c02d9ada157f74f1496b8e59f4eae5))
* Apply version constraint [1.0, 2.0) to appmap-agent ([ceec53c](https://github.com/applandinc/appmap-gradle-plugin/commit/ceec53ca003f03633cff34a6facd2d8a86f580fd))
* Rename 'validate-config' task to 'appmap-validate-config' ([d4418ac](https://github.com/applandinc/appmap-gradle-plugin/commit/d4418acbcaa50d6c7767454546a1dee7b6004f52))
* Update behavior of debug flag ([d6e9019](https://github.com/applandinc/appmap-gradle-plugin/commit/d6e9019a2eb505c0acf01b4ae62534dac6619b71))

## [1.0.2](https://github.com/applandinc/appmap-gradle-plugin/compare/v1.0.1...v1.0.2) (2021-05-20)


### Bug Fixes

* to be able to config gradle plugin from a kotlin .kts script ([#10](https://github.com/applandinc/appmap-gradle-plugin/issues/10)) ([eb2f555](https://github.com/applandinc/appmap-gradle-plugin/commit/eb2f555bff214f0a39686408a9210bf94b026589))

## [1.0.1](https://github.com/applandinc/appmap-gradle-plugin/compare/v1.0.0...v1.0.1) (2021-05-12)


### Bug Fixes

* Publish to plugins.gradle.org ([d0ec88d](https://github.com/applandinc/appmap-gradle-plugin/commit/d0ec88dcc5db8f2b878651e9bcd0a1d0c475fae3))

# 1.0.0 (2021-05-12)


### Features

* Updated Documentation, Added validate-config goal to check on config files, and clean output directory action to delete previously generated files. ([#5](https://github.com/applandinc/appmap-gradle-plugin/issues/5)) ([8f44c51](https://github.com/applandinc/appmap-gradle-plugin/commit/8f44c51da0a20656f89e12e74383142939fb8abb))
