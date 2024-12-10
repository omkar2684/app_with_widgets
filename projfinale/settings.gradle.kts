pluginManagement {
    repositories {
        google()  // Only Google repository
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()  // Only Google repository for dependency resolution
        mavenCentral()
    }
}

rootProject.name = "despuapp"
include(":app")
