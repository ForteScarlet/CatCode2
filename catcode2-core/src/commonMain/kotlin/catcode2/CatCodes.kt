@file:JvmName("CatCodes")
@file:JvmMultifileClass

package catcode2

import catcode2.annotation.Api4Jvm
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import kotlin.js.JsExport
import kotlin.js.JsName
import kotlin.jvm.JvmMultifileClass
import kotlin.jvm.JvmName
import kotlin.jvm.JvmOverloads
import kotlin.jvm.JvmSynthetic


// [HEAD:type,a=b,c=d]

/**
 * 解析一个猫猫码字符串中的所有属性, 包括 `head`、`type` 和所有的属性键值对。
 *
 * Kotlin
 * ```kotlin
 * walkCatCode(
 *     codeValue = "[CAT:foo,bar=tar,name=forte]",
 *     decodeValue = true,
 *     perceiveHead = { head -> /* ... */ },
 *     perceiveType = { type -> /* ... */ },
 * ) { key, value ->
 *    // ...
 * }
 * ```
 *
 * JS
 * ```js
 * catcode2.walkCatCode(
 *    "[CAT:foo,bar=tar,name=forte]",
 *    true,
 *    (head) => { /* ... */ },
 *    (type) => { /* ... */ },
 *    (key, value) => { /* ... */ }
 * )
 * ```
 *
 * @param catCode 进行解析的catcode字符串
 * @param perceiveHead 得到被解析的head。将会是第一个被触发的perceive函数，应当至少被触发一次
 * @param perceiveType 得到被解析的type。将会是第二个被触发的perceive函数，应当至少被触发一次
 * @param perceiveProperty
 *
 * @throws IllegalArgumentException 当 [catCode] 可能不符合标准结构时。
 * @throws IllegalArgumentException 当一个属性切割符 [CAT_PROPERTIES_SEPARATOR] 后面无法寻得有效键值对（缺少键值切割符 [CAT_PROPERTY_SEPARATOR] 时）
 */
@OptIn(ExperimentalContracts::class)
@JvmSynthetic // TODO for Java
@JsName("walkCatCode")
@JsExport
public inline fun walkCatCode(
    catCode: String,
    decodeValue: Boolean = true,
    crossinline perceiveHead: (String) -> Unit = {},
    crossinline perceiveType: (String) -> Unit = {},
    crossinline perceiveProperty: (key: String, value: String) -> Unit,
) {
    contract {
        callsInPlace(perceiveHead, InvocationKind.AT_MOST_ONCE)
        callsInPlace(perceiveType, InvocationKind.AT_MOST_ONCE)
        callsInPlace(perceiveProperty, InvocationKind.UNKNOWN)
    }
    
    
    walkCatCodeInternal(
        catCode,
        { s, e -> perceiveHead(requireCatHead(catCode.substring(s, e))) },
        { s, e -> perceiveType(requireCatType(catCode.substring(s, e))) },
        if (decodeValue) {
            { si, spi, ei ->
                val key = catCode.substring(si, spi)
                val value = catCode.substring(spi + 1, ei)
                perceiveProperty(key, decodeCatParam(value))
            }
        } else {
            { si, spi, ei ->
                val key = catCode.substring(si, spi)
                val value = catCode.substring(spi + 1, ei)
                perceiveProperty(key, value)
            }
        }
    )
}


@PublishedApi
internal inline fun String.targetCatCodeHead(
    perceive: (startIndex: Int, endIndex: Int) -> Unit,
    notFound: () -> Unit = { throw IllegalArgumentException("codeValue '$this' is not a catcode: head not found by head-type separator '$CAT_HEAD_SEPARATOR'") },
): Int {
    val headEndIndex = indexOf(CAT_HEAD_SEPARATOR)
    if (headEndIndex < 0) {
        notFound()
    }
    
    perceive(1, headEndIndex)
    
    return headEndIndex
}


@PublishedApi
internal inline fun String.targetCatCodeType(
    startIndex: Int = 1, // 0 should be '['
    perceive: (startIndex: Int, endIndex: Int) -> Unit,
    notFound: () -> Unit = {
        throw IllegalArgumentException("codeValue '$this' is not a catcode: type not found by cat properties separator '$CAT_PROPERTIES_SEPARATOR'")
    },
    typeEmpty: () -> Unit = {
        throw IllegalArgumentException("codeValue '$this' is not a catcode: type is empty before cat properties separator '$CAT_PROPERTIES_SEPARATOR'")
    },
): Int {
    var typeEndIndex = indexOf(CAT_PROPERTIES_SEPARATOR, startIndex)
    
    when {
        typeEndIndex < 0 -> {
            val lastIndex = lastIndex
            // not found, maybe last
            if (lastIndex - 1 == startIndex) {
                notFound()
            }
            typeEndIndex = lastIndex
        }
        
        typeEndIndex - 1 == startIndex -> typeEmpty()
    }
    
    perceive(startIndex + 1, typeEndIndex)
    return typeEndIndex
}

/**
 * @throws IllegalArgumentException
 */
@PublishedApi
internal inline fun walkCatCodeInternal(
    catCode: String,
    perceiveHead: (startIndex: Int, endIndex: Int) -> Unit,
    perceiveType: (startIndex: Int, endIndex: Int) -> Unit,
    perceiveProperty: (startIndex: Int, separatorIndex: Int, endIndex: Int) -> Unit,
) {
    if (!checkCatCodeLoosely(catCode)) throw IllegalArgumentException("codeValue '$catCode' is not a catcode: Not wrapped by '$CAT_PREFIX' and '$CAT_SUFFIX' or length < 5")
    
    // find head
    val headEndIndex = catCode.targetCatCodeHead(perceiveHead)
    
    // find type
    val typeEndIndex = catCode.targetCatCodeType(
        startIndex = headEndIndex,
        perceive = perceiveType
    )
    
    walkCatCodePropertiesInlineLooselyInternal(typeEndIndex, catCode, perceiveProperty)
}


/**
 * 尝试获取 [catCode] 中的 head 部分。会对 [catCode] 进行 [宽松地][checkCatCodeLoosely] 验证，如果无法通过验证则抛出异常。
 *
 */
@JsExport
public fun getCatCodeHead(catCode: String): String {
    lateinit var head: String
    requireCatCodeLoosely(catCode).targetCatCodeHead(perceive = { s, e ->
        head = catCode.substring(s, e)
    })
    
    return head
}

/**
 * 尝试获取 [catCode] 中的 head 部分，当无法获取时得到null。
 *
 */
@JsExport
public fun getCatCodeHeadOrNull(catCode: String): String? {
    if (!checkCatCodeLoosely(catCode)) {
        return null
    }
    
    var head: String? = null
    catCode.targetCatCodeHead(perceive = { s, e ->
        head = catCode.substring(s, e)
    }) { /* ignore. */ }
    
    return head
}


/**
 * 尝试获取 [catCode] 中的 type 部分。会对 [catCode] 进行 [宽松地][checkCatCodeLoosely] 验证，如果无法通过验证则抛出异常。
 *
 */
@JsExport
public fun getCatCodeType(catCode: String): String {
    lateinit var type: String
    requireCatCodeLoosely(catCode).targetCatCodeType(perceive = { s, e ->
        type = catCode.substring(s, e)
    })
    
    return type
}

/**
 * 尝试获取 [catCode] 中的 type 部分，当无法获取时得到null。
 *
 */
@JsExport
public fun getCatCodeTypeOrNull(catCode: String): String? {
    if (!checkCatCodeLoosely(catCode)) {
        return null
    }
    
    var type: String? = null
    catCode.targetCatCodeType(perceive = { s, e ->
        type = catCode.substring(s, e)
    }) { /* ignore. */ }
    
    return type
}


/**
 * 尝试依次遍历一个猫猫码字符串中的所有属性键值对。
 *
 * 为了提供更低的损耗，[walkCatCodePropertiesInlineLoosely] 的遍历与检测**十分宽松**。对于 [codeValue] 的合规检测来说，
 * **只会**检测 [codeValue] 是否被 [`[`][CAT_PREFIX] 和 [`]`][CAT_SUFFIX] 前后包裹而不关心 `HEAD` 和 `TYPE`。
 *
 * 这种宽松地检测方式可能会使得一些类似下述格式的代码也能够被正常使用：
 * - `[a=b,c=d]` (只会检测到 `c=d`)
 * - `[ bc]`
 * - `[,a=b,c=d]`
 *
 * 如果想要保证严谨性，请在使用前先通过 [checkCatCode] 或 [requireCatCode] 进行检测。
 *
 * @throws IllegalArgumentException 当 [codeValue] 不是被 [`[`][CAT_PREFIX] 和 [`]`][CAT_SUFFIX] 前后包裹时。
 * @throws IllegalArgumentException 当一个属性切割符 [CAT_PROPERTIES_SEPARATOR] 后面无法寻得有效键值对（缺少键值切割符 [CAT_PROPERTY_SEPARATOR] 时）
 */
@OptIn(ExperimentalContracts::class)
@JvmSynthetic
@JsName("walkCatCodePropertiesInlineLoosely")
public inline fun walkCatCodePropertiesInlineLoosely(
    codeValue: String,
    decodeValue: Boolean = true,
    walk: (key: String, value: String) -> Unit,
) {
    contract {
        callsInPlace(walk, InvocationKind.UNKNOWN)
    }
    
    if (!checkCatCodeLoosely(codeValue)) throw IllegalArgumentException("codeValue '$codeValue' is not a catcode: Not wrapped by '$CAT_PREFIX' and '$CAT_SUFFIX' or length < 5")
    
    if (decodeValue) {
        walkCatCodePropertiesInlineLoosely0(
            codeValue.indexOf(CAT_PROPERTIES_SEPARATOR),
            codeValue,
            { decodeCatParam(it) },
            walk
        )
    } else {
        walkCatCodePropertiesInlineLoosely0(codeValue.indexOf(CAT_PROPERTIES_SEPARATOR), codeValue, { it }, walk)
    }
}


@PublishedApi
internal inline fun walkCatCodePropertiesInlineLoosely0(
    startIndex0: Int,
    codeValue: String,
    valuePreprocess: (String) -> String,
    walk: (key: String, value: String) -> Unit,
) {
    walkCatCodePropertiesInlineLooselyInternal(startIndex0, codeValue) { si, spi, ei ->
        val key = codeValue.substring(si, spi)
        val value = codeValue.substring(spi + 1, ei)
        walk(key, valuePreprocess(value))
    }
}


@PublishedApi
@JvmSynthetic
internal inline fun walkCatCodePropertiesInlineLooselyInternal(
    startIndex0: Int,
    codeValue: String,
    walk: (startIndex: Int, separatorIndex: Int, endIndex: Int) -> Unit,
) {
    val lastIndex = codeValue.lastIndex
    
    var startIndex = startIndex0
    
    // cannot be 0
    while (startIndex in 1 until lastIndex) {
        
        val propertyIndex = codeValue.indexOf(CAT_PROPERTY_SEPARATOR, startIndex)
        if (propertyIndex < 0) {
            // has ',', but no '=' ?
            throw IllegalArgumentException("Property separator '$CAT_PROPERTY_SEPARATOR' not found after properties separator '$CAT_PROPERTIES_SEPARATOR' of index $startIndex")
        }
        
        var endIndex = codeValue.indexOf(CAT_PROPERTIES_SEPARATOR, propertyIndex)
        if (endIndex < 0) {
            endIndex = lastIndex
        }
        
        walk(startIndex + 1, propertyIndex, endIndex)
        
        startIndex = endIndex
    }
}


/**
 * 遍历提供的 [codeValue] 中的全部键值对。
 */
@JvmName("walkProperties")
@JsName("walkCatCodeProperties")
@JvmOverloads
public fun walkCatCodeProperties(codeValue: String, decodeValue: Boolean = true, walker: CatCodeKeyValueWalker) {
    walkCatCodePropertiesLoosely(requireCatCode(codeValue), decodeValue, walker)
}

/**
 * 宽松的遍历提供的 [codeValue] 中的全部键值对。
 */
@JvmName("walkPropertiesLoosely")
@JsName("walkCatCodePropertiesLoosely")
@JvmOverloads
public fun walkCatCodePropertiesLoosely(
    codeValue: String,
    decodeValue: Boolean = true,
    walker: CatCodeKeyValueWalker,
) {
    walkCatCodePropertiesInlineLoosely(codeValue, decodeValue, walker::walk)
}

// 对Js不友好, 对Jvm友好
@Api4Jvm
public fun interface CatCodeKeyValueWalker {
    public fun walk(key: String, value: String)
}



