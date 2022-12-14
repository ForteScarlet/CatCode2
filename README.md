<div align="center">
    <img src="./.catcode2/logo.png" alt="catcode logo"/>
    <h3>
        - ðº CatCode â¡ ðº -
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
            <img src="https://img.shields.io/npm/v/@catcode2/core/latest" alt="catcode2/core-latest" />
    </a>
    <a href="https://www.npmjs.com/package/@catcode2/core" target="_blank" >
        <img src="https://img.shields.io/npm/v/@catcode2/core/snapshot" alt="catcode2/core-snapshot" />
    </a>
</div>

*****

<div align="center">
    <h3>
        ç«ç«ç ï¼Cat Codeï¼ï¼ä¸ä¸ªå¯ç±çéç¨ç¹æ®ç ï¼CQç çç²¾ç¥å»¶ç»­ã
    </h3>
</div>   

> <small><i>CatCode1åå¾ [CatCode](https://github.com/ForteScarlet/CatCode) æ¥çã</i></small>

ç«ç«ç æ¯ä¸ä¸ªå·æç¹å®æ ¼å¼çå­ç¬¦ä¸²æ ¼å¼ç¼ç ï¼å®çä¸å»æ¯è¿ä¸ªæ ·å­ç ð `[CAT:xxx,param1=value1,param2=value2]`

- ä»¥`[`å¼å¤´ï¼`]`ç»å°¾ã
- é¦åæ¯ç«ç«ç çå¤´æ è¯(å¤§å°åæ°å­æä¸åçº¿ï¼æ ååºä¸º`CAT`)
- å¤´æ è¯åæ¯ç«ç«ç çç±»å(å¤§å°åæ°å­æä¸åçº¿)ï¼ä¸å¤´æ è¯ä¹é´ä½¿ç¨åå·`:`åå²ã
- åæ°ä¸ºå¤ä¸ªkeyä¸éå¤çé®å¼å¯¹ï¼ä¸å¯¹åæ°ä½¿ç¨`=`è¿æ¥é®ä¸å¼ï¼å¤å¯¹åæ°ä½¿ç¨`,`åå²ã
- å¯ä»¥æ²¡æåæ°ã
- åºåå¤§å°åã

### **ç«ç«æ¯ä¸¥æ ¼çã**

ä¸ºäºé¿åæ··æ·æ´ä¸ªå­å¨ç«ç«ç çææ¬ä¸­ä¸åè®¸åºç°ï¼`[`ã`]`ã`&`
å­ç¬¦åå¶è¡¨ç¬¦ä¸æ¢è¡ç¬¦ï¼ç«ç«ç ææ¬åä¸­é¤äºä¸è¿°å­ç¬¦å¤ï¼è¿ä¸åè®¸åºç° `,` ä¸ `=` å­ç¬¦ã
å æ­¤ï¼ç«ç«ç çè½¬ä¹è§åä¸ºï¼

| æº    | è½¬       |
|------|---------|
| `&`  | `&amp;` |
| `[`  | `&#91;` |
| `]`  | `&#93;` |
| `,`  | `&#44;` |
| `=`  | `&#61;` |
| `\n` | `&#13;` |
| `\r` | `&#10;` |
| `\t` | `&#09;` |


### **ç«ç«æ¯åå®¹çã**

ä»»ä½ç¬¦åè§å `[HEAD:type,param=value,param=value,...]` çç¹æ®ç åå¯ä»¥è§ä¸ºç«ç«ç ï¼å°½ç®¡ `HEAD` å¯è½å¹¶ä¸æ¯ `CAT`ã

### **ç«ç«æ¯æ å¤ä¸å¨çã**

**CatCode2** æ ¸å¿åºåºäº [**Kotlin Multiplatform**](https://kotlinlang.org/docs/multiplatform.html) æ¯æå¤å¹³å°ï¼JVMãJSãNativeï¼ï¼
å¹¶ä¸ **CatCode2** æä¾äºåºäº [**kotlinx-serialization**](https://github.com/Kotlin/kotlinx.serialization) çå¤å¹³å°åºååæ¨¡åæ¯æï¼
ä½ å¯ä»¥åå© **kotlinx-serialization** æ¥èªå®ä¹ç»ææ¥æè¿°ä½ çç«ç«ç ãåºåå/ååºååä½ çç«ç«ç ã

## åºç¨

### [æ ¸å¿åº](catcode2-core)

[æ ¸å¿åº](catcode2-core) æä¾äºéå¯¹ç«ç«ç å­ç¬¦ä¸²çè§£æãè¯»åä¸å°è£çæ ¸å¿åºç¡åè½ï¼æ¯æä¸»è¦ä¹æ¯æåºæ¬çæ¨¡åã

åå¾ [æ ¸å¿åºæ¨¡å](catcode2-core) è·åæ´å¤ä¿¡æ¯ã

### [åºåååº](catcode2-serialization)

**CatCode2** æä¾äºä¸äºåè®¸éè¿å®ä½ç±»ä½ä¸ºè½½ä½æ¥å¯¹ç«ç«ç è¿è¡æè¿°çæ¨¡åï¼
ä¾å¦å¯¹ [**kotlinx-serialization**](https://github.com/Kotlin/kotlinx.serialization) çå®ç°ã

åå¾ [åºååæ¨¡å](catcode2-serialization) è·åæ´å¤ä¿¡æ¯ã


## éè§

```kotlin
walkCatCodeContinuously(
    "[CAT:code,k1=v1,name=forte,tar=foo]",
    true,
    { head: String -> println("head = $head") },
    { type: String -> println("type = $type") }
) { key: String, value: String -> println("$key: $value") }
```

```kotlin
val cat = catOf("[CAT:at,code=123]")

cat.code
cat.toCode("CQ")
cat.head
cat.type
cat.keys

val code: Int by cat.provider.int
```

```kotlin
val cat = buildCat("foo", "CAT") {
    "key" - "value"
    set("foo", "bar", false)
    this["age"] = "18"
    key("name") value "forte"
}

val catCode = buildCatLiteral("foo") {
    "key" - "value"
    set("foo", "bar", false)
    this["age"] = "18"
    key("name") value "forte"
}
```


```kotlin
@Serializable
@SerialName("at") // or use @CatCodeTypeName("at")
data class At(val code: Long, val name: String)

fun sample() {
    val at = At(123456L, "forte")
    val catcode = CatCode.encodeToString(At.serializer(), at)
    val newAt = CatCode.decodeFromString(At.serializer(), catcode)
    
    // ...
}
```

## LICENSE

[MIT LICENSE](LICENSE).