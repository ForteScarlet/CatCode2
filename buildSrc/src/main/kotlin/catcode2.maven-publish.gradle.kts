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
    
    // val jarSources by tasks.registering(Jar::class) {
    //     archiveClassifier.set("sources")
    //     from(sourceSets["main"].allSource)
    // }
    
    // val jarJavadocTask = tasks.register<Jar>(name + "JavadocJar") {
    //     archiveClassifier.set("javadoc")
    // }
    val jarJavadoc by tasks.registering(Jar::class) {
        archiveClassifier.set("javadoc")
    }
    publishing {
        publications {
            create<MavenPublication>("catcodeDist") {
                // val jarJavadocTask = tasks.register<Jar>(this.name + "JavadocJar") {
                // val jarJavadocTask = tasks.register<Jar>(this.name + "JavadocJar") {
                //     archiveClassifier.set("javadoc")
                // }
                // from(components["java"])
                // artifact(jarSources)
                artifact(jarJavadoc)
                
                groupId = project.group.toString()
                artifactId = project.name
                version = project.version.toString()
                description = project.description?.toString() ?: Version.DESCRIPTION
                
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
            
            
            
            repositories {
                mavenLocal()
                // if (project.version.toString().contains("SNAPSHOT", true)) {
                //     configPublishMaven(Sonatype.Snapshot, sonatypeUsername, sonatypePassword)
                // } else {
                //     configPublishMaven(Sonatype.Central, sonatypeUsername, sonatypePassword)
                // }
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
    
    
    println("[publishing-configure] - [$name] configured.")
}

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

fun show() {
    //// show project info
    println("========================================================")
    println("== project.group:       $group")
    println("== project.name:        $name")
    println("== project.version:     $version")
    println("== project.description: $description")
    println("========================================================")
}