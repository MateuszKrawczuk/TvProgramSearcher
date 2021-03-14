pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        maven(url = "https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }

}


rootProject.name = "TvProgramSearcher"

enableFeaturePreview("GRADLE_METADATA")

include(":app", ":common")