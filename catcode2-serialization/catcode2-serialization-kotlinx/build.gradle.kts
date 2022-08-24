plugins {
    id("catcode2.all-platform-config-without32")
    kotlin("plugin.serialization")
}


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
                implementation(libs.kotlinx.serialization.properties)
            }
        }
    }
}
