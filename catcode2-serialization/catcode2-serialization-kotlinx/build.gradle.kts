plugins {
    id("catcode2.kotlinx-serialization-all-platform-config")
    id("catcode2.maven-publish")
    id("catcode2.dokka-module-configuration")
    kotlin("plugin.serialization")
}


kotlin {
    sourceSets {
        all {
            languageSettings {
                optIn("kotlin.js.ExperimentalJsExport")
            }
        }
        
        getByName("commonMain") {
            dependencies {
                api(project(":catcode2-core"))
                api(libs.kotlinx.serialization.core)
            }
        }
        getByName("commonTest") {
            dependencies {
                implementation(libs.kotlinx.serialization.json)
                implementation(libs.kotlinx.serialization.properties)
            }
        }
    }
}
