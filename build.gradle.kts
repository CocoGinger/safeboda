// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id(BuildPlugins.ktlintPlugin) version Versions.ktlint
    id(BuildPlugins.detektPlugin) version Versions.detekt
    id(BuildPlugins.spotlessPlugin) version Versions.spotless
    id(BuildPlugins.androidLibrary) apply false
    id(BuildPlugins.androidApplication) apply false
    id(BuildPlugins.kotlinAndroid) apply false
    id(BuildPlugins.kotlinAndroidExtensions) apply false
    id(BuildPlugins.dokkaPlugin) version Versions.dokka
    id(BuildPlugins.gradleVersionsPlugin) version Versions.gradleVersionsPlugin
    id(BuildPlugins.slackKeeper) version Versions.slackKeeper
}

allprojects {

    repositories {
        google()
        jcenter()
        maven(url = "https://jitpack.io")
        maven("https://dl.bintray.com/kotlin/kotlin-eap")
    }

    apply(plugin = BuildPlugins.dokkaPlugin)

    apply(plugin = BuildPlugins.spotlessPlugin)

    apply(plugin = BuildPlugins.ktlintPlugin)
    ktlint {
        android.set(true)
        verbose.set(true)
        filter {
            exclude { element -> element.file.path.contains("generated/") }
        }
    }
}

buildscript {
    val kotlinVersion by extra("1.4.10")
    val jacocoVersion by extra("0.2")

    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
        classpath("com.hiya:jacoco-android:$jacocoVersion")
    }
}

subprojects {
    apply(plugin = BuildPlugins.detektPlugin)
    apply(plugin = BuildPlugins.spotlessPlugin)
    detekt {
        config = files("${project.rootDir}/detekt.yml")
        parallel = true
    }
    spotless {
        kotlin {
            target("**/*.kt")
            licenseHeaderFile(
                rootProject.file("${project.rootDir}/spotless/copyright.kt"),
                "^(package|object|import|interface)"
            )
        }
    }
}