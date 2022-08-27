import org.gradle.nativeplatform.platform.internal.DefaultNativePlatform
import org.jetbrains.kotlin.konan.target.HostManager
import org.jetbrains.kotlin.konan.target.KonanTarget

plugins {
    id("signing")
    id("maven-publish")
}

val isPublishConfigurable = isPublishConfigurable()

if (!isPublishConfigurable) {
    println("[WARN] - sonatype.username or sonatype.password is null, cannot config nexus publishing.")
}

if (isPublishConfigurable) {
    val (sonatypeUsername, sonatypePassword) = sonatypeUserInfo
    
    val jarJavadoc by tasks.registering(Jar::class) {
        group = "documentation"
        archiveClassifier.set("javadoc")
        from(tasks.findByName("dokkaHtml"))
    }
    
    publishing {
        publications {
            repositories {
                mavenLocal()
                if (project.version.toString().contains("SNAPSHOT", true)) {
                    configPublishMaven(Sonatype.Snapshot, sonatypeUsername, sonatypePassword)
                } else {
                    configPublishMaven(Sonatype.Central, sonatypeUsername, sonatypePassword)
                }
            }
            
            configureEach {
                if (this !is MavenPublication) {
                    return@configureEach
                }
                
                artifact(jarJavadoc)
                
                
                // groupId = project.group.toString()
                // artifactId = project.name
                // version = project.version.toString()
                // description = project.description?.toString() ?: Version.DESCRIPTION
                
                pom {
                    show()
                    name by "${project.group}:${project.name}"
                    description by (project.description ?: Version.DESCRIPTION)
                    url by "https://github.com/ForteScarlet/CatCode2"
                    licenses {
                        license {
                            name by "MIT License"
                            url by "https://mit-license.org/"
                        }
                    }
                    scm {
                        url by "https://github.com/ForteScarlet/CatCode2"
                        connection by "scm:git:https://github.com/ForteScarlet/CatCode2.git"
                        developerConnection by "scm:git:ssh://git@github.com/ForteScarlet/CatCode2.git"
                    }
                    
                    setupDevelopers()
                }
            }
            
        }
    }
    
    val keyId = prop("GPG_KEY_ID")
    val secretKey = prop("GPG_SECRET_KEY")
    val password = prop("GPG_PASSWORD")
    
    signing {
        // setRequired {
        //     !project.version.toString().endsWith("SNAPSHOT")
        // }
        useInMemoryPgpKeys(keyId, secretKey, password)
        
        // sign(publishing.publications)
        // sign(catcodeDist)
        sign(publishing.publications)
    }
    
    afterEvaluate {
        // bases
        val publicationsFromMainHost = setOf(
            "jvm",
            "js",
            "kotlinMultiplatform",
            "metadata"
        )
        // val hostManager = HostManager().isEnabled()
        
        val hostManager = HostManager()
        
        // linux上支持的host
        val linuxSupports = hostManager.enabledByHost[KonanTarget.LINUX_X64]?.mapTo(mutableSetOf()) { it.name }
            ?: emptySet<String>()
        
        configure<PublishingExtension> {
            publications.matching {
                println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!")
                println("publications.name: ${it.name}")
                val hostName = buildString(it.name.length) {
                    it.name.forEach { c ->
                        if (c.isUpperCase()) {
                            append('_').append(c.toLowerCase())
                        } else {
                            append(c)
                        }
                    }
                }
                println("publications.hostName: $hostName")
                println("publications.name in $publicationsFromMainHost: ${it.name in publicationsFromMainHost}")
                println("publications.hostName in $linuxSupports: ${hostName in linuxSupports}")
                println("match: ${it.name in publicationsFromMainHost || hostName in linuxSupports}")
                println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!")
                it.name in publicationsFromMainHost || hostName in linuxSupports
            }.all {
                val targetPublication = this@all
                tasks.withType<AbstractPublishToMaven>()
                    .matching { it.publication == targetPublication }
                    .configureEach {
                        onlyIf {
                            // main publishing CI job is executed on Linux host
                            DefaultNativePlatform.getCurrentOperatingSystem().isLinux.apply {
                                if (!this) {
                                    // logger.lifecycle("Publication ${(it as AbstractPublishToMaven).publication.name} is skipped on current host")
                                    println("Publication ${(it as AbstractPublishToMaven).publication.name} is skipped on current host")
                                }
                            }
                        }
                    }
            }
        }
    }
    
    
    println("[publishing-configure] - [$name] configured.")
}

// kotlin {
//     jvm {
//         mavenPublication {
//             javadocJar("jvmJavadocJar")
//             pom {}
//         }
//     }
//     js {
//         mavenPublication {
//             javadocJar("jsJavadocJar")
//             pom {}
//         }
//     }
// }


fun RepositoryHandler.configPublishMaven(sonatype: Sonatype, username: String?, password: String?) {
    maven {
        name = sonatype.name
        url = uri(sonatype.url)
        credentials {
            this.username = username
            this.password = password
        }
    }
}

/**
 * 配置开发者/协作者信息。
 *
 */
fun MavenPom.setupDevelopers() {
    developers {
        developer {
            id by "forte"
            name by "ForteScarlet"
            email by "ForteScarlet@163.com"
            url by "https://github.com/ForteScarlet"
        }
        developer {
            id by "forliy"
            name by "ForliyScarlet"
            email by "ForliyScarlet@163.com"
            url by "https://github.com/ForliyScarlet"
        }
    }
}

inline val Project.sourceSets: SourceSetContainer
    get() = extensions.getByName("sourceSets") as SourceSetContainer

inline val Project.publishing: PublishingExtension
    get() = extensions.getByType<PublishingExtension>()

fun org.gradle.api.Project.`kotlin`(configure: Action<org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension>): Unit =
    (this as org.gradle.api.plugins.ExtensionAware).extensions.configure("kotlin", configure)

fun MavenPublication.jar(taskName: String, config: Action<Jar>) = artifact(tasks.create(taskName, Jar::class, config))

fun MavenPublication.javadocJar(taskName: String, config: Jar.() -> Unit = {}) = jar(taskName) {
    archiveClassifier by "javadoc"
    config()
}

fun show() {
    //// show project info
    println("========================================================")
    println("== project.group:       $group")
    println("== project.name:        $name")
    println("== project.version:     $version")
    println("== project.description: $description")
    println("========================================================")
}