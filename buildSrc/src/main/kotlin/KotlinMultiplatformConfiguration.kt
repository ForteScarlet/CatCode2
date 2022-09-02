import org.gradle.api.Action
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Project
import org.gradle.kotlin.dsl.get
import org.gradle.kotlin.dsl.getValue
import org.gradle.kotlin.dsl.getting
import org.gradle.kotlin.dsl.repositories
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet
import org.jetbrains.kotlin.gradle.plugin.mpp.SharedLibrary

fun Project.configMultiplatform(
    skipSerializationNotSupport: Boolean = false,
    sharedLibConfigure: SharedLibrary.() -> Unit = {},
    andMore: (KotlinMultiplatformExtension.() -> Unit)? = null,
) {
    repositories {
        mavenCentral()
    }
    
    kotlin {
        explicitApi()
        
        jvm {
            compilations.all {
                kotlinOptions {
                    jvmTarget = "1.8"
                    javaParameters = true
                }
            }
            withJava()
            testRuns["test"].executionTask.configure {
                useJUnitPlatform()
            }
        }
        
        js(BOTH) {
            browser()
            nodejs {
            
            }
        }
        
        configureSupportNativePlatforms(skipSerializationNotSupport, sharedLibConfigure)
        
        sourceSets {
            val commonMain by getting
            val commonTest by getting {
                dependencies {
                    implementation(kotlin("test"))
                }
            }
            
            getByName("jvmTest") {
                dependencies {
                    implementation(kotlin("test-junit5"))
                }
            }
            
            getByName("jsTest") {
                dependencies {
                    implementation(kotlin("test-js"))
                }
            }
            
            val nativeCommonMain = create("nativeCommonMain") {
                dependsOn(commonMain)
            }
            val nativeCommonTest = create("nativeCommonTest") {
                dependsOn(commonTest)
            }
            val notIn = setOf(
                "commonMain", "commonTest",
                "jvmMain", "jvmTest",
                "jsMain", "jsTest",
                "nativeCommonMain", "nativeCommonTest",
            )
            
            names.forEach { n ->
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
        
        andMore?.invoke(this)
    }
    
    sharedLibsCopyTask()
}

internal fun org.gradle.api.Project.`kotlin`(configure: Action<org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension>): Unit =
    (this as org.gradle.api.plugins.ExtensionAware).extensions.configure("kotlin", configure)


internal fun org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension.`sourceSets`(configure: Action<NamedDomainObjectContainer<KotlinSourceSet>>): Unit =
    (this as org.gradle.api.plugins.ExtensionAware).extensions.configure("sourceSets", configure)