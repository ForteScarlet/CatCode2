package catcode2


/**
 * 猫猫码特殊字符转义器。
 *
 * 当一个猫猫码于普通文本混合时，
 * 对于普通文本中所出现的猫猫码需要使用的特殊符号，便需要进行转义，否则可能会导致最终的解析结果不正确。
 *
 * 例如
 * ```
 * 猫猫码 [CAT:foo] 的开始符为 &#91;。
 * ```
 *
 * 则可以解析为三段：
 * - 普通文本: `"猫猫码 "`
 * - 猫猫码文本: `"[CAT:foo]"`
 * - 普通文本: `" 的开始符为 [。"`
 *
 *
 * |  源  |  转义  |
 * |-----|-------|
 * |  `&`   |   `&amp;`   |
 * |  `[`   |   `&#91;`   |
 * |  `]`   |   `&#93;`   |
 * |  `'\t'`   |   `&#09;`   |
 * |  `'\r'`   |   `&#10;`   |
 * |  `'\n'`   |   `&#13;`   |
 * |  `=`   |   `&#61;`   |
 * |  `,`   |   `&#44;`   |
 *
 * 其中，对于 `=` 和 `,` 的转义规则仅存在于猫猫码内的属性值中。
 *
 */
public object CatEscalator {
    private const val DECODE_PREFIX: Char = '&'
    private const val AND_VALUE: Char = '&'
    private const val DECODE_SUFFIX: Char = ';'
    
    private val TEXT_ENCODE_MAP = mapOf(
        '&' to "&amp;",
        '[' to "&#91;",
        ']' to "&#93;",
        '\t' to "&#09;",
        '\r' to "&#10;",
        '\n' to "&#13;",
    )
    
    private val PARAM_ENCODE_MAP = mapOf(
        '&' to "&amp;",
        '[' to "&#91;",
        ']' to "&#93;",
        '=' to "&#61;",
        ',' to "&#44;",
        '\t' to "&#09;",
        '\r' to "&#10;",
        '\n' to "&#13;",
    )
    
    private val TEXT_DECODE_MAP = mapOf(
        "&amp;" to '&',
        "&#91;" to '[',
        "&#93;" to ']',
        "&#09;" to '\t',
        "&#10;" to '\r',
        "&#13;" to '\n',
    )
    
    private val PARAM_DECODE_MAP = mapOf(
        "&amp;" to '&',
        "&#91;" to '[',
        "&#93;" to ']',
        "&#61;" to '=',
        "&#44;" to ',',
        "&#09;" to '\t',
        "&#10;" to '\r',
        "&#13;" to '\n',
    )
    
    private val TEXT_DECODE_CODE_VALUE_MAP: Map<Long, Char> = mapOf(
        "91".toKeyValue() to '[',
        "93".toKeyValue() to ']',
        "09".toKeyValue() to '\t',
        "10".toKeyValue() to '\r',
        "13".toKeyValue() to '\n',
    )
    
    private val PARAM_DECODE_CODE_VALUE_MAP = mapOf(
        "91".toKeyValue() to '[',
        "93".toKeyValue() to ']',
        "61".toKeyValue() to '=',
        "44".toKeyValue() to ',',
        "09".toKeyValue() to '\t',
        "10".toKeyValue() to '\r',
        "13".toKeyValue() to '\n',
    )
    
    private fun String.toKeyValue(): Long {
        return toKeyValue(this[0], this[1])
    }
    
    private fun toKeyValue(v1: Char, v2: Char): Long {
        return (v1.code.toLong() shl 32) or v2.code.toLong()
    }
    
    // 0009 0003
    
    /**
     * 根据指定字符，得到它应当被转义为的结果。如果无需转义则得到null。
     */
    public fun getTextEncode(value: Char): String? = TEXT_ENCODE_MAP[value]
    
    /**
     * 根据指定字符串，得到它转义前的结果。如果无需转义则得到null。
     */
    public fun getTextDecode(value: String): Char? = TEXT_DECODE_MAP[value]
    
    /**
     * 根据指定字符，得到它应当被转义为的结果。如果无需转义则得到null。
     */
    public fun getParamEncode(value: Char): String? = PARAM_ENCODE_MAP[value]
    
    /**
     * 根据指定字符串，得到它转义前的结果。如果无需转义则得到null。
     */
    public fun getParamDecode(value: String): Char? = PARAM_DECODE_MAP[value]
    
    private fun getTextDecodeByCodeValue(code: Long): Char {
        return TEXT_DECODE_CODE_VALUE_MAP[code] ?: Char.MIN_VALUE
    }
    
    private fun getParamDecodeByCodeValue(code: Long): Char {
        return PARAM_DECODE_CODE_VALUE_MAP[code] ?: Char.MIN_VALUE
    }
    
    
    /**
     * 依次遍历 [text] 进行转义后的字符。
     * 当遇到特殊字符转移后，会依次walk转义结果，例如当 `&` 被转义为 `&amp;`，
     * 则会依次遍历这5个字符。
     *
     */
    private inline fun walkEncoded(text: String, encodeGetter: (Char) -> String?, walk: (Char) -> Unit) {
        for (c in text) {
            val encode = encodeGetter(c)
            if (encode == null) {
                walk(c)
            } else {
                encode.forEach(walk)
            }
        }
    }
    
    /**
     * 将 [text] 根据转义标准进行转义，例如将 `&` 被转义为 `&amp;`。
     */
    public fun encodeText(text: String): String = buildString(text.length) {
        walkEncoded(text, ::getTextEncode, ::append)
    }
    
    /**
     * 将 [text] 根据转义标准进行转义，例如将 `&` 被转义为 `&amp;`。
     */
    public fun encodeParam(text: String): String = buildString(text.length) {
        walkEncoded(text, ::getParamEncode, ::append)
    }
    
    /**
     * 将 [text] 转为转义前的内容, 例如将 `&amp;` 转为 `&`。
     */
    private inline fun decode(text: String, decodeGetter: (Long) -> Char): String = buildString(text.length) {
        val lastIndex = text.lastIndex
        var next = 0
        while (next <= lastIndex) {
            // &?
            val preIndex = text.indexOf(DECODE_PREFIX, next)
            if (preIndex < 0) {
                // no more.
                append(text, next, text.length)
                break
            }
            
            append(text, next, preIndex)
            
            // ;
            val sufIndex = preIndex + 4
            // the last
            if (sufIndex > lastIndex) {
                append(text, preIndex, text.length)
                break
            }
    
            next = sufIndex + 1
    
            if (text[sufIndex] == DECODE_SUFFIX) {
                val c1 = text[preIndex + 1]
                val c2 = text[preIndex + 2]
                val c3 = text[preIndex + 3]
                
                // other number
                if (c1 == '#') {
                    // &#ab;
                    // find number
                    val target = decodeGetter(toKeyValue(c2, c3))
                    if (target != Char.MIN_VALUE) {
                        // decoded, append it.
                        append(target)
                        continue
                    }
                }
                
                // &amp;
                if (c1 == 'a' && c2 == 'm' && c3 == 'p') {
                    // append '&'
                    append(AND_VALUE)
                    continue
                }
            }
            
            // just append.
            append(text, preIndex, sufIndex)
        }
    }
    
    /**
     * 将 [text] 转为转义前的内容, 例如将 `&amp;` 转为 `&`。
     */
    public fun decodeText(text: String): String = decode(text, ::getTextDecodeByCodeValue)
    
    /**
     * 将 [text] 转为转义前的内容, 例如将 `&amp;` 转为 `&`。
     */
    public fun decodeParam(text: String): String = decode(text, ::getParamDecodeByCodeValue)
    
}