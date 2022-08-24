import org.jetbrains.kotlin.gradle.dsl.AbstractKotlinNativeBinaryContainer
import org.jetbrains.kotlin.gradle.dsl.KotlinTargetContainerWithPresetFunctions
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinTargetWithBinaries
import org.jetbrains.kotlin.gradle.plugin.mpp.SharedLibrary


fun KotlinTargetWithBinaries<*, AbstractKotlinNativeBinaryContainer>.configSharedLib(configure: SharedLibrary.() -> Unit = {}) {
    binaries {
        sharedLib(configure = configure)
    }
}


fun KotlinTargetContainerWithPresetFunctions.configureSupportNativePlatforms(skipSerializationNotSupport: Boolean = false) {
    iosArm32 {
        configSharedLib()
    }
    iosArm64 {
        configSharedLib()
    }
    iosX64 {
        configSharedLib()
    }
    iosSimulatorArm64 {
        configSharedLib()
    }
    watchosArm32 {
        configSharedLib()
    }
    watchosArm64 {
        configSharedLib()
    }
    watchosX86 {
        configSharedLib()
    }
    watchosX64 {
        configSharedLib()
    }
    watchosSimulatorArm64 {
        configSharedLib()
    }
    tvosArm64 {
        configSharedLib()
    }
    tvosX64 {
        configSharedLib()
    }
    tvosSimulatorArm64 {
        configSharedLib()
    }
    linuxX64 {
        configSharedLib()
    }
    mingwX86 {
        configSharedLib()
    }
    mingwX64 {
        configSharedLib()
    }
    macosX64 {
        configSharedLib()
    }
    macosArm64 {
        configSharedLib()
    }
    linuxArm64 {
        configSharedLib()
    }
    linuxArm32Hfp {
        configSharedLib()
    }
    
    if (!skipSerializationNotSupport) {
        linuxMips32 {
            configSharedLib()
        }
        linuxMipsel32 {
            configSharedLib()
        }
        wasm32 {
            // unsupported
            // Caused by: java.lang.IllegalArgumentException: Cannot create a dynamic library: debugShared. Binaries of this kind are not available for target wasm32
            // configSharedLib()
        }
    }
}