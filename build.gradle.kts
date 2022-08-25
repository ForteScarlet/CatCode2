plugins {
    id("catcode2.nexus-publish")
}

val versionValue = if (isRelease()) Version.VERSION else "${Version.VERSION}-SNAPSHOT"

group = Version.GROUP
version = versionValue

println("VERSION = $group")
println("GROUP   = $version")

subprojects {
    group = Version.GROUP
    version = versionValue
}

repositories {
    mavenCentral()
}