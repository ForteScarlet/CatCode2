import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.kotlin
import org.gradle.kotlin.dsl.repositories

plugins {
    kotlin("multiplatform")
}

repositories {
    mavenCentral()
}

kotlin {
    explicitApi()
    
    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = "1.8"
        }
        withJava()
        testRuns["test"].executionTask.configure {
            useJUnitPlatform()
        }
    }
    js(BOTH) {
        browser()
        // browser {
        //     commonWebpackConfig {
        // }
        // }
    }
    
    
    // region native target
    iosArm32()
    iosArm64()
    iosX64()
    iosSimulatorArm64()
    watchosArm32()
    watchosArm64()
    watchosX86()
    watchosX64()
    watchosSimulatorArm64()
    tvosArm64()
    tvosX64()
    tvosSimulatorArm64()
    linuxX64()
    mingwX86()
    mingwX64()
    macosX64()
    macosArm64()
    linuxArm64()
    linuxArm32Hfp()
    linuxMips32()
    linuxMipsel32()
    wasm32()
    // endregion
    
    
    sourceSets {
        getByName("commonTest") {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        getByName("jvmTest") {
            dependencies {
                implementation(kotlin("test-junit5"))
            }
        }
        
        val nativeCommonMain = create("nativeCommonMain") {
        
        }
        val nativeCommonTest = create("nativeCommonTest") {
        
        }
        val notIn = setOf(
            "commonMain", "commonTest",
            "jvmMain", "jvmTest",
            "jsMain", "jsTest",
            "nativeCommonMain", "nativeCommonTest",
        )
        
        this.names.forEach { n ->
            if (n !in notIn) {
                getByName(n) {
                    when {
                        n.endsWith("Main") -> dependsOn(nativeCommonMain)
                        n.endsWith("Test") -> dependsOn(nativeCommonTest)
                    }
                }
            }
        }
        
    }
}
