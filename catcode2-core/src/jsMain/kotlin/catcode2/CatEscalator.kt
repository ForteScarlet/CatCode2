package catcode2

import catcode2.annotation.Api4Js

@JsExport
public actual object CatEscalator {
    /**
     * 根据指定字符，得到它应当被转义为的结果。如果无需转义则得到null。
     */
    @Suppress("NON_EXPORTABLE_TYPE")
    @JsName("_getTextEncode")
    public actual fun getTextEncode(value: Char): String? = CatEscalatorInternalBaseImpl.getTextEncode(value)
    
    /**
     * 根据指定字符串，得到它转义前的结果。如果无需转义则得到null。
     */
    @Suppress("NON_EXPORTABLE_TYPE")
    @JsName("_getTextDecode")
    public actual fun getTextDecode(value: String): Char? = CatEscalatorInternalBaseImpl.getTextDecode(value)
    
    /**
     * 根据指定字符，得到它应当被转义为的结果。如果无需转义则得到null。
     */
    @Suppress("NON_EXPORTABLE_TYPE")
    @JsName("_getParamEncode")
    public actual fun getParamEncode(value: Char): String? = CatEscalatorInternalBaseImpl.getParamEncode(value)
    
    /**
     * 根据指定字符串，得到它转义前的结果。如果无需转义则得到null。
     */
    @Suppress("NON_EXPORTABLE_TYPE")
    @JsName("_getParamDecode")
    public actual fun getParamDecode(value: String): Char? = CatEscalatorInternalBaseImpl.getParamDecode(value)
    
    // region 4js
    
    /**
     * 根据指定字符，得到它应当被转义为的结果。如果无需转义则得到null。
     *
     * 同 [getTextEncode], 用于通过 [JsExport] 导出。
     * [value] 必须保证有且只能有1个字符。
     *
     */
    @Api4Js
    @JsName("getTextEncode")
    public fun getTextEncode4Js(value: String): String? {
        require(value.length == 1) { "value.length must == 1, but ${value.length}" }
        return getTextEncode(value.first())
    }
    
    /**
     * 根据指定字符串，得到它转义前的结果。如果无需转义则得到null。
     *
     * 同 [getTextDecode], 用于通过 [JsExport] 导出。
     * 因此返回值在不为null的情况下只可能有一个字符。
     */
    @Api4Js
    @JsName("getTextDecode")
    public fun getTextDecode4Js(value: String): String? = getTextDecode(value)?.toString()
    
    /**
     * 根据指定字符，得到它应当被转义为的结果。如果无需转义则得到null。
     *
     * 同 [getParamEncode], 用于通过 [JsExport] 导出。
     * [value] 必须保证有且只能有1个字符。
     */
    @Api4Js
    @JsName("getParamEncode")
    public fun getParamEncode4Js(value: String): String? {
        require(value.length == 1) { "value.length must == 1, but ${value.length}" }
        return getParamEncode(value.first())
    }
    
    /**
     * 根据指定字符串，得到它转义前的结果。如果无需转义则得到null。
     *
     * 同 [getParamDecode], 用于通过 [JsExport] 导出。
     * 因此返回值在不为null的情况下只可能有一个字符。
     */
    @Api4Js
    @JsName("getParamDecode")
    public fun getParamDecode4Js(value: String): String? = getParamDecode(value)?.toString()
    
    // endregion
    
    /**
     * 将 [text] 根据转义标准进行转义，例如将 `&` 被转义为 `&amp;`。
     */
    public actual fun encodeText(text: String): String = CatEscalatorInternalBaseImpl.encodeText(text)
    
    /**
     * 将 [text] 根据转义标准进行转义，例如将 `&` 被转义为 `&amp;`。
     */
    public actual fun encodeParam(text: String): String = CatEscalatorInternalBaseImpl.encodeParam(text)
    
    /**
     * 将 [text] 转为转义前的内容, 例如将 `&amp;` 转为 `&`。
     */
    public actual fun decodeText(text: String): String = CatEscalatorInternalBaseImpl.decodeText(text)
    
    /**
     * 将 [text] 转为转义前的内容, 例如将 `&amp;` 转为 `&`。
     */
    public actual fun decodeParam(text: String): String = CatEscalatorInternalBaseImpl.decodeParam(text)
    
}

