@file:JvmName("CatChecker")
@file:JsExport

package catcode2

import kotlin.js.JsExport
import kotlin.jvm.JvmName

@Suppress("RegExpRedundantEscape") // '\\]' for nodejs.
private val CATCODE_CHECK_REGEX = Regex("\\[[a-zA-Z_.$-]+:[a-zA-Z_.$-]+(,.+=.*)*\\]")

private val HEAD_CHECK_REGEX = Regex("[a-zA-Z_.$-]+")

private val TYPE_CHECK_REGEX = HEAD_CHECK_REGEX

/**
 * 校验 [head] 是否为一个标准的猫猫码head。
 * 即需要 [head] 匹配正则 `[a-zA-Z_.$-]+`。
 */
@JvmName("checkHead")
public fun checkCatHead(head: String): Boolean = HEAD_CHECK_REGEX.matches(head)

/**
 * 校验 [head] 是否为一个标准的猫猫码head。如果不匹配则会抛出 [IllegalArgumentException] 异常。
 *
 * @see checkCatHead
 */
@JvmName("requireHead")
public inline fun requireCatHead(
    head: String,
    lazyMessage: () -> String = { "head '$head' not match '[a-zA-Z_.\$]+'" },
): String {
    require(checkCatHead(head), lazyMessage)
    return head
}

/**
 * 校验 [type] 是否为一个标准的猫猫码类型。即需要 [type] 匹配正则 `[a-zA-Z_.$-]+`。
 */
@JvmName("checkType")
public fun checkCatType(type: String): Boolean = TYPE_CHECK_REGEX.matches(type)

/**
 * 校验 [type] 是否为一个标准的猫猫码类型。如果不匹配则会抛出 [IllegalArgumentException] 异常。
 */
@JvmName("requireType")
public inline fun requireCatType(
    type: String,
    lazyMessage: () -> String = { "type '$type' not match '[a-zA-Z_.\$]+'" },
): String {
    require(checkCatType(type), lazyMessage)
    return type
}

/**
 * 校验 [codeValue] 是否大概率为一个猫猫码字符串。即需要 [codeValue] 满足正则 `\\[[a-zA-Z_.$-]+:[a-zA-Z_.$-]+(,.+=.*)*\\]`。
 */
@JvmName("check")
public fun checkCatCode(codeValue: String): Boolean = CATCODE_CHECK_REGEX.matches(codeValue)


/**
 * 校验 [codeValue] 是否大概率为一个猫猫码字符串，如果无法通过 [checkCatCode] 的检测则会抛出 [IllegalArgumentException]。
 */
@JvmName("require")
public inline fun requireCatCode(
    codeValue: String,
    lazyMessage: () -> String = { "code value '$codeValue' not a catcode" },
): String {
    require(checkCatCode(codeValue), lazyMessage)
    return codeValue
}

/**
 * 更宽松地校验 [codeValue] 是否大概率为一个猫猫码字符串。
 * [checkCatCodeLeniently] 只会检测 [codeValue] 的长度（应该 >=5），以及是否被 [CAT_PREFIX] 和 [CAT_SUFFIX] 包裹。
 *
 * [checkCatCodeLeniently] 相比较于 [checkCatCode] 更加迅速，但同时牺牲了准确性，
 * 因此 [checkCatCodeLeniently] 不能保证 [codeValue] 是一个cat code。
 *
 * 例如字符串 `[ bc]` 可以通过 [checkCatCodeLeniently] 的检测，但是很明显它并不是一个cat code。
 *
 */
public fun checkCatCodeLeniently(codeValue: String): Boolean {
    // [a:b]
    if (codeValue.length < 5) return false
    return codeValue.first() == CAT_PREFIX && codeValue.last() == CAT_SUFFIX
}

/**
 * 更宽松地校验 [codeValue] 是否大概率为一个猫猫码字符串。
 *
 * [checkCatCodeLeniently] 只会检测 [codeValue] 的长度（应该 >=5），以及是否被 [CAT_PREFIX] 和 [CAT_SUFFIX] 包裹。
 *
 */
public inline fun requireCatCodeLeniently(
    codeValue: String,
    lazyMessage: () -> String = { "code value '$codeValue' not a catcode" },
): String {
    require(checkCatCodeLeniently(codeValue), lazyMessage)
    return codeValue
}
