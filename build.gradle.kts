import tasks.registerChangelogGenerateTask

plugins {
    id("catcode2.nexus-publish")
    id("catcode2.dokka-multi-module")
}

val versionValue = if (isRelease()) Version.VERSION else "${Version.VERSION}-SNAPSHOT"

group = Version.GROUP
version = versionValue
description = Version.DESCRIPTION

println("GROUP   = $group")
println("VERSION = $version")



repositories {
    mavenLocal()
    mavenCentral()
    maven {
        url = uri(Sonatype.Snapshot.URL)
        mavenContent {
            snapshotsOnly()
        }
    }
}

subprojects {
    group = Version.GROUP
    version = versionValue
    description = Version.DESCRIPTION
    repositories {
        mavenLocal()
        mavenCentral()
        maven {
            url = uri(Sonatype.Snapshot.URL)
            mavenContent {
                snapshotsOnly()
            }
        }
    }
}



registerJsCopyTask()

registerChangelogGenerateTask()