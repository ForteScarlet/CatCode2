<div align="center">
    <img src="./.catcode2/logo.png" alt="catcode logo"/>
    <h3>
        - 😺 CatCode 2 😺 -
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

</div>

*****

<div align="center">
    <h3>
        猫猫码（Cat code），一个可爱的通用特殊码，CQ码的精神延续。
    </h3>
</div>   

猫猫码是一个具有特定格式的字符串格式编码，它看上去是这个样子的 👉 `[CAT:xxx,param1=value1,param2=value2]`

- 以`[`开头，`]`结尾。
- 首先是猫猫码的头标识(大小写数字或下划线，标准应为`CAT`)
- 头标识后是猫猫码的类型(大小写数字或下划线)，与头标识之间使用冒号`:`分割。
- 参数为多个key不重复的键值对，一对参数使用`=`连接键与值，多对参数使用`,`分割。
- 可以没有参数。
- 区分大小写。

### **猫猫是严格的。**

为了避免混淆整个存在猫猫码的文本中不允许出现：`[`、`]`、`&`
字符和制表符与换行符，猫猫码文本内中除了上述字符外，还不允许出现`,`与`=`字符。
因此，猫猫码的转义规则为：

- `&`  ->  `&amp;`
- `[`  ->  `&#91;`
- `]`  ->  `&#93;`
- `,`  ->  `&#44;`
- `=`  ->  `&#61;`
- `\n` ->  `&#13;`
- `\r` ->  `&#10;`
- `\t` ->  `&#09;`

### **猫猫是包容的。**

任何符合规则 `[HEAD:type,param=value,param=value,...]` 的特殊码均可以视为猫猫码，尽管 `HEAD` 可能并不是 `CAT`。

### **猫猫是无处不在的。**

**CatCode2** 核心库基于 [**Kotlin Multiplatform**](https://kotlinlang.org/docs/multiplatform.html) 支持多平台（JVM、JS、Native），
并且 **CatCode2** 提供了基于 [**kotlinx-serialization**](https://github.com/Kotlin/kotlinx.serialization) 的多平台序列化模块支持，
你可以借助 **kotlinx-serialization** 来自定义结构来描述你的猫猫码、序列化/反序列化你的猫猫码。

## 安装

### JVM

**Gradle Groovy**

```groovy
// 核心库
implementation 'love.forte.catcode2:catcode2-core:$catcode2_version'

// ktx序列化库
implementation 'love.forte.catcode2:catcode2-serialization-kotlinx:$catcode2_version'
```

**Gradle Kotlin DSL**

```kotlin
// 核心库
implementation("love.forte.catcode2:catcode2-core:$catcode2_version")

// ktx序列化库
implementation("love.forte.catcode2:catcode2-serialization-kotlinx:$catcode2_version")
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

### JS

**npm**

> npm package see: https://www.npmjs.com/package/@catcode2/core

```shell
npm i @catcode2/core
```

_参考 [core/README-JS](catcode2-core/README-JS.md)_


### Native

前往 [releases](https://github.com/ForteScarlet/CatCode2/releases)
选择你需要的版本，并在其文件列表中找到你所需要某平台下的动态链文件和 `.h` 头文件，
例如 `catcode2_core_mingwX64.dll` 和 `catcode2_core_api.h` 。



