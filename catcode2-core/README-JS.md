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
const catcodeValue = '[CAT:code,k1=v1,name=forte,tar=foo]'


const catHead = catcode2.getCatCodeHead(catcodeValue)
const catType = catcode2.getCatCodeType(catcodeValue)

let properties = []

catcode2.walkCatCodeProperties(catcodeValue, true, (key, value) => {
    properties.push(key + ': ' + value)
})

console.log(catHead)    // CAT
console.log(catType)    // code
console.log(properties) // [ 'k1: v1', 'name: forte', 'tar: foo' ]
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

构建一个猫猫码实例，

通过 `key-value` 对象数组：

```js
const cat = catcode2.cat.catOfProperties(
    'code',
    'CAT',
    [
        {key: 'name', value: 'forte'},
        {key: 'foo', value: 'tar'},
    ]
)

console.log(cat.get('name'))  // forte          
console.log(cat.toString())   // [CAT:code,name=forte,foo=tar]      
console.log(cat.head)         // CAT  
console.log(cat.type)         // code  
console.log(cat.keys)         // [ 'name', 'foo' ]  
```

`catOfProperties` 最后的数组参数，你需要保证数组内元素为对象，且对象内都至少存在 `key` 和 `value` 属性。

或者通过任意对象构建：

```js
const cat = catcode2.cat.catOfEntity(
    'code',
    'CAT',
    { name: 'forte', foo: 'tar' }
)
    
console.log(cat.get('name'))  // forte          
console.log(cat.toString())   // [CAT:code,name=forte,foo=tar]      
console.log(cat.head)         // CAT  
console.log(cat.type)         // code  
console.log(cat.keys)         // [ 'name', 'foo' ]  
```

需要注意的是，当使用对象时，属性值的类型不可为对象或数组，应保证元素都为字符串、布尔值、数值等基本数据类型。

也可以通过构建器构建结果。

```js
const builder = catcode2.cat.newCatCodeBuilder('code')

builder.set('name', 'forte')
builder.key('foo').value('tar')

builder.setDecode('age', '12')
builder.key('size').valueEncode('8')

const cat = builder.build()

console.log(cat.toString()) // [CAT:code,name=forte,foo=tar,age=12,size=8]
```

或者直接构建字符串。

```js
const builder = catcode2.cat.newCatCodeLiteralBuilder('at', 'CQ')

builder.set('qq', '1145141919810')

const cat = builder.build() // string

console.log(cat) // [CQ:at,qq=1145141919810]
```
