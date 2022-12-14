<div align="center">
    <img src="../../.catcode2/logo.png" alt="catcode logo"/>
    <h3>
        - ðº CatCode â¡ - Serialization Kotlinx ðº -
    </h3>
    <span>
        <a href="https://github.com/ForteScarlet/CatCode2" target="_blank">github</a>
    </span> 
    &nbsp;&nbsp; | &nbsp;&nbsp;
    <span>
        <a href="https://gitee.com/ForteScarlet/CatCode2" target="_blank">gitee</a>
    </span> <br />
    <small> &gt; æè°¢ <a href="https://github.com/ForteScarlet/simpler-robot" target="_blank">Simple Robot</a> å¼åå¢éæåå¶ä½çç«ç«logo &lt; </small> <br />
    &gt; å¦ææç¹å»ä¸ä¸â­çè¯ï¼ç«ç«ä¼å¾å¼å¿å¦~ &lt; <br />
    <a href="https://repo1.maven.org/maven2/love/forte/catcode2/" target="_blank" >
        <img src="https://img.shields.io/maven-central/v/love.forte.catcode2/catcode2-core" alt="maven-central" />
    </a>
    <a href="https://www.npmjs.com/package/@catcode2/core" target="_blank" >
            <img src="https://img.shields.io/npm/v/@catcode2/core/latest" alt="npm-latest" />
    </a>
    <a href="https://www.npmjs.com/package/@catcode2/core" target="_blank" >
        <img src="https://img.shields.io/npm/v/@catcode2/core/snapshot" alt="npm-snapshot" />
    </a>

</div>

**CatCode2** éå¯¹ [**kotlinx-serialization**](https://github.com/Kotlin/kotlinx.serialization) æå®ç°çåºååæ¨¡åã
**CatCode2 Serialization Kotlinx** åè®¸éè¿å®ä½ç±»ä½ä¸ºä¸ä¸ªç«ç«ç çåºæ¬è½½ä½ä½¿ç¨ã

```kotlin
@Serializable
@SerialName("at") // or use @CatCodeTypeName("at")
data class At(val code: Long, val name: String)

@Test
fun sample() {
    val at = At(123456L, "forte")
    val catcode = CatCode.encodeToString(At.serializer(), at)
    val newAt = CatCode.decodeFromString(At.serializer(), catcode)
    
    assertTrue { at == newAt } // pass.
    println(catcode)           // [CAT:at,code=123456,name=forte]
    
    val cqcode = CatCode.encodeToString(At.serializer(), at, "CQ")
    println(cqcode)            // [CQ:at,code=123456,name=forte]
}
```