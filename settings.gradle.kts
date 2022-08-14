
rootProject.name = "catcode2"

dependencyResolutionManagement {
    @Suppress("UnstableApiUsage")
    versionCatalogs {
        create("libs") {
            from(files(File(rootProject.projectDir, "libs.versions.toml")))
        }
    }
}

include("catcode2-core")
include("catcode2-serialization:catcode2-serialization-kotlinx")