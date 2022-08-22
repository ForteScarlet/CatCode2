@file:JvmName("CatCodes")

package catcode2

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import kotlin.js.JsName
import kotlin.jvm.JvmName
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
private val CHECK_REGEX = Regex("\\[[a-zA-Z_.$]+:[a-zA-Z_.$]+(,.+=.*)*\\]")

/**
 * 校验 [codeValue] 是否大概率为一个猫猫码字符串。
 */
@JvmName("check")
public fun checkCatCode(codeValue: String): Boolean = CHECK_REGEX.matches(codeValue)


/**
 * 校验 [codeValue] 是否大概率为一个猫猫码字符串，如果无法通过 [checkCatCode] 的检测则会抛出 [IllegalArgumentException]。
 */
@JvmName("require")
public inline fun requireCatCode(
    codeValue: String,
    lazyMessage: (code: String) -> String = { "code value '$codeValue' not a catcode" },
): String {
    require(checkCatCode(codeValue)) { lazyMessage(codeValue) }
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
public inline fun walkCatCode(
    codeValue: String,
    perceiveHead: (String) -> Unit,
    perceiveType: (String) -> Unit,
    perceiveProperty: (key: String, value: String) -> Unit,
) {
    contract {
        callsInPlace(perceiveHead, InvocationKind.AT_MOST_ONCE)
        callsInPlace(perceiveType, InvocationKind.AT_MOST_ONCE)
        callsInPlace(perceiveProperty, InvocationKind.UNKNOWN)
    }
    
    if (!checkCatCodeLoosely(codeValue)) throw IllegalArgumentException("codeValue '$codeValue' is not a catcode: Not wrapped by '$CAT_PREFIX' and '$CAT_SUFFIX' or length < 5")
    
    // find head
    val headEndIndex = codeValue.indexOf(CAT_HEAD_SEPARATOR)
    if (headEndIndex < 0) {
        throw IllegalArgumentException("codeValue '$codeValue' is not a catcode: head not found by head-type separator '$CAT_HEAD_SEPARATOR'")
    }
    
    perceiveHead(codeValue.substring(1, headEndIndex))
    
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
    
    perceiveType(codeValue.substring(headEndIndex + 1, typeEndIndex))
    
    walkCatCodePropertiesInlineLoosely0(typeEndIndex, codeValue, perceiveProperty)
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
public inline fun walkCatCodePropertiesInlineLoosely(codeValue: String, walk: (key: String, value: String) -> Unit) {
    contract {
        callsInPlace(walk, InvocationKind.UNKNOWN)
    }
    
    if (!checkCatCodeLoosely(codeValue)) throw IllegalArgumentException("codeValue '$codeValue' is not a catcode: Not wrapped by '$CAT_PREFIX' and '$CAT_SUFFIX' or length < 5")
    
    walkCatCodePropertiesInlineLoosely0(codeValue.indexOf(CAT_PROPERTIES_SEPARATOR), codeValue, walk)
}


@PublishedApi
@JvmSynthetic
@OptIn(ExperimentalContracts::class)
internal inline fun walkCatCodePropertiesInlineLoosely0(
    startIndex0: Int,
    codeValue: String,
    walk: (key: String, value: String) -> Unit,
) {
    contract {
        callsInPlace(walk, InvocationKind.UNKNOWN)
    }
    
    val lastIndex = codeValue.lastIndex
    
    var startIndex = startIndex0 // codeValue.indexOf(CAT_PROPERTIES_SEPARATOR)
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
        
        val key = codeValue.substring(startIndex + 1, propertyIndex)
        val value = codeValue.substring(propertyIndex + 1, endIndex)
        
        walk(key, value)
        
        startIndex = endIndex
    }
}

@JvmName("walkProperties")
@JsName("walkCatCodeProperties")
public fun walkCatCodeProperties(codeValue: String, walker: CatCodePropertiesWalker) {
    walkCatCodePropertiesInlineLoosely(codeValue, walker::walk)
}


public fun interface CatCodePropertiesWalker {
    public fun walk(key: String, value: String)
}



