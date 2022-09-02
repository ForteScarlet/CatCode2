/*
 *  Copyright (c) 2022-2022 ForteScarlet <ForteScarlet@163.com>
 *
 *  本文件是 simply-robot (即 simple robot的v3版本，因此亦可称为 simple-robot v3 、simbot v3 等) 的一部分。
 *
 *  simply-robot 是自由软件：你可以再分发之和/或依照由自由软件基金会发布的 GNU 通用公共许可证修改之，无论是版本 3 许可证，还是（按你的决定）任何以后版都可以。
 *
 *  发布 simply-robot 是希望它能有用，但是并无保障;甚至连可销售和符合某个特定的目的都不保证。请参看 GNU 通用公共许可证，了解详情。
 *
 *  你应该随程序获得一份 GNU 通用公共许可证的复本。如果没有，请看:
 *  https://www.gnu.org/licenses
 *  https://www.gnu.org/licenses/gpl-3.0-standalone.html
 *  https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 *
 *
 */

/*
 使用在根配置，配置dokka多模块
 */

plugins {
    id("org.jetbrains.dokka")
}


fun org.jetbrains.dokka.gradle.AbstractDokkaTask.configOutput(format: String) {
    moduleName.set("simple-robot")
    outputDirectory.set(rootProject.file("build/dokka/$format/v$version"))
}

tasks.named<org.jetbrains.dokka.gradle.DokkaMultiModuleTask>("dokkaHtmlMultiModule") {
    configOutput("html")
}


tasks.register("dokkaHtmlMultiModuleAndPost") {
    // TODO doc version?
    group = JavaBasePlugin.DOCUMENTATION_GROUP
    dependsOn("dokkaHtmlMultiModule")
    doLast {
        val outDir = rootProject.file("build/dokka/html")
        val indexFile = File(outDir, "index.html")
        indexFile.createNewFile()
        indexFile.writeText(
            """
            <html xmlns="http://www.w3.org/1999/xhtml">
            <head>
                <meta http-equiv="refresh" content="0;URL='v$version'" />
            </head>
            <body>
            </body>
            </html>
        """.trimIndent()
        )
        
        // readme?
    }
}