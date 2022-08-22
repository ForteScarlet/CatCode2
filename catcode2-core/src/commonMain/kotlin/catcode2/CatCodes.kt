@file:JvmName("CatCodes")

package catcode2

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import kotlin.js.JsName
import kotlin.jvm.JvmName
import kotlin.jvm.JvmOverloads
import kotlin.jvm.JvmSynthetic

/*
 * 猫猫码解析等相关内容的辅助工具类。
 *
 */


/**
 * 默认情况下猫猫码的 `head`。即 `[CAT:type,p=v]` 中的 `CAT` 所应代表的具体的值。
 */
public const val CAT_HEAD: String = "CAT"

/**
 * 标准猫猫码的起始符 `[`。
 */
public const val CAT_PREFIX: Char = '['

/**
 * 标准猫猫码的终止符 `]`。
 */
public const val CAT_SUFFIX: Char = ']'

/**
 * 用于分割`head` 和 `type` 之间的切割符。
 */
public const val CAT_HEAD_SEPARATOR: Char = ':'

/**
 * 用于分割不同键值对之间的切割符。
 */
public const val CAT_PROPERTIES_SEPARATOR: Char = ','

/**
 * 用于分割键与值之间的切割符。
 */
public const val CAT_PROPERTY_SEPARATOR: Char = '='


// [HEAD:type,a=b,c=d]
@Suppress("RegExpRedundantEscape") // '\\]' for nodejs.
private val CATCODE_CHECK_REGEX = Regex("\\[[a-zA-Z_.$]+:[a-zA-Z_.$]+(,.+=.*)*\\]")

private val HEAD_CHECK_REGEX = Regex("[a-zA-Z_.$]+")

private val TYPE_CHECK_REGEX = HEAD_CHECK_REGEX

/**
 * 校验 [head] 是否为一个标准的猫猫码head
 */
@JvmName("checkHead")
@JsName("checkCatHead")
public fun checkCatHead(head: String): Boolean = HEAD_CHECK_REGEX.matches(head)

/**
 * 校验 [head] 是否为一个标准的猫猫码head
 */
@JvmName("requireHead")
@JsName("requireCatHead")
public inline fun requireCatHead(head: String, lazyMessage: () -> String = { "head '$head' not match '[a-zA-Z_.\$]+'" }): String {
    require(checkCatHead(head), lazyMessage)
    return head
}

/**
 * 校验 [type] 是否为一个标准的猫猫码类型
 */
@JvmName("checkType")
@JsName("checkCatType")
public fun checkCatType(type: String): Boolean = TYPE_CHECK_REGEX.matches(type)

/**
 * 校验 [type] 是否为一个标准的猫猫码类型
 */
@JvmName("requireType")
@JsName("requireCatType")
public inline fun requireCatType(type: String, lazyMessage: () -> String = { "type '$type' not match '[a-zA-Z_.\$]+'" }): String {
    require(checkCatType(type), lazyMessage)
    return type
}

/**
 * 校验 [codeValue] 是否大概率为一个猫猫码字符串。
 */
@JvmName("check")
@JsName("checkCatCode")
public fun checkCatCode(codeValue: String): Boolean = CATCODE_CHECK_REGEX.matches(codeValue)


/**
 * 校验 [codeValue] 是否大概率为一个猫猫码字符串，如果无法通过 [checkCatCode] 的检测则会抛出 [IllegalArgumentException]。
 */
@JvmName("require")
@JsName("requireCatCode")
public inline fun requireCatCode(
    codeValue: String,
    lazyMessage: () -> String = { "code value '$codeValue' not a catcode" },
): String {
    require(checkCatCode(codeValue), lazyMessage)
    return codeValue
}

//
@PublishedApi
internal fun checkCatCodeLoosely(codeValue: String): Boolean {
    // [a:b]
    if (codeValue.length < 5) return false
    return codeValue.first() == CAT_PREFIX && codeValue.last() == CAT_SUFFIX
}


/**
 * 解析一个猫猫码字符串中的所有属性, 包括 `head`、`type` 和所有的属性键值对。
 *
 * @param codeValue 进行解析的catcode字符串
 * @param perceiveHead 得到被解析的head。将会是第一个被触发的perceive函数，应当至少被触发一次
 * @param perceiveType 得到被解析的type。将会是第二个被触发的perceive函数，应当至少被触发一次
 * @param perceiveProperty
 *
 * @throws IllegalArgumentException 当 [codeValue] 可能不符合标准结构时。
 * @throws IllegalArgumentException 当一个属性切割符 [CAT_PROPERTIES_SEPARATOR] 后面无法寻得有效键值对（缺少键值切割符 [CAT_PROPERTY_SEPARATOR] 时）
 */
@OptIn(ExperimentalContracts::class)
@JvmOverloads
@JsName("walkCatCode")
public inline fun walkCatCode(
    codeValue: String,
    decodeValue: Boolean = true,
    perceiveHead: (String) -> Unit,
    perceiveType: (String) -> Unit,
    crossinline perceiveProperty: (key: String, value: String) -> Unit,
) {
    contract {
        callsInPlace(perceiveHead, InvocationKind.AT_MOST_ONCE)
        callsInPlace(perceiveType, InvocationKind.AT_MOST_ONCE)
        callsInPlace(perceiveProperty, InvocationKind.UNKNOWN)
    }
    
    
    walkCatCodeInternal(
        codeValue,
        { s, e -> perceiveHead(requireCatHead(codeValue.substring(s, e))) },
        { s, e -> perceiveType(requireCatType(codeValue.substring(s, e))) },
        if (decodeValue) {
            { si, spi, ei ->
                val key = codeValue.substring(si, spi)
                val value = codeValue.substring(spi + 1, ei)
                perceiveProperty(key, CatEscalator.decodeParam(value))
            }
        } else {
            { si, spi, ei ->
                val key = codeValue.substring(si, spi)
                val value = codeValue.substring(spi + 1, ei)
                perceiveProperty(key, value)
            }
        }
    )
}

/**
 * @throws IllegalArgumentException
 */
@PublishedApi
internal inline fun walkCatCodeInternal(
    codeValue: String,
    perceiveHead: (startIndex: Int, endIndex: Int) -> Unit,
    perceiveType: (startIndex: Int, endIndex: Int) -> Unit,
    perceiveProperty: (startIndex: Int, separatorIndex: Int, endIndex: Int) -> Unit,
) {
    if (!checkCatCodeLoosely(codeValue)) throw IllegalArgumentException("codeValue '$codeValue' is not a catcode: Not wrapped by '$CAT_PREFIX' and '$CAT_SUFFIX' or length < 5")
    
    // find head
    val headEndIndex = codeValue.indexOf(CAT_HEAD_SEPARATOR)
    if (headEndIndex < 0) {
        throw IllegalArgumentException("codeValue '$codeValue' is not a catcode: head not found by head-type separator '$CAT_HEAD_SEPARATOR'")
    }
    
    perceiveHead(1, headEndIndex)
    
    // find type
    var typeEndIndex = codeValue.indexOf(CAT_PROPERTIES_SEPARATOR, headEndIndex)
    when {
        typeEndIndex < 0 -> {
            val lastIndex = codeValue.lastIndex
            // not found, maybe last
            if (lastIndex - 1 == headEndIndex) {
                throw IllegalArgumentException("codeValue '$codeValue' is not a catcode: type not found by cat properties separator '$CAT_PROPERTIES_SEPARATOR'")
            }
            typeEndIndex = lastIndex
        }
        
        typeEndIndex - 1 == headEndIndex -> throw IllegalArgumentException("codeValue '$codeValue' is not a catcode: type is empty before cat properties separator '$CAT_PROPERTIES_SEPARATOR'")
    }
    
    perceiveType(headEndIndex + 1, typeEndIndex)
    
    walkCatCodePropertiesInlineLooselyInternal(typeEndIndex, codeValue, perceiveProperty)
}

/**
 * 尝试依次遍历一个猫猫码字符串中的所有属性键值对。
 *
 * 为了提供更低的损耗，[walkCatCodePropertiesInlineLoosely] 的遍历与检测**十分宽松**。对于 [codeValue] 的合规检测来说，
 * **只会**检测 [codeValue] 是否被 [`[`][CAT_PREFIX] 和 [`]`][CAT_SUFFIX] 前后包裹而不关心 `HEAD` 和 `TYPE`。
 *
 * 这种宽松地检测方式可能会使得一些类似下述格式的代码也能够被正常使用：
 * - `[a=b,c=d]` (只会检测到 `c=d`)
 * - `[]`
 * - `[,a=b,c=d]`
 *
 * 如果想要保证严谨性，请在使用前先通过 [checkCatCode] 或 [requireCatCode] 进行检测。
 *
 * @throws IllegalArgumentException 当 [codeValue] 不是被 [`[`][CAT_PREFIX] 和 [`]`][CAT_SUFFIX] 前后包裹时。
 * @throws IllegalArgumentException 当一个属性切割符 [CAT_PROPERTIES_SEPARATOR] 后面无法寻得有效键值对（缺少键值切割符 [CAT_PROPERTY_SEPARATOR] 时）
 */
@OptIn(ExperimentalContracts::class)
@JvmOverloads
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
            { CatEscalator.decodeParam(it) },
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
public fun walkCatCodeProperties(codeValue: String, decodeValue: Boolean = true, walker: CatCodePropertiesWalker) {
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
    walker: CatCodePropertiesWalker,
) {
    walkCatCodePropertiesInlineLoosely(codeValue, decodeValue, walker::walk)
}

/**
 * 用于遍历CatCode的properties的函数接口，相当于 `(String, String) -> Unit`。
 */
public fun interface CatCodePropertiesWalker {
    public fun walk(key: String, value: String)
}



