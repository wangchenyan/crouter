pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
        maven("https://jitpack.io")
        mavenLocal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}
rootProject.name = "crouter"
include(":crouter-annotation")
include(":crouter-api")
include(":crouter-processor")
include(":crouter-plugin")
include(":sample")
include(":sample-lib")
