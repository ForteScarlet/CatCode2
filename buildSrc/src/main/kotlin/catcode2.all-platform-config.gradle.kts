plugins {
    kotlin("multiplatform")
}

repositories {
    mavenCentral()
}

kotlin {
    explicitApi()
    
    
    
    jvm {
        compilations.all {
            kotlinOptions {
                jvmTarget = "1.8"
                javaParameters = true
            }
        }
        withJava()
        testRuns["test"].executionTask.configure {
            useJUnitPlatform()
        }
    }
    
    js(BOTH) {
        browser()
        nodejs()
        useCommonJs()
    }
    
    
    configureSupportNativePlatforms()
    
    
    sourceSets {
        
        val commonMain by getting
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        getByName("jvmTest") {
            dependencies {
                implementation(kotlin("test-junit5"))
            }
        }
        
        val nativeCommonMain = create("nativeCommonMain") {
            dependsOn(commonMain)
        }
        val nativeCommonTest = create("nativeCommonTest") {
            dependsOn(commonTest)
        }
        val notIn = setOf(
            "commonMain", "commonTest",
            "jvmMain", "jvmTest",
            "jsMain", "jsTest",
            "nativeCommonMain", "nativeCommonTest",
        )
        
        names.forEach { n ->
            if (n !in notIn) {
                getByName(n) {
                    when {
                        n.endsWith("Main") -> dependsOn(nativeCommonMain)
                        n.endsWith("Test") -> dependsOn(nativeCommonTest)
                    }
                }
            }
        }
        
    }
}




