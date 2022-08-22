plugins {
    id("catcode2.all-platform-config-without32")
    kotlin("plugin.serialization")
}

group = "love.forte.catcode2"
version = "0.0.1"


kotlin {
    sourceSets {
        getByName("commonMain") {
            dependencies {
                api(project(":catcode2-core"))
                api(libs.kotlinx.serialization.core)
            }
        }
        getByName("commonTest") {
            dependencies {
                implementation(libs.kotlinx.serialization.json)
            }
        }
    }
}
