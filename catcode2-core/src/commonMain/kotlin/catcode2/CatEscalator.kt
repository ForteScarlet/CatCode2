package catcode2

import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport
import kotlin.js.JsName
import kotlin.jvm.JvmStatic


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
    
    private val TEXT_DECODE_CODE_VALUE_MAP = LongCharMap(
        "91".toKeyValue() to '[',
        "93".toKeyValue() to ']',
        "09".toKeyValue() to '\t',
        "10".toKeyValue() to '\r',
        "13".toKeyValue() to '\n',
    )
    
    private val PARAM_DECODE_CODE_VALUE_MAP = LongCharMap(
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
    @JvmStatic @JsName("getTextEncode")
    public fun getTextEncode(value: Char): String? = TEXT_ENCODE_MAP[value]
    
    /**
     * 根据指定字符串，得到它转义前的结果。如果无需转义则得到null。
     */
    @JvmStatic @JsName("getTextDecode")
    public fun getTextDecode(value: String): Char? = TEXT_DECODE_MAP[value]
    
    /**
     * 根据指定字符，得到它应当被转义为的结果。如果无需转义则得到null。
     */
    @JvmStatic @JsName("getParamEncode")
    public fun getParamEncode(value: Char): String? = PARAM_ENCODE_MAP[value]
    
    /**
     * 根据指定字符串，得到它转义前的结果。如果无需转义则得到null。
     */
    @JvmStatic @JsName("getParamDecode")
    public fun getParamDecode(value: String): Char? = PARAM_DECODE_MAP[value]
    
    private fun getTextDecodeByCodeValue(code: Long): Char = TEXT_DECODE_CODE_VALUE_MAP[code]
    private fun getParamDecodeByCodeValue(code: Long): Char = PARAM_DECODE_CODE_VALUE_MAP[code]
    
    
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
    @JvmStatic
    public fun encodeText(text: String): String = buildString(text.length) {
        walkEncoded(text, ::getTextEncode, ::append)
    }
    
    /**
     * 将 [text] 根据转义标准进行转义，例如将 `&` 被转义为 `&amp;`。
     */
    @JvmStatic
    public fun encodeParam(text: String): String = buildString(text.length) {
        walkEncoded(text, ::getParamEncode, ::append)
    }
    
    private inline fun String.walk(startIndex: Int, endIndex: Int, walk: (Char) -> Unit) {
        for (i in startIndex until endIndex) {
            walk(this[i])
        }
    }
    
    /**
     * 依次遍历 [text] 转为转义前的内容, 例如将 `&amp;` 转为 `&`。
     */
    private inline fun walkDecoded(text: String, decodeGetter: (Long) -> Char, walk: (Char) -> Unit) {
        val lastIndex = text.lastIndex
        var next = 0
        while (next <= lastIndex) {
            // &?
            val preIndex = text.indexOf(DECODE_PREFIX, next)
            if (preIndex < 0) {
                // no more.
                text.walk(next, text.length, walk)
                break
            }
            
            text.walk(next, preIndex, walk)
            
            // ;
            val sufIndex = preIndex + 4
            // the last
            if (sufIndex > lastIndex) {
                text.walk(preIndex, text.length, walk)
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
                        walk(target)
                        continue
                    }
                }
                
                // &amp;
                if (c1 == 'a' && c2 == 'm' && c3 == 'p') {
                    // append '&'
                    walk(AND_VALUE)
                    continue
                }
            }
            
            // just append.
            text.walk(preIndex, sufIndex, walk)
        }
    }
    
    /**
     * 将 [text] 转为转义前的内容, 例如将 `&amp;` 转为 `&`。
     */
    @JvmStatic
    public fun decodeText(text: String): String =
        buildString(text.length) { walkDecoded(text, ::getTextDecodeByCodeValue, ::append) }
    
    /**
     * 将 [text] 转为转义前的内容, 例如将 `&amp;` 转为 `&`。
     */
    @JvmStatic
    public fun decodeParam(text: String): String =
        buildString(text.length) { walkDecoded(text, ::getParamDecodeByCodeValue, ::append) }
    
}


/**
 * 固定容量不可扩容的基础类型 (key: [Long], value: [Char]) 哈希表。
 * [LongCharMap] 用于降低内存占用，键值对都使用基本数据类型来避免拆/装箱（例如JVM平台上）
 *
 * 对于键与值的定位与存放使用开放寻址的方式。
 *
 * 此表固定大小，仅可用于 [获取][get]，键值对从一开始便被确定。
 *
 * 其中, 存放的 value char 的 [Char.code] 不可为 [Char.MIN_VALUE] —— `Char(0)` 将会用于标记为'未应用'，作用同 `null`。
 *
 */
private class LongCharMap(
    vararg pairs: Pair<Long, Char>,
    /**
     * 代表用于标记'未应用'状态的值。
     */
    private val unusedValue: Char = Char.MIN_VALUE,
) {
    /*
        实际上只是想写着玩，这种场景下并不会节省多少内存，大概。
        效率？也许会高，也许不会。最多不超过10个键值对，又如何呢
     */
    
    private val keys = LongArray(pairs.size)
    private val values = CharArray(pairs.size)
    
    private inline val Char.isUnused: Boolean get() = this == unusedValue
    
    init {
        fun put(key: Long, value: Char) {
            val startIndex = hashIndex(key)
            var index = startIndex
            while (true) {
                if (values[index].isUnused) {
                    // maybe nothing here, set value.
                    keys[index] = key
                    values[index] = value
                    break
                }
                
                if (keys[index] == key) {
                    throw IllegalArgumentException("Duplicate key $key")
                }
                
                index = probeNext(index)
                if (index == startIndex) {
                    throw IllegalStateException("Unable to insert")
                }
            }
        }
        
        pairs.forEach { (k, v) ->
            put(k, v)
        }
    }
    
    private fun hashIndex(key: Long): Int {
        return ((key % keys.size + keys.size) % keys.size).toInt()
    }
    
    private fun probeNext(index: Int): Int {
        return if (index == keys.lastIndex) 0 else index + 1
    }
    
    operator fun get(key: Long): Char {
        val startIndex = hashIndex(key)
        var index = startIndex
        while (true) {
            val value = values[index]
            if (value.isUnused) {
                return unusedValue
            }
            
            val gotKey = keys[index]
            if (gotKey == key) {
                return values[index]
            }
            
            index = probeNext(index)
            if (index == startIndex) {
                return unusedValue
            }
        }
    }
}