# Bundletool Gradle Plugin

A Gradle plugin to manage building split APKs with bundletool.

## About

[`bundletool`] is Google's tool for generating and signing split APKs, the modern format for
distributing apps on Android. Typically the Play Store generates these split APKs, but for
developers who generate them themselves, managing `bundletool` manually can be foreign and
complicated. This plugin aims to streamline the process of building split APKs for developers. It is
primarily intended for those publishing to [Accrescent], but is built to be more generally useful
outside of Accrescent.

## Usage

Apply the plugin to your Android app as described on [the plugin's home page]. You can then build
split APKs for your app by running `./gradlew buildApks${variant}`. For example, if your apps has a
`release` variant, you can build the corresponding split APKs with the following command:

```
$ ./gradlew buildApksRelease
```

The resulting APK set will be generated as
`app/build/outputs/apkset/${variant}/app-${variant}.apks`.

The default signing configuration for the respective app variant will be used to sign the split
APKs.

[Accrescent]: https://accrescent.app
[`bundletool`]: https://developer.android.com/studio/command-line/bundletool
[the plugin's home page]: https://plugins.gradle.org/plugin/app.accrescent.tools.bundletool
