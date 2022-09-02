import org.gradle.api.Project
import org.gradle.api.file.DuplicatesStrategy
import org.gradle.api.tasks.Copy
import org.gradle.kotlin.dsl.register

fun Project.registerJsCopyTask() = tasks.register<Copy>("copyJsFileToSharedLibs") {
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
}
