@file:JvmName("Cats")
@file:JvmMultifileClass

package catcode2.cat

import catcode2.*
import kotlin.js.JsExport
import kotlin.js.JsName
import kotlin.jvm.JvmMultifileClass
import kotlin.jvm.JvmName
import kotlin.jvm.JvmOverloads
import kotlin.jvm.JvmStatic
import catcode2.cat.KeyHandle as BaseKeyHandle

private class LiteralCat(
    private val literal: String,
    override val head: String,
    private val headCoordinate: IntRange,
    override val type: String,
    private val properties: Map<String, String>,
    private val _keys: Array<String>,
    private val _values: Array<String>,
) : Cat {
    
    override fun toString(): String = literal
    
    override fun toCode(head: String?): String {
        if (head == null || head == this.head) {
            return literal
        }
        
        return buildString(literal.length - this.head.length + head.length) {
            append(literal, 0, headCoordinate.first)
            append(head)
            append(literal, headCoordinate.last + 1, literal.length)
        }
    }
    
    override fun get(key: String): String? = properties[key]
    
    override val keys: Set<String>
        get() = _keys.toSet()
    
    override val size: Int
        get() = properties.size
    
    override fun keyAt(index: Int): String {
        return _keys[index]
    }
    
    override fun keyAtOrNull(index: Int): String? {
        return if (index in _keys.indices) keyAt(index) else null
    }
    
    override fun valueAt(index: Int): String {
        return _values[index]
    }
    
    override fun valueAtOrNull(index: Int): String? {
        return if (index in _values.indices) valueAt(index) else null
    }
    
    override fun equals(other: Any?): Boolean {
        if (other !is Cat) return false
        if (other === this) return true
        
        if (other is LiteralCat && literal == other.literal) {
            return true
        }
        
        if (head != other.head) return false
        if (type != other.type) return false
        
        val otherKeys = other.keys
        if (properties.size != otherKeys.size) return false
        
        otherKeys.forEach { k ->
            if (properties[k] != other[k]) return false
        }
        
        return true
    }
    
    override fun hashCode(): Int = literal.hashCode()
}


/**
 * ?????????catcode?????????????????? [Cat]???
 *
 * @throws IllegalArgumentException ?????????????????????
 */
@JvmName("of")
@JsName("catOfLiteral")
@JsExport
public fun catOf(codeValue: String): Cat {
    lateinit var head: String
    lateinit var headCoordinate: IntRange
    lateinit var type: String
    
    val properties = LinkedHashMap<String, String>()
    
    properties.apply {
        walkCatCodeInternal(
            codeValue,
            { s, e ->
                headCoordinate = s until e
                head = codeValue.substring(s, e)
                null
            },
            { s, e ->
                type = codeValue.substring(s, e)
                null
            },
            { s, c, e ->
                val key = codeValue.substring(s, c)
                val value = codeValue.substring(c + 1, e)
                put(key, decodeCatParam(value))
                null
            }
        )
    }
    
    val keyArray = arrayOfNulls<String>(properties.size)
    val valueArray = arrayOfNulls<String>(properties.size)
    
    properties.includeToArrays(keyArray, valueArray)
    
    
    @Suppress("UNCHECKED_CAST")
    return LiteralCat(
        codeValue,
        head,
        headCoordinate,
        type,
        properties,
        keyArray as Array<String>,
        valueArray as Array<String>
    )
}

/**
 * ?????????????????????????????? [Cat]???
 */
@JvmName("of")
public fun catOf(type: String, head: String = CAT_HEAD, properties: Map<String, String>): Cat {
    return buildCat(type, head) {
        properties.forEach { (k, v) ->
            set(k, v)
        }
    }
}


/**
 * ?????? [CatCodeBuilder] ???????????? [Cat] ?????????
 *
 */
public inline fun buildCat(type: String, head: String = CAT_HEAD, block: CatCodeBuilder.() -> Unit): Cat {
    return CatCodeBuilder.of(type = type, head = head).apply(block).build()
}


/**
 * ?????? [CatCodeLiteralBuilder] ????????????catcode????????????
 */
@JvmOverloads
@JvmName("buildCatLiteral")
public inline fun buildCatLiteral(
    type: String,
    head: String = CAT_HEAD,
    appendable: Appendable = StringBuilder(),
    block: CatCodeLiteralBuilder.() -> Unit,
): String {
    return CatCodeLiteralBuilder.of(type, head, appendable).apply(block).build()
}

/**
 * [Cat] ???????????????
 */
public class CatCodeBuilder private constructor(private val head: String, private val type: String) :
    BaseCatCodeBuilder<Cat, CatCodeBuilder> {
    // [HEAD:TYPE]
    private var capacity: Int = 3 + head.length + type.length
    private val properties = mutableMapOf<String, String>()
    private lateinit var currentKey: String
    private lateinit var handle: KeyHandleImpl
    
    /**
     * ??????????????????????????????
     *
     * ```kotlin
     * buildCat(type = "foo") {
     *    set("key", "u&me")
     *    set("name", "forte", false)
     * }
     * ```
     */
    override fun set(key: String, value: String, encode: Boolean): CatCodeBuilder = apply {
        val propertyValue = if (encode) encodeCatParam(value) else value
        properties[key] = propertyValue
        capacity += (2 + key.length + propertyValue.length)
    }
    
    /**
     * ???????????? [key] ????????????key??? [KeyHandle]???
     *
     * ```kotlin
     * buildCat(type = "foo") {
     *   key("key") value "u&me"
     *   key("name").value("forte", false)
     * }
     * ```
     *
     * @see KeyHandle
     */
    override fun key(key: String): KeyHandle {
        currentKey = key
        if (!::handle.isInitialized) {
            handle = KeyHandleImpl()
        }
        return handle
    }
    
    
    /**
     * ?????? [CatCodeBuilder.key] ?????????, ?????????key??????????????????
     *
     * [CatCodeBuilder.key] ??? [KeyHandle] ???????????????????????????????????????????????????????????????
     * ????????? [key] ??? [KeyHandle] ??????????????????????????????????????????????????????
     *
     * ```kotlin
     * key("key").value("value")
     * ```
     * [KeyHandle] ?????????????????????????????????????????????
     *
     *
     */
    public interface KeyHandle : BaseKeyHandle<Cat, CatCodeBuilder> {
        /**
         * ?????????key????????????value????????? [value] ???null????????????????????????
         *
         * ???????????? [encode] ??????????????? [value] ????????????????????????true???
         * ???????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
         *
         * @param encode ????????? [value] ????????????????????????true
         */
        override fun value(value: String?, encode: Boolean): CatCodeBuilder
    }
    
    
    private inner class KeyHandleImpl : KeyHandle {
        override fun value(value: String?, encode: Boolean): CatCodeBuilder {
            val key = currentKey
            if (value != null) {
                set(key, value, encode)
            }
            return this@CatCodeBuilder
        }
    }
    
    override fun build(): Cat = catOfBuilder(capacity, head, type, properties)
    
    public companion object {
        /**
         * ???????????? [CatCodeBuilder]???
         */
        @JvmStatic
        @JvmOverloads
        @JsName("of")
        public fun of(type: String, head: String = CAT_HEAD): CatCodeBuilder {
            return CatCodeBuilder(head, type)
        }
    }
}


/**
 * ???????????? catcode.
 */
private fun catOfBuilder(
    capacity: Int, head: String, type: String, properties: Map<String, String>,
): LiteralCat {
    require(requireCatHead(head).isNotEmpty()) { "'head' cannot be empty" }
    require(requireCatType(type).isNotEmpty()) { "'type' cannot be empty" }
    
    
    val headCoordinate = 1..head.length
    val catcode = buildString(capacity) {
        append(CAT_PREFIX)
        append(head)
        append(CAT_HEAD_SEPARATOR)
        append(type)
        properties.forEach { (k, v) ->
            append(CAT_PROPERTIES_SEPARATOR)
            append(k).append(CAT_PROPERTY_SEPARATOR).append(v)
        }
        append(CAT_SUFFIX)
    }
    
    val keyArray = arrayOfNulls<String>(properties.size)
    val valueArray = arrayOfNulls<String>(properties.size)
    
    
    properties.includeToArrays(keyArray, valueArray)
    
    @Suppress("UNCHECKED_CAST")
    return LiteralCat(
        catcode,
        head,
        headCoordinate,
        type,
        properties.toMap(),
        keyArray as Array<String>,
        valueArray as Array<String>
    )
}

private fun <K, V> Map<K, V>.includeToArrays(keyArray: Array<K?>, valueArray: Array<V?>) {
    var index = 0
    forEach { (k, v) ->
        keyArray[index] = k
        valueArray[index] = v
        index++
    }
}