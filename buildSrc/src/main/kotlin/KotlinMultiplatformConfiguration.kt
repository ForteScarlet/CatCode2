import org.gradle.api.Action
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Project
import org.gradle.kotlin.dsl.get
import org.gradle.kotlin.dsl.getValue
import org.gradle.kotlin.dsl.getting
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet
import org.jetbrains.kotlin.gradle.plugin.mpp.SharedLibrary
import tasks.npmPublishTask

private const val GITHUB_URL = "https://github.com/ForteScarlet/CatCode2"
private const val GIT_URL = "git+https://github.com/ForteScarlet/CatCode2.git"
private const val GITHUB_ISSUES_URL = "https://github.com/ForteScarlet/CatCode2/issues"

fun Project.configMultiplatform(
    skipSerializationNotSupport: Boolean = false,
    sharedLibConfigure: SharedLibrary.() -> Unit = {},
    andMore: (KotlinMultiplatformExtension.() -> Unit)? = null,
) {
    
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
            testRuns.all {
                executionTask.configure {
                    useJUnitPlatform()
                }
            }
        }
        
        var jsVersion = this@configMultiplatform.jsVersion
        
        // The IR backend does not make Kotlin declarations available to JavaScript by default at all.
        // To make Kotlin declarations visible to JavaScript, they must be annotated with @JsExport.
        js(IR) {
            moduleName = project.name
            
            browser()
            nodejs()
            
            binaries.library()
            
            // moduleKind0 = "umd"
            val moduleKind0 = "commonjs"
            
            compilations["main"].apply {
                packageJson {
                    name = jsPackageJsonName
                    version = jsVersion
                    
                    customField("repository", mapOf("type" to "git", "url" to GIT_URL))
                    customField("description", project.description?.toString() ?: "")
                    customField(
                        "keywords", listOf(
                            "CatCode", "Kotlin", "ForteScarlet", "Simbot", "Simple Robot", "Forte"
                        )
                    )
                    customField("author", "ForteScarlet")
                    customField("license", "MIT")
                    customField("bugs", mapOf("url" to GITHUB_ISSUES_URL))
                    customField("homepage", GITHUB_URL)
                }
                
                kotlinOptions.apply {
                    outputFile = "$buildDir/js/packages/${project.name}/kotlin/${project.name}"
                    moduleKind = moduleKind0
                    sourceMap = true
                    sourceMapEmbedSources = "always"
                }
            }
            
            
            compilations["test"].kotlinOptions.apply {
                moduleKind = moduleKind0
                metaInfo = true
                sourceMap = true
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
            
            val exceptJvmCommonMain = create("exceptJvmCommonMain") {
                dependsOn(commonMain)
            }
            
            val exceptJvmCommonTest = create("exceptJvmCommonTest") {
                dependsOn(commonTest)
            }
            
            getByName("jsMain") {
                dependsOn(exceptJvmCommonMain)
            }
            
            getByName("jvmTest") {
                dependencies {
                    implementation(kotlin("test-junit5"))
                }
                dependsOn(exceptJvmCommonTest)
            }
            
            getByName("jsTest") {
                dependencies {
                    implementation(kotlin("test-js"))
                }
            }
            
            val nativeCommonMain = create("nativeCommonMain") {
                dependsOn(commonMain)
                dependsOn(exceptJvmCommonMain)
            }
            val nativeCommonTest = create("nativeCommonTest") {
                dependsOn(commonTest)
                dependsOn(exceptJvmCommonTest)
            }
            val notIn = setOf(
                "commonMain", "commonTest",
                "jvmMain", "jvmTest",
                "jsMain", "jsTest",
                "exceptJvmCommonMain", "exceptJvmCommonTest",
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
    npmPublishTask()
}

internal fun org.gradle.api.Project.`kotlin`(configure: Action<org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension>): Unit =
    (this as org.gradle.api.plugins.ExtensionAware).extensions.configure("kotlin", configure)


internal fun org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension.`sourceSets`(configure: Action<NamedDomainObjectContainer<KotlinSourceSet>>): Unit =
    (this as org.gradle.api.plugins.ExtensionAware).extensions.configure("sourceSets", configure)


public val Project.jsPackageJsonName: String
    get() {
        // @catcode2/module
        val prefix = "${rootProject.name}-"
        val projectName = project.name
        if (projectName.startsWith(prefix)) {
            return "@${rootProject.name}/${projectName.drop(prefix.length)}"
        } else {
            return "@${rootProject.name}/$projectName"
        }
    }


public val Project.jsVersion: String
    get() = if (isRelease()) version.toString() else version.toString() + "." + Version.currentTimeValue