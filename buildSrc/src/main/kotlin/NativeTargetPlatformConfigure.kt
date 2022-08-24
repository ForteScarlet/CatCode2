import gradle.kotlin.dsl.accessors._56d1663cf6fec625a3f24228305cadc2.build
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


fun KotlinTargetWithBinaries<*, AbstractKotlinNativeBinaryContainer>.configSharedLib(configure: SharedLibrary.() -> Unit = {}) {
    binaries {
        sharedLib {
            val targetSubDirectory = target.disambiguationClassifier?.let { "$it/" }.orEmpty()
            outputDirectory = project.buildDir.resolve("sharedLibs/$targetSubDirectory${this.name}")
            configure()
        }
    }
}

fun Project.sharedLibsCopyTask() {
    val nativeLinks = tasks.withType<KotlinNativeLink>()
    
    nativeLinks.forEach {
        val targetPlatformName = it.binary.target.disambiguationClassifier
        tasks.register<Copy>("${it.name}CopySharedLibs") {
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
            rename { name ->
                if (name.endsWith(".h")) {
                    return@rename name
                }
                println("============================")
                println("rename.name: ${name}")
                println("nativeLink: ${it}")
                println("nativeLink.name: ${it.name}")
                println("nativeLink.debuggable: ${it.binary.debuggable}")
                println("nativeLink.targetPlatformName: ${targetPlatformName}")
                println("============================")
                
                if (targetPlatformName == null) {
                    return@rename name
                }
                
                val lastIndex = name.indexOfLast { it == '.' }
                if (lastIndex < 0 || lastIndex == name.lastIndex) {
                    return@rename name + "_$targetPlatformName"
                }
                
                val filenameWithoutExtension = name.substring(0, lastIndex)
                val extension = name.substring(lastIndex)
                
                "${filenameWithoutExtension}_$targetPlatformName$extension"
            }
            into(buildDir.resolve("releaseSharedLibs"))
        }
    
    }
    
    tasks.register("copySharedLibs") {
        dependsOn(provider {
            //  it.name == "build" ||
            tasks.filter { it.name.endsWith("CopySharedLibs") }
        })
        mustRunAfter(provider { tasks.named("build") })
    }
}

fun KotlinTargetContainerWithPresetFunctions.configureSupportNativePlatforms(
    skipSerializationNotSupport: Boolean = false,
    sharedLibConfigure: SharedLibrary.() -> Unit = {},
) {
    iosArm32 {
        configSharedLib(sharedLibConfigure)
    }
    iosArm64 {
        configSharedLib(sharedLibConfigure)
    }
    iosX64 {
        configSharedLib(sharedLibConfigure)
    }
    iosSimulatorArm64 {
        configSharedLib(sharedLibConfigure)
    }
    watchosArm32 {
        configSharedLib(sharedLibConfigure)
    }
    watchosArm64 {
        configSharedLib(sharedLibConfigure)
    }
    watchosX86 {
        configSharedLib(sharedLibConfigure)
    }
    watchosX64 {
        configSharedLib(sharedLibConfigure)
    }
    watchosSimulatorArm64 {
        configSharedLib(sharedLibConfigure)
    }
    tvosArm64 {
        configSharedLib(sharedLibConfigure)
    }
    tvosX64 {
        configSharedLib(sharedLibConfigure)
    }
    tvosSimulatorArm64 {
        configSharedLib(sharedLibConfigure)
    }
    linuxX64 {
        configSharedLib(sharedLibConfigure)
    }
    mingwX86 {
        configSharedLib(sharedLibConfigure)
    }
    mingwX64 {
        configSharedLib(sharedLibConfigure)
    }
    macosX64 {
        configSharedLib(sharedLibConfigure)
    }
    macosArm64 {
        configSharedLib(sharedLibConfigure)
    }
    linuxArm64 {
        configSharedLib(sharedLibConfigure)
    }
    linuxArm32Hfp {
        configSharedLib(sharedLibConfigure)
    }

    if (!skipSerializationNotSupport) {
        linuxMips32 {
            configSharedLib(sharedLibConfigure)
        }
        linuxMipsel32 {
            configSharedLib(sharedLibConfigure)
        }
        wasm32 {
            // unsupported
            // Caused by: java.lang.IllegalArgumentException: Cannot create a dynamic library: debugShared. Binaries of this kind are not available for target wasm32
            // configSharedLib()
        }
    }
}