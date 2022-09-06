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
