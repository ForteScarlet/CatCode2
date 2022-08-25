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
    
    val jarSources by tasks.registering(Jar::class) {
        archiveClassifier.set("sources")
        from(sourceSets["main"].allSource)
    }
    
    val jarJavadoc by tasks.registering(Jar::class) {
        archiveClassifier.set("javadoc")
    }
    
    publishing {
        publications {
            create<MavenPublication>("catcodeDist") {
                from(components["java"])
                artifact(jarSources)
                artifact(jarJavadoc)
                
                groupId = project.group.toString()
                artifactId = project.name
                version = project.version.toString()
                
                println("[publication] - groupId:    $groupId")
                println("[publication] - artifactId: $artifactId")
                println("[publication] - version:    $version")
                
                pom {
                    show()
                    
                    name.set("${project.group}:${project.name}")
                    description.set(project.description ?: Version.DESCRIPTION)
                    url.set("https://github.com/ForteScarlet/CatCode2")
                    licenses {
                        license {
                            name.set("MIT License")
                            url.set("https://mit-license.org/")
                        }
                    }
                    scm {
                        url.set("https://github.com/ForteScarlet/CatCode2")
                        connection.set("scm:git:https://github.com/ForteScarlet/CatCode2.git")
                        developerConnection.set("scm:git:ssh://git@github.com/ForteScarlet/CatCode2.git")
                    }
                    
                    setupDevelopers()
                }
            }
            
            
            
            repositories {
                mavenLocal()
                if (project.version.toString().contains("SNAPSHOT", true)) {
                    configPublishMaven(Sonatype.Snapshot, sonatypeUsername, sonatypePassword)
                } else {
                    configPublishMaven(Sonatype.Central, sonatypeUsername, sonatypePassword)
                }
            }
        }
    }
    
    signing {
        val keyId = System.getenv("GPG_KEY_ID")
        val secretKey = System.getenv("GPG_SECRET_KEY")
        val password = System.getenv("GPG_PASSWORD")
        
        setRequired {
            !project.version.toString().endsWith("SNAPSHOT")
        }
        
        useInMemoryPgpKeys(keyId, secretKey, password)
        
        sign(publishing.publications["catcodeDist"])
    }
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
            id.set("forte")
            name.set("ForteScarlet")
            email.set("ForteScarlet@163.com")
            url.set("https://github.com/ForteScarlet")
        }
        developer {
            id.set("forliy")
            name.set("ForliyScarlet")
            email.set("ForliyScarlet@163.com")
            url.set("https://github.com/ForliyScarlet")
        }
    }
}

inline val Project.sourceSets: SourceSetContainer
    get() = extensions.getByName("sourceSets") as SourceSetContainer

fun show() {
    //// show project info
    println("========================================================")
    println("== project.group:       $group")
    println("== project.name:        $name")
    println("== project.version:     $version")
    println("== project.description: $description")
    println("========================================================")
}