import org.gradle.api.initialization.resolve.RepositoriesMode

pluginManagement {
    repositories {
        maven { url = uri("https://dl.google.com/android/maven2") }
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        maven { url = uri("https://dl.google.com/android/maven2") }
        google()
        mavenCentral()
    }
}

rootProject.name = "LeaveFlowApp"
include(":app")
