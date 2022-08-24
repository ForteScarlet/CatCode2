plugins {
    id("catcode2.all-platform-config")
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