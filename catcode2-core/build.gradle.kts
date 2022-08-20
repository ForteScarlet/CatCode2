plugins {
    id("catcode2.all-platform-config")
    // kotlin("multiplatform")
    kotlin("plugin.serialization")
}

group = "love.forte.catcode2"
version = "0.0.1"


kotlin {
    sourceSets {
        getByName("commonMain") {
            dependencies {
                api(libs.kotlinx.serialization.core)
            }
        }
        getByName("commonTest") {
            dependencies {
                implementation(libs.kotlinx.serialization.json)
                // implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
            }
        }
//         val jvmMain by getting
//         val jvmTest by getting {
//             dependencies {
//                 implementation(kotlin("test-junit5"))
//             }
//         }
//         // val jsMain by getting
//         // val jsTest by getting
    }
}
