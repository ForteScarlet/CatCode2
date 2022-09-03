plugins {
    id("catcode2.all-platform-config")
    id("catcode2.maven-publish")
    id("catcode2.dokka-module-configuration")
}

kotlin {
    
    sourceSets {
        all {
            languageSettings {
                optIn("kotlin.js.ExperimentalJsExport")
            }
        }
        getByName("jsTest") {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.4.0")
            }
        }
    }
}

// apply(plugin = "catcode2.maven-publish")