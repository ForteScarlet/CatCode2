plugins {
    kotlin("multiplatform")
}

configMultiplatform(sharedLib = false, staticLib = false, skipSerializationNotSupport = true)
