## 安装

前往 [releases](https://github.com/ForteScarlet/CatCode2/releases)
选择你需要的版本，并在其文件列表中找到你所需要某平台下的动态链文件和 `.h` 头文件，
例如 `catcode2_core_mingwX64.dll` 和 `catcode2_core_api.h` 。

## 应用

> 下述代码以 **`mingwX64`** 平台为例。
 
得到猫猫码的头与类型：

```cpp
#include <iostream>
#include "catcode2_core_api.h"

int main() {
    // 入口点
    catcode2_core_ExportedSymbols *catcodeSymbols = catcode2_core_symbols();

    const char *catcode = "[CAT:code]";

    printf("code = %s\n", catcode);

    const char *head = catcodeSymbols->kotlin.root.catcode2.getCatCodeHead(catcode);
    const char *type = catcodeSymbols->kotlin.root.catcode2.getCatCodeType(catcode);


    printf("head = %s\n", head);
    printf("type = %s\n", type);

    // 释放字符串
    catcodeSymbols->DisposeString(head);
    catcodeSymbols->DisposeString(type);

    return 0;
}
```

输出：

```
code = [CAT:code]
head = CAT
type = code
```


使用猫猫码本体：

```cpp
int main() {
    // 入口点
    catcode2_core_ExportedSymbols *catcodeSymbols = catcode2_core_symbols();

    const char *catcode = "[CAT:code,name=forte]";

    const catcode2_core_kref_catcode2_cat_Cat &cat = catcodeSymbols->kotlin.root.catcode2.cat.catOf(catcode);

    const char *catHead = catcodeSymbols->kotlin.root.catcode2.cat.Cat.get_head(cat);
    const char *catType = catcodeSymbols->kotlin.root.catcode2.cat.Cat.get_type(cat);
    const char *nameValue = catcodeSymbols->kotlin.root.catcode2.cat.Cat.get(cat, "name");
    const char *codeValue = catcodeSymbols->kotlin.root.catcode2.cat.Cat.toString(cat);


    printf("cat head    = %s\n", catHead);
    printf("cat type    = %s\n", catType);
    printf("cat['name'] = %s\n", nameValue);
    printf("cat         = %s\n", codeValue);


    // 释放相关内容
    catcodeSymbols->DisposeString(catHead);
    catcodeSymbols->DisposeString(catType);
    catcodeSymbols->DisposeString(nameValue);
    catcodeSymbols->DisposeString(codeValue);

    catcodeSymbols->DisposeStablePointer(cat.pinned);

    return 0;
}
```

输出：

```
cat head    = CAT
cat type    = code
cat['name'] = forte
cat         = [CAT:code,name=forte]
```

或者使用构建器：

```cpp
int main() {
    // 入口点
    catcode2_core_ExportedSymbols *catcodeSymbols = catcode2_core_symbols();

    const catcode2_core_kref_catcode2_cat_CatCodeBuilder_Companion &catCodeBuilderInstance = catcodeSymbols->kotlin.root.catcode2.cat.CatCodeBuilder.Companion._instance();
    const catcode2_core_kref_catcode2_cat_CatCodeBuilder &builder = catcodeSymbols->kotlin.root.catcode2.cat.CatCodeBuilder.Companion.of(
            catCodeBuilderInstance, "foo", "CAT");

    catcodeSymbols->kotlin.root.catcode2.cat.CatCodeBuilder.set(builder, "name", "forte", true);
    catcodeSymbols->kotlin.root.catcode2.cat.CatCodeBuilder.set(builder, "age", "18", false);

    const catcode2_core_kref_catcode2_cat_Cat &cat = catcodeSymbols->kotlin.root.catcode2.cat.CatCodeBuilder.build(
            builder);

    printf("%s", catcodeSymbols->kotlin.root.catcode2.cat.Cat.toString(cat));

    catcodeSymbols->DisposeStablePointer(cat.pinned);
    catcodeSymbols->DisposeStablePointer(builder.pinned);
    catcodeSymbols->DisposeStablePointer(catCodeBuilderInstance.pinned);

    return 0;
}
```

输出：

```
[CAT:foo,name=forte,age=18]
```

或者直接构建字符串？

```cpp
int main() {
    // 入口点
    catcode2_core_ExportedSymbols *catcodeSymbols = catcode2_core_symbols();

    const catcode2_core_kref_catcode2_cat_CatCodeLiteralBuilder_Companion &catCodeBuilderInstance = catcodeSymbols->kotlin.root.catcode2.cat.CatCodeLiteralBuilder.Companion._instance();
    const catcode2_core_kref_catcode2_cat_CatCodeLiteralBuilder &builder = catcodeSymbols->kotlin.root.catcode2.cat.CatCodeLiteralBuilder.Companion.of(
            catCodeBuilderInstance, "foo", "CAT");

    catcodeSymbols->kotlin.root.catcode2.cat.CatCodeLiteralBuilder.set(builder, "name", "forte", true);
    catcodeSymbols->kotlin.root.catcode2.cat.CatCodeLiteralBuilder.set(builder, "age", "18", false);

    const char *cat = catcodeSymbols->kotlin.root.catcode2.cat.CatCodeLiteralBuilder.build(
            builder);

    printf("code = %s", cat);

    catcodeSymbols->DisposeString(cat;
    catcodeSymbols->DisposeStablePointer(builder.pinned);
    catcodeSymbols->DisposeStablePointer(catCodeBuilderInstance.pinned);

    return 0;
}

```

输出：

```
[CAT:foo,name=forte,age=18]
```