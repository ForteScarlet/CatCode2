## 安装

**Gradle Groovy**

```groovy
implementation 'love.forte.catcode2:catcode2-core:$catcode2_version'
```

**Gradle Kotlin DSL**

```kotlin
implementation("love.forte.catcode2:catcode2-core:$catcode2_version")
```

> 在 `Gradle` 中，如果是直接导入而不使用Kotlin，则需要在坐标名候面追加平台后缀，例如 `-jvm`，参考下述 `Maven` 坐标。

**Maven**

```xml

<dependencies>
    <!-- 核心库 -->
    <dependency>
        <groupId>love.forte.catcode2</groupId>
        <artifactId>catcode2-core-jvm</artifactId>
        <version>${catcode2.version}</version>
        <scope>compile</scope>
    </dependency>

    <!-- ktx序列化库 -->
    <dependency>
        <groupId>love.forte.catcode2</groupId>
        <artifactId>catcode2-serialization-kotlinx-jvm</artifactId>
        <version>${catcode2.version}</version>
        <scope>compile</scope>
    </dependency>
</dependencies>
```

## 应用

遍历一个猫猫码。

<details open>
<summary>Kotlin</summary>

```kotlin
val catcodeValue = "[CAT:code,k1=v1,name=forte,tar=foo]"

val properties: MutableList<String> = ArrayList(2)

walkCatCodeContinuously(
    catcodeValue,
    true,
    { head: String -> println("head = $head") },
    { type: String -> println("type = $type") }
) { key: String, value: String -> properties.add("$key: $value") }

println(properties)
```

输出：

```
head = CAT
type = code
[k1: v1, name: forte, tar: foo]
```

</details>

<details>
<summary>Java</summary>

```java
final String catcodeValue = "[CAT:code,k1=v1,name=forte,tar=foo]";

List<String> properties = new ArrayList<>(2);

CatCodes.walkCatCodeContinuously(catcodeValue, true, head -> {
    System.out.println("head = " + head);
}, type -> {
    System.out.println("type = " + type);
}, (key, value) -> {
    properties.add(key + ": " + value);
});

System.out.println(properties);
```

输出：

```
head = CAT
type = code
[k1: v1, name: forte, tar: foo]
```

</details>

或者分开操作？

<details open>
<summary>Kotlin</summary>

```kotlin
val catcodeValue = "[CAT:code,k1=v1,name=forte,tar=foo]"

val head = getCatCodeHead(catcodeValue)
println(head)

val type = getCatCodeType(catcodeValue)
println(type)

walkCatCodePropertiesContinuously(catcodeValue, true) { k, v ->
    println("$k: $v")
}
```

输出：

```
CAT
code
k1: v1
name: forte
tar: foo
```

</details>

<details>
<summary>Java</summary>

```java
final String catcodeValue = "[CAT:code,k1=v1,name=forte,tar=foo]";

final String head = CatCodes.getCatCodeHead(catcodeValue);
System.out.println("head = " + head);

final String type = CatCodes.getCatCodeType(catcodeValue);
System.out.println("type = " + type);

CatCodes.walkCatCodePropertiesContinuously(catcodeValue, true, (key, value) -> {
    System.out.println(key + ": " + value);
});
```

输出：

```
CAT
code
k1: v1
name: forte
tar: foo
```

</details>

得到一个猫猫码实例：

<details open>
<summary>Kotlin</summary>

```kotlin
val cat = catOf("[CAT:at,code=123]")
println(cat)                    // [CAT:at,code=123] | same as cat.toString()
println(cat.code)               // [CAT:at,code=123] | same as cat.toString(), same as cat.toCode()
println(cat.toCode("CQ"))       // [CQ:at,code=123]
println(cat["code"])            // 123
println(cat.head)               // CAT
println(cat.type)               // at
println(cat.keys)               // [code]
```

</details>

<details>
<summary>Java</summary>

```java
final Cat cat = Cats.of("[CAT:at,code=123]");
System.out.println(cat);                // [CAT:at,code=123]
System.out.println(cat.toCode(null));   // [CAT:at,code=123]
System.out.println(cat.toCode("CQ"));   // [CQ:at,code=123]
System.out.println(cat.get("code"));    // 123
System.out.println(cat.getHead());      // CAT
System.out.println(cat.getType());      // at
System.out.println(cat.getKeys());      // [code]
```

</details>

构建一个猫猫码实例：

<details open>
<summary>Kotlin</summary>

```kotlin
val cat = catOf(
    "type", "HEAD",
    mapOf(
        "name" to "forte",
        "age" to "18"
    )
)

println(cat) // [HEAD:type,name=forte,age=18]
```

</details>

<details>
<summary>Java</summary>

```java
final HashMap<String, String> properties = new HashMap<>();
properties.put("name", "forte");
properties.put("age", "18");

final Cat cat = Cats.of("type", "HEAD", properties);

System.out.println(cat); // [HEAD:type,name=forte,age=18]
```

</details>