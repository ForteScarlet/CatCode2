package tasks

import isRelease
import org.gradle.api.Project
import org.gradle.api.tasks.Copy
import org.gradle.kotlin.dsl.register
import org.gradle.process.ExecResult
import org.gradle.process.ExecSpec

fun Project.npmPublishTask() = run {
    
    val baseDir = buildDir.resolve("productionLibrary")
    val targetDir = buildDir.resolve("jsPackageTemp")
    
    val packageCopyTask = tasks.register<Copy>("jsPackageCopy") {
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
    
    val isSnapshot = !isRelease()
    
    tasks.register("publishToNpm") {
        dependsOn(packageCopyTask)
        group = "publishing"
        
        onlyIf { targetDir.exists() }
        doLast {
            val hostOs = System.getProperty("os.name")
            val isMingwX64 = hostOs.startsWith("Windows")
            val npmFirstArgs = if (isMingwX64) "npm.cmd" else "npm"
            fun exec0(block: ExecSpec.() -> Unit): ExecResult {
                return project.exec {
                    workingDir = targetDir
                    block()
                }
            }
            exec0 { i(npmFirstArgs) }
            exec0 { ci(npmFirstArgs) }
            exec0 { publish(npmFirstArgs, isSnapshot) }
        }
    }
    
    
}

/*
 //on windows:
  commandLine 'cmd', '/c', 'stop.bat'
  //on linux
  commandLine './stop.sh'
 */

// private fun ExecSpec.windowsExec(isSnapshot: Boolean) {
//     platformExec("npm.cmd", isSnapshot)
// }
//
// private fun ExecSpec.linuxExec(isSnapshot: Boolean) {
//     platformExec("npm", isSnapshot)
// }

private fun ExecSpec.i(firstArgs: String) {
    commandLine(firstArgs, "i")
}

private fun ExecSpec.ci(firstArgs: String) {
    commandLine(firstArgs, "ci")
}

private fun ExecSpec.publish(firstArgs: String, isSnapshot: Boolean) {
    val publishArgs = mutableListOf<String>().apply {
        add(firstArgs)
        add("publish")
        add("--access")
        add("public")
        if (isSnapshot) {
            add("--tag")
            add("snapshot")
        }
    }
    
    commandLine(publishArgs)
}

// private fun ExecSpec.platformExec(firstArgs: String, isSnapshot: Boolean) {
//     commandLine(firstArgs, "install")
//     commandLine(firstArgs, "ci")
//
//     val publishArgs = mutableListOf<String>().apply {
//         add(firstArgs)
//         add("publish")
//         add("--access public")
//         if (isSnapshot) {
//             add("--tag snapshot")
//         }
//     }
//
//     commandLine(publishArgs)
//
//     println(this.commandLine)
// }
