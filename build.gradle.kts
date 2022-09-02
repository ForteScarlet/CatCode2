plugins {
    id("catcode2.nexus-publish")
    id("catcode2.dokka-multi-module")
}

val versionValue = if (isRelease()) Version.VERSION else "${Version.VERSION}-SNAPSHOT"

group = Version.GROUP
version = versionValue
description = Version.DESCRIPTION

println("VERSION = $group")
println("GROUP   = $version")

subprojects {
    group = Version.GROUP
    version = versionValue
    description = Version.DESCRIPTION
}

repositories {
    mavenCentral()
}

tasks.register<Copy>("copyJsFileToSharedLibs") {
    group = "build"
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    
    
    from(fileTree(rootProject.buildDir.resolve("js")) {
        include("packages/*-js-legacy/kotlin/*-js-legacy.js")
        include("packages/*-js-legacy/kotlin/*-js-legacy.js.map")
        include("packages_imported/kotlin/1.7.10/kotlin.js")
        include("packages_imported/kotlin/1.7.10/kotlin.js.map")
    }.files)
    
    rename { n ->
        var renamed = n
        if (renamed.endsWith("-js-legacy.js")) {
            renamed = renamed.removeSuffix("-js-legacy.js") + ".js"
        }
        if (renamed.endsWith("-js-legacy.js.map")) {
            renamed = renamed.removeSuffix("-js-legacy.js.map") + ".js.map"
        }
    
        if (renamed.startsWith(rootProject.name + "-")) {
            renamed = renamed.removePrefix(rootProject.name + "-")
        }
        
        renamed
    }
    into(rootProject.buildDir.resolve("releaseSharedLibs"))
    
    // onlyIf {
    //     // main publishing CI job is executed on Linux host
    //     org.gradle.nativeplatform.platform.internal.DefaultNativePlatform.getCurrentOperatingSystem().isLinux
    // }
    
}