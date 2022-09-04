package tasks

import org.gradle.api.Project
import org.gradle.api.tasks.Copy
import org.gradle.kotlin.dsl.register

fun Project.npmPublishTask(
    packageCopyTaskName: String = "jsPackageCopy",
) {
    
    val baseDir = buildDir.resolve("productionLibrary")
    val targetDir = buildDir.resolve("jsPackageTemp")
    
    val packageCopyTask = tasks.register<Copy>(packageCopyTaskName) {
        dependsOn("jsNodeProductionLibraryDistribution")
        
        group = "publishing"
        
        // buildDir/productionLibrary
        from(baseDir)
        val readmeFile = projectDir.resolve("README-JS.md")
        if (readmeFile.exists() && readmeFile.length() > 0) {
            from(readmeFile)
        }
        rename("README-JS.md", "README.md")
        into(targetDir)
    }
    
    // if (baseDir.exists()) {
    //     val result = exec {
    //         workingDir = baseDir
    //
    //         // commandLine("powershell", "npm", "install")
    //         /*
    //          //on windows:
    //           commandLine 'cmd', '/c', 'stop.bat'
    //
    //           //on linux
    //           commandLine './stop.sh'
    //          */
    //     }
    //     println("===============================================================")
    //     println("===============================================================")
    //     println(result.toString())
    //     println("===============================================================")
    //     println("===============================================================")
    // }
    
}


private fun generatePublishScript(): String {
    
    
    TODO()
}