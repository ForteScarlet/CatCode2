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