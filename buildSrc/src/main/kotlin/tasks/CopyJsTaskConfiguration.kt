import org.gradle.api.Project
import org.gradle.api.file.DuplicatesStrategy
import org.gradle.api.tasks.Copy
import org.gradle.kotlin.dsl.register

fun Project.jsCopyTask() = tasks.register<Copy>("copyJsFileToSharedLibs") {
    group = "build"
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    
    from(fileTree(rootProject.buildDir.resolve("js")) {
        rootProject.subprojects.forEach {
            val projectName = it.name
            include("packages/$projectName/kotlin/$projectName.js")
            include("packages/$projectName/kotlin/$projectName.js.map")
    
        }
        include("packages_imported/*/*/*.js")
        include("packages_imported/*/*/*.js.map")
        
        exclude { "test" in it.name }
        
    }.files)
    
    
    into(rootProject.buildDir.resolve("releaseSharedLibs"))
}
