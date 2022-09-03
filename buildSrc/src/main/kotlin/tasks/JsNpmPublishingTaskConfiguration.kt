package tasks

import org.gradle.api.Project
import org.gradle.api.tasks.Copy
import org.gradle.kotlin.dsl.register

fun Project.registerNpmPublishTask() {
    val copyJsLib = tasks.register<Copy>("jsPackageCopy") {
        group = "copy"
        val rootBuildDir = buildDir
        // buildDir/productionLibrary
        subprojects {
            from("$rootBuildDir/js/packages/${name}/**")
        }
        
        into("$rootBuildDir/jsPackagesTmp/")
    }
    
    
}