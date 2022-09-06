@file:JsExport

package catcode2


// region 4js

/**
 * 根据指定字符，得到它应当被转义为的结果。如果无需转义则得到null。
 *
 * 同 [getCatTextEncode], 用于通过 [JsExport] 导出。
 * [value] 必须保证有且只能有1个字符。
 *
 */
@JsName("getTextEncode")
public fun getCatTextEncodeString(value: String): String? {
    require(value.length == 1) { "value.length must == 1, but ${value.length}" }
    return getCatTextEncode(value.first())
}

/**
 * 根据指定字符串，得到它转义前的结果。如果无需转义则得到null。
 *
 * 同 [getCatTextDecode], 用于通过 [JsExport] 导出。
 * 因此返回值在不为null的情况下只可能有一个字符。
 */
@JsName("getTextDecode")
public fun getCatTextDecodeString(value: String): String? = getCatTextDecode(value)?.toString()

/**
 * 根据指定字符，得到它应当被转义为的结果。如果无需转义则得到null。
 *
 * 同 [getCatParamEncode], 用于通过 [JsExport] 导出。
 * [value] 必须保证有且只能有1个字符。
 */
@JsName("getParamEncode")
public fun getCatParamEncodeString(value: String): String? {
    require(value.length == 1) { "value.length must == 1, but ${value.length}" }
    return getCatParamEncode(value.first())
}

/**
 * 根据指定字符串，得到它转义前的结果。如果无需转义则得到null。
 *
 * 同 [getCatParamDecode], 用于通过 [JsExport] 导出。
 * 因此返回值在不为null的情况下只可能有一个字符。
 */
@JsName("getParamDecode")
public fun getCatParamDecodeString(value: String): String? = getCatParamDecode(value)?.toString()

// endregion


