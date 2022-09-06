<div align="center">
    <img src="../../.catcode2/logo.png" alt="catcode logo"/>
    <h3>
        - 😺 CatCode 2 - Serialization Kotlinx 😺 -
    </h3>
    <span>
        <a href="https://github.com/ForteScarlet/CatCode2" target="_blank">github</a>
    </span> 
    &nbsp;&nbsp; | &nbsp;&nbsp;
    <span>
        <a href="https://gitee.com/ForteScarlet/CatCode2" target="_blank">gitee</a>
    </span> <br />
    <small> &gt; 感谢 <a href="https://github.com/ForteScarlet/simpler-robot" target="_blank">Simple Robot</a> 开发团队成员制作的猫猫logo &lt; </small> <br />
    &gt; 如果有点击一下⭐的话，猫猫会很开心哦~ &lt; <br />
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

**CatCode2** 针对 [**kotlinx-serialization**](https://github.com/Kotlin/kotlinx.serialization) 所实现的序列化模块。
**CatCode2 Serialization Kotlinx** 允许通过实体类作为一个猫猫码的基本载体使用。

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