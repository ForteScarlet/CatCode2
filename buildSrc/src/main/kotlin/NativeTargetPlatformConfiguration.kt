import org.gradle.api.Project
import org.gradle.api.file.DuplicatesStrategy
import org.gradle.api.tasks.Copy
import org.gradle.kotlin.dsl.register
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.AbstractKotlinNativeBinaryContainer
import org.jetbrains.kotlin.gradle.dsl.KotlinTargetContainerWithPresetFunctions
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinTargetWithBinaries
import org.jetbrains.kotlin.gradle.plugin.mpp.SharedLibrary
import org.jetbrains.kotlin.gradle.tasks.KotlinNativeLink


fun KotlinTargetWithBinaries<*, AbstractKotlinNativeBinaryContainer>.configSharedLib(
    sharedLib: Boolean,
    staticLib: Boolean,
    configure: SharedLibrary.() -> Unit = {},
) {
    binaries {
        val targetSubDirectory = target.disambiguationClassifier?.let { "$it/" }.orEmpty()
        if (sharedLib) {
            sharedLib {
                baseName = project.name
                outputDirectory = project.buildDir.resolve("sharedLibs/$targetSubDirectory${this.name}")
                configure()
            }
        }
        
        if (staticLib) {
            staticLib {
                outputDirectory = project.buildDir.resolve("staticLibs/${targetSubDirectory}${this.name}")
            }
        }
        
    }
}

fun Project.sharedLibsCopyTask() {
    val nativeLinks = tasks.withType<KotlinNativeLink>()
    
    val nativeLinkCopyTasks = nativeLinks.map {
        val targetPlatformName = it.binary.target.disambiguationClassifier
        tasks.register<Copy>("${it.name}CopySharedLibs") {
            group = "build"
            duplicatesStrategy = DuplicatesStrategy.EXCLUDE
            from(it.outputs)
            include("**.h")
            // windows
            include("**.dll")
            include("**.def")
            // linux
            include("**.so")
            // macos
            include("**.dylib")
            // staticLib
            include("**.a")
            include("**.lib")
            
            rename { name ->
                val removedPrefixName = name.removePrefix("lib")
                if (removedPrefixName.endsWith(".h")) {
                    return@rename removedPrefixName
                }
                println("============================")
                println("rename.name: ${removedPrefixName}")
                println("nativeLink: ${it}")
                println("nativeLink.name: ${it.name}")
                println("nativeLink.debuggable: ${it.binary.debuggable}")
                println("nativeLink.targetPlatformName: ${targetPlatformName}")
                println("============================")
                
                if (targetPlatformName == null) {
                    return@rename removedPrefixName
                }
                
                val lastIndex = removedPrefixName.indexOfLast { it == '.' }
                if (lastIndex < 0 || lastIndex == removedPrefixName.lastIndex) {
                    return@rename removedPrefixName + "_$targetPlatformName"
                }
                
                val filenameWithoutExtension = removedPrefixName.substring(0, lastIndex)
                val extension = removedPrefixName.substring(lastIndex)
                
                "${filenameWithoutExtension}_$targetPlatformName$extension"
            }
            into(rootProject.buildDir.resolve("releaseSharedLibs"))
        }
    }
    
    tasks.register("copySharedLibs") {
        group = "build"
        // dependsOn(provider {
        //     //  it.name == "build" ||
        //     tasks.filter { it.name != "buildAndCopySharedLibs" && it.name.endsWith("CopySharedLibs") }
        // })
        dependsOn(nativeLinkCopyTasks)
        mustRunAfter(provider { tasks.named("build") })
    }
}

fun KotlinTargetContainerWithPresetFunctions.configureSupportNativePlatforms(
    sharedLib: Boolean,
    staticLib: Boolean,
    skipSerializationNotSupport: Boolean = false,
    sharedLibConfigure: SharedLibrary.() -> Unit = {},
) {
    
    fun KotlinTargetWithBinaries<*, AbstractKotlinNativeBinaryContainer>.configSharedLib0() {
        configSharedLib(sharedLib, staticLib, sharedLibConfigure)
    }
    
    iosArm32 {
        configSharedLib0()
    }
    iosArm64 {
        configSharedLib0()
    }
    iosX64 {
        configSharedLib0()
    }
    iosSimulatorArm64 {
        configSharedLib0()
    }
    watchosArm32 {
        configSharedLib0()
    }
    watchosArm64 {
        configSharedLib0()
    }
    watchosX86 {
        configSharedLib0()
    }
    watchosX64 {
        configSharedLib0()
    }
    watchosSimulatorArm64 {
        configSharedLib0()
    }
    tvosArm64 {
        configSharedLib0()
    }
    tvosX64 {
        configSharedLib0()
    }
    tvosSimulatorArm64 {
        configSharedLib0()
    }
    linuxX64 {
        configSharedLib0()
    }
    mingwX86 {
        configSharedLib0()
    }
    mingwX64 {
        configSharedLib0()
    }
    macosX64 {
        configSharedLib0()
    }
    macosArm64 {
        configSharedLib0()
    }
    linuxArm64 {
        configSharedLib0()
    }
    linuxArm32Hfp {
        configSharedLib0()
    }
    
    // not support in kotlinx serialization
    // ...
    // right?
    if (!skipSerializationNotSupport) {
        linuxMips32 {
            configSharedLib0()
        }
        linuxMipsel32 {
            configSharedLib0()
        }
        wasm32 {
            // unsupported
            // Caused by: java.lang.IllegalArgumentException: Cannot create a dynamic library: debugShared. Binaries of this kind are not available for target wasm32
            // configSharedLib()
        }
    }
}