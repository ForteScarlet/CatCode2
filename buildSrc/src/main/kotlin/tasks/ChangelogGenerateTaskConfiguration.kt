package tasks

import Version
import org.gradle.api.Project
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.kotlin.dsl.findByType
import prop
import java.io.File


fun Project.changelogGenerateTask() = tasks.register("generateChangelog") {
    group = "changelog"
    
    logger.info("============================================================")
    val tagNameProperty = "generateChangelogTagName"
    val tagName = prop(tagNameProperty) ?: "v${Version.VERSION}"
    
    val moduleTargetsList = mutableListOf<ModuleRepoTargets>()
    
    project.subprojects.forEach {
        logger.info("project: {}", it)
        // publishable
        val publishingExtensions = it.extensions.findByType<org.gradle.api.publish.PublishingExtension>()
        
        val repoTargets = mutableListOf<MavenRepoTarget>()
        publishingExtensions?.generateModuleRepo(it, repoTargets::add)
        
        if (repoTargets.isNotEmpty()) {
            moduleTargetsList.add(ModuleRepoTargets(it.name, repoTargets))
        }
    }
    
    val text: String = if (moduleTargetsList.isNotEmpty()) {
        buildString {
            append(
                """
                <details>
                <summary>仓库参考</summary>
                
                
            """.trimIndent()
            )
            
            moduleTargetsList.forEach { targets ->
                targets.generateTable(::append)
                append("\n\n")
            }
            
            append("</details>\n")
        }
    } else ""
    
    
    writeToChangelog(text, tagName)
    
    logger.info("============================================================")
}


private inline fun PublishingExtension.generateModuleRepo(project: Project, generated: (MavenRepoTarget) -> Unit) {
    val logger = project.logger
    logger.info("PublishingExtension: {}", this)
    logger.info("publication: {}", publications)
    publications.forEach {
        if (it !is MavenPublication) {
            return@forEach
        }
        
        generated(MavenRepoTarget(it.groupId, it.artifactId, it.version))
    }
    
    
}

private fun Project.changelogFile(filename: String): File {
    return rootProject.file(".changelog").apply {
        mkdirs()
    }.resolve(filename)
}

private fun Project.writeToChangelog(text: String, tag: String) {
    fun File.takeIfNotExists(): File? = takeIf { !it.exists() }
    fun File.takeIfExists(): File? = takeIf { it.exists() }
    
    fun File.applyCreateNewFile(): File = apply { createNewFile() }
    
    
    val file = changelogFile("$tag.changelog.md").takeIfNotExists()?.applyCreateNewFile() ?: return
    
    changelogFile("$tag.md")
        .takeIfExists()
        ?.takeIf { it.canRead() }
        ?.also { customFile ->
            
            file.bufferedWriter().use { writer ->
                customFile.bufferedReader().use {  reader ->
                    reader.copyTo(writer)
                }
                writer.newLine()
                writer.newLine()
                writer.append("<hr />")
                writer.newLine()
                writer.newLine()
            }
        }
    
    file.appendText(text)
}

private data class MavenRepoTarget(val groupId: String, val artifactId: String, val version: String) {
    val groupPath = groupId.replace('.', '/')
    
    fun toRepo1Url(): String = "https://repo1.maven.org/maven2/$groupPath/$artifactId/$version"
    
    // https://search.maven.org/artifact/
    fun tomavenSerachUrl(): String = "https://search.maven.org/artifact/$groupId/$artifactId/$version/jar"
}


private inline fun repoTargetsToTableString(targets: Iterable<MavenRepoTarget>, onString: (String) -> Unit) {
    onString("| **模块** | **repo1.maven** | **search.maven** |\n")
    onString("|---------|-----------------|------------------|\n")
    
    targets.forEach { target ->
        val linkDisplay = "${target.artifactId}: v${target.version}"
        onString("| ${target.artifactId} | [$linkDisplay](${target.toRepo1Url()}) | [$linkDisplay](${target.tomavenSerachUrl()}) |\n")
    }
}

private data class ModuleRepoTargets(val module: String, val targets: List<MavenRepoTarget>) {
    inline fun generateTable(onGenerate: (String) -> Unit) {
        onGenerate("**$module**\n\n")
        repoTargetsToTableString(targets, onGenerate)
    }
}