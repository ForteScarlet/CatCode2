
val versionValue = if (isRelease()) Version.VERSION else "${Version.VERSION}-SNAPSHOT"

group = Version.GROUP
version = versionValue

subprojects {
    group = Version.GROUP
    version = versionValue
}

repositories {
    mavenCentral()
}