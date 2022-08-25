plugins {
    id("catcode2.all-platform-config")
    id("catcode2.maven-publish")
}

kotlin {
    sourceSets {
        getByName("jsTest") {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.4.0")
            }
        }
    }
}