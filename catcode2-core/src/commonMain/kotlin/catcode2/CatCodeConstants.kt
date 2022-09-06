@file:JvmName("CatCodeConstants")
@file:JsExport

package catcode2

import kotlin.js.JsExport
import kotlin.js.JsName
import kotlin.jvm.JvmName


/**
 * 默认情况下猫猫码的 `head`。即 `[CAT:type,p=v]` 中的 `CAT` 所应代表的具体的值。
 */
public const val CAT_HEAD: String = "CAT"

/**
 * 标准猫猫码的起始符 `[`。
 */
@Suppress("NON_EXPORTABLE_TYPE")
@JsName("CAT_PREFIX_CHAR")
public const val CAT_PREFIX: Char = '['

/**
 * 标准猫猫码的终止符 `]`。
 */
@Suppress("NON_EXPORTABLE_TYPE")
@JsName("CAT_SUFFIX_CHAR")
public const val CAT_SUFFIX: Char = ']'

/**
 * 用于分割`head` 和 `type` 之间的切割符。
 */
@Suppress("NON_EXPORTABLE_TYPE")
@JsName("CAT_HEAD_SEPARATOR_CHAR")
public const val CAT_HEAD_SEPARATOR: Char = ':'

/**
 * 用于分割不同键值对之间的切割符。
 */
@Suppress("NON_EXPORTABLE_TYPE")
@JsName("CAT_PROPERTIES_SEPARATOR_CHAR")
public const val CAT_PROPERTIES_SEPARATOR: Char = ','

/**
 * 用于分割键与值之间的切割符。
 */
@Suppress("NON_EXPORTABLE_TYPE")
@JsName("CAT_PROPERTY_SEPARATOR_CHAR")
public const val CAT_PROPERTY_SEPARATOR: Char = '='

/**
 * [CAT_PREFIX] 的字符串常量值。
 */
@JsName("CAT_PREFIX")
public const val CAT_PREFIX_VALUE: String = "["

/**
 * [CAT_SUFFIX] 的字符串常量值。
 */
@JsName("CAT_SUFFIX")
public const val CAT_SUFFIX_VALUE: String = "]"

/**
 * [CAT_HEAD_SEPARATOR] 的字符串常量值。
 */
@JsName("CAT_HEAD_SEPARATOR")
public const val CAT_HEAD_SEPARATOR_VALUE: String = ":"

/**
 * [CAT_PROPERTIES_SEPARATOR] 的字符串常量值。
 */
@JsName("CAT_PROPERTIES_SEPARATOR")
public const val CAT_PROPERTIES_SEPARATOR_VALUE: String = ","

/**
 * [CAT_PROPERTY_SEPARATOR] 的字符串常量值。
 */
@JsName("CAT_PROPERTY_SEPARATOR")
public const val CAT_PROPERTY_SEPARATOR_VALUE: String = "="