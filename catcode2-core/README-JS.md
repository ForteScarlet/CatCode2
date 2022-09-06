# CatCode2 - Core

CatCode2的核心模块。


## 安装

```shell
npm i @catcode2/core
```

## 引用

```js
const catcode2 = require('@catcode2/core').catcode2
```

> Kotlin/JS 暂时不支持 ES6。详情参考 [KT-8373](https://youtrack.jetbrains.com/issue/KT-8373)


## 应用

遍历一个猫猫码。

```js
const catcodeValue = '[CAT:code,k1=v1,name=forte,tar=foo]'

let catHead, catType;
let properties = [];

catcode2.walkCatCode(
    catcodeValue, true,
    (head) => {
        catHead = head
    },
    (type) => {
        catType = type
    },
    (key, value) => {
        properties.push(key + ': ' + value)
    }
)

console.log(catHead)    // CAT
console.log(catType)    // code
console.log(properties) // [ 'k1: v1', 'name: forte', 'tar: foo' ]
```

或者分开操作？

```js

```


得到一个猫猫码实例。

```js
const cat = catcode2.cat.catOfLiteral('[CAT:at,code=123]')

const code = cat.get('code')    // 123
const catcode = cat.toString()  // [CAT:at,code=123]
const catHead = cat.head        // CAT
const catHead = cat.type        // at
const keys = cat.keys           // [ 'code' ]
```

