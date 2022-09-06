@file:JvmName("CatCodes")
@file:JvmMultifileClass

package catcode2

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import kotlin.js.JsExport
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
 *     perceiveHead = { head -> /* ... */ WalkResult.CONTINUE },
 *     perceiveType = { type -> /* ... */ WalkResult.CONTINUE },
 * ) { key, value ->
 *    // ...
 *    WalkResult.CONTINUE
 * }
 * ```
 *
 * JS
 * ```js
 * catcode2.walkCatCode(
 *    "[CAT:foo,bar=tar,name=forte]",
 *    true,
 *    (head) => { /* ... */ return catcode2.WalkResult.CONTINUE },
 *    (type) => { /* ... */ return catcode2.WalkResult.CONTINUE },
 *    (key, value) => { /* ... */ return catcode2.WalkResult.CONTINUE }
 * )
 * ```
 *
 * ```java
 * CatCodes.walkCatCode(
 *    "[CAT:foo,bar=tar,name=forte]",
 *    true,
 *    head -> { /* ... */ return WalkResult.CONTINUE; },
 *    type -> { /* ... */ return WalkResult.CONTINUE; },
 *    (key, value) -> { /* ... */ return WalkResult.CONTINUE; }
 * );
 * ```
 *
 * @param catCode 进行解析的catcode字符串
 * @param perceiveHead 得到被解析的head。将会是第一个被触发的perceive函数，应当至少被触发一次
 * @param perceiveType 得到被解析的type。将会是第二个被触发的perceive函数，应当至少被触发一次
 * @param perceiveProperty
 *
 * @throws IllegalArgumentException 当 [catCode] 可能不符合标准结构时。
 * @throws IllegalArgumentException 当一个属性切割符 [CAT_PROPERTIES_SEPARATOR] 后面无法寻得有效键值对（缺少键值切割符 [CAT_PROPERTY_SEPARATOR] 时）
 *
 */
@OptIn(ExperimentalContracts::class)
@JvmName("walkCatCode")
@JsExport
public inline fun walkCatCode(
    catCode: String,
    decodeValue: Boolean = true,
    crossinline perceiveHead: (String) -> WalkResult? = { WalkResult.CONTINUE },
    crossinline perceiveType: (String) -> WalkResult? = { WalkResult.CONTINUE },
    crossinline perceiveProperty: (key: String, value: String) -> WalkResult?,
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

/**
 * 解析一个猫猫码字符串中的所有属性, 包括 `head`、`type` 和所有的属性键值对。
 *
 * Kotlin
 * ```kotlin
 * walkCatCodeContinuously(
 *     codeValue = "[CAT:foo,bar=tar,name=forte]",
 *     decodeValue = true,
 *     perceiveHead = { head -> /* ... */ },
 *     perceiveType = { type -> /* ... */ },
 * ) { key, value ->
 *    // ...
 *    WalkResult.CONTINUE
 * }
 * ```
 *
 * JS
 * ```js
 * catcode2.walkCatCodeContinuously(
 *    "[CAT:foo,bar=tar,name=forte]",
 *    true,
 *    (head) => { /* ... */ },
 *    (type) => { /* ... */ },
 *    (key, value) => { /* ... */ }
 * )
 * ```
 *
 * ```java
 * CatCodes.walkCatCodeContinuously(
 *    "[CAT:foo,bar=tar,name=forte]",
 *    true,
 *    head -> { /* ... */ return Unit.INSTANCE; },
 *    type -> { /* ... */ return Unit.INSTANCE; },
 *    (key, value) -> { /* ... */ return Unit.INSTANCE; }
 * );
 * ```
 *
 * @param catCode 进行解析的catcode字符串
 * @param perceiveHead 得到被解析的head。将会是第一个被触发的perceive函数，应当至少被触发一次
 * @param perceiveType 得到被解析的type。将会是第二个被触发的perceive函数，应当至少被触发一次
 * @param perceiveProperty
 *
 * @throws IllegalArgumentException 当 [catCode] 可能不符合标准结构时。
 * @throws IllegalArgumentException 当一个属性切割符 [CAT_PROPERTIES_SEPARATOR] 后面无法寻得有效键值对（缺少键值切割符 [CAT_PROPERTY_SEPARATOR] 时）
 *
 */
@OptIn(ExperimentalContracts::class)
@JvmName("walkCatCodeContinuously")
@JsExport
public inline fun walkCatCodeContinuously(
    catCode: String,
    decodeValue: Boolean = true,
    crossinline perceiveHead: (String) -> Unit = { },
    crossinline perceiveType: (String) -> Unit = { },
    crossinline perceiveProperty: (key: String, value: String) -> Unit,
) {
    contract {
        callsInPlace(perceiveHead, InvocationKind.AT_MOST_ONCE)
        callsInPlace(perceiveType, InvocationKind.AT_MOST_ONCE)
        callsInPlace(perceiveProperty, InvocationKind.UNKNOWN)
    }
    
    walkCatCode(
        catCode, decodeValue,
        perceiveHead = {
            perceiveHead(it)
            WalkResult.CONTINUE
        },
        perceiveType = {
            perceiveType(it)
            WalkResult.CONTINUE
            
        },
        perceiveProperty = { k, v ->
            perceiveProperty(k, v)
            WalkResult.CONTINUE
        },
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
    headEndIndex: Int,
    perceive: (startIndex: Int, endIndex: Int) -> Unit,
    notFound: () -> Unit = {
        throw IllegalArgumentException("codeValue '$this' is not a catcode: type not found by cat properties separator '$CAT_PROPERTIES_SEPARATOR'")
    },
    typeEmpty: () -> Unit = {
        throw IllegalArgumentException("codeValue '$this' is not a catcode: type is empty before cat properties separator '$CAT_PROPERTIES_SEPARATOR'")
    },
): Int {
    var typeEndIndex = indexOf(CAT_PROPERTIES_SEPARATOR, headEndIndex)
    
    when {
        typeEndIndex < 0 -> {
            val lastIndex = lastIndex
            // not found, maybe last
            if (lastIndex - 1 == headEndIndex) {
                notFound()
            }
            typeEndIndex = lastIndex
        }
        
        typeEndIndex - 1 == headEndIndex -> typeEmpty()
    }
    
    perceive(headEndIndex + 1, typeEndIndex)
    return typeEndIndex
}

/**
 * @throws IllegalArgumentException
 */
@PublishedApi
internal inline fun walkCatCodeInternal(
    catCode: String,
    perceiveHead: (startIndex: Int, endIndex: Int) -> WalkResult?,
    perceiveType: (startIndex: Int, endIndex: Int) -> WalkResult?,
    perceiveProperty: (startIndex: Int, separatorIndex: Int, endIndex: Int) -> WalkResult?,
) {
    if (!checkCatCodeLoosely(catCode)) throw IllegalArgumentException("codeValue '$catCode' is not a catcode: Not wrapped by '$CAT_PREFIX' and '$CAT_SUFFIX' or length < 5")
    
    
    // find head
    val headEndIndex = catCode.targetCatCodeHead(perceive = { s, e ->
        if (perceiveHead(s, e) == WalkResult.STOP) {
            return
        }
    })
    
    
    // find type
    val typeEndIndex = catCode.targetCatCodeType(
        headEndIndex = headEndIndex,
        perceive = { s, e ->
            if (perceiveType(s, e) == WalkResult.STOP) {
                return
            }
        }
    )
    
    walkCatCodePropertiesInlineLooselyInternal(typeEndIndex, catCode) { s, spi, e ->
        if (perceiveProperty(s, spi, e) == WalkResult.STOP) {
            return
        }
    }
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
    val headEndIndex = catCode.targetCatCodeHead({ _, _ -> })
    
    lateinit var type: String
    requireCatCodeLoosely(catCode).targetCatCodeType(
        headEndIndex = headEndIndex,
        perceive = { s, e ->
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
    
    val headEndIndex = catCode.targetCatCodeHead({ _, _ -> }, { /* ignore. */ })
    if (headEndIndex < 0) {
        return null
    }
    
    var type: String? = null
    catCode.targetCatCodeType(
        headEndIndex = headEndIndex,
        perceive = { s, e ->
            type = catCode.substring(s, e)
        }) { /* ignore. */ }
    
    return type
}

/**
 * 尝试依次遍历一个猫猫码字符串中的所有属性键值对。
 *
 * @throws IllegalArgumentException 当 [codeValue] 无法通过 [checkCatCode] 校验时。（see [requireCatCode]）
 * @throws IllegalArgumentException 当一个属性切割符 [CAT_PROPERTIES_SEPARATOR] 后面无法寻得有效键值对（缺少键值切割符 [CAT_PROPERTY_SEPARATOR] 时）
 *
 * @see walkCatCodePropertiesLoosely
 */
@OptIn(ExperimentalContracts::class)
@JsExport
@JvmOverloads
@JvmName("walkCatCodeProperties")
public inline fun walkCatCodeProperties(
    codeValue: String,
    decodeValue: Boolean = true,
    walk: (key: String, value: String) -> WalkResult?,
) {
    contract {
        callsInPlace(walk, InvocationKind.UNKNOWN)
    }
    
    walkCatCodePropertiesLoosely(requireCatCode(codeValue), decodeValue, walk)
}

/**
 * 尝试依次遍历一个猫猫码字符串中的所有属性键值对。
 *
 * @throws IllegalArgumentException 当 [codeValue] 无法通过 [checkCatCode] 校验时。（see [requireCatCode]）
 * @throws IllegalArgumentException 当一个属性切割符 [CAT_PROPERTIES_SEPARATOR] 后面无法寻得有效键值对（缺少键值切割符 [CAT_PROPERTY_SEPARATOR] 时）
 *
 * @see walkCatCodePropertiesLoosely
 */
@OptIn(ExperimentalContracts::class)
@JsExport
@JvmOverloads
@JvmName("walkCatCodePropertiesContinuously")
public inline fun walkCatCodePropertiesContinuously(
    codeValue: String,
    decodeValue: Boolean = true,
    walk: (key: String, value: String) -> Unit,
) {
    contract {
        callsInPlace(walk, InvocationKind.UNKNOWN)
    }
    
    walkCatCodePropertiesLoosely(requireCatCode(codeValue), decodeValue) { k, v ->
        walk(k, v)
        WalkResult.CONTINUE
    }
}


/**
 * 尝试依次遍历一个猫猫码字符串中的所有属性键值对。
 *
 * 为了提供更低的损耗，[walkCatCodePropertiesLoosely] 的遍历与检测**十分宽松**。对于 [codeValue] 的合规检测来说，
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
@JsExport
@JvmOverloads
@JvmName("walkCatCodePropertiesLoosely")
public inline fun walkCatCodePropertiesLoosely(
    codeValue: String,
    decodeValue: Boolean = true,
    walk: (key: String, value: String) -> WalkResult?,
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


/**
 * 尝试依次遍历一个猫猫码字符串中的所有属性键值对。
 *
 * 为了提供更低的损耗，[walkCatCodePropertiesLoosely] 的遍历与检测**十分宽松**。对于 [codeValue] 的合规检测来说，
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
@JsExport
@JvmOverloads
@JvmName("walkCatCodePropertiesLooselyContinuously")
public inline fun walkCatCodePropertiesLooselyContinuously(
    codeValue: String,
    decodeValue: Boolean = true,
    walk: (key: String, value: String) -> Unit,
) {
    contract {
        callsInPlace(walk, InvocationKind.UNKNOWN)
    }
    
    return walkCatCodePropertiesLoosely(
        codeValue, decodeValue
    ) { k, v ->
        walk(k, v)
        WalkResult.CONTINUE
    }
}

@PublishedApi
internal inline fun walkCatCodePropertiesInlineLoosely0(
    startIndex0: Int,
    codeValue: String,
    valuePreprocess: (String) -> String,
    walk: (key: String, value: String) -> WalkResult?,
) {
    walkCatCodePropertiesInlineLooselyInternal(startIndex0, codeValue) { si, spi, ei ->
        val key = codeValue.substring(si, spi)
        val value = codeValue.substring(spi + 1, ei)
        if (walk(key, valuePreprocess(value)) == WalkResult.STOP) {
            return
        }
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
 * 用于与 `walk` 相关的API中，作为控制流返回值使用。
 */
@JsExport
public enum class WalkResult {
    /**
     * 代表 **继续**。大多数情况下应当是默认的类型，代表继续后续流程或逻辑。
     */
    CONTINUE,
    
    // BREAK?
    
    /**
     * 代表 **终止**。会立即停止walk行为，并抛弃后续所有的逻辑。
     */
    STOP
}