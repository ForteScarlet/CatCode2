@file:JvmName("Cats")
@file:JvmMultifileClass

package catcode2.cat

import catcode2.*
import kotlin.js.JsName
import kotlin.jvm.JvmMultifileClass
import kotlin.jvm.JvmName
import kotlin.jvm.JvmOverloads
import kotlin.jvm.JvmStatic

private class LiteralCat(
    private val literal: String,
    override val head: String,
    private val headCoordinate: IntRange,
    override val type: String,
    private val properties: Map<String, String>,
) : Cat {
    override fun toString(head: String): String {
        return buildString(literal.length - this.head.length + head.length) {
            append(literal, 0, headCoordinate.first)
            append(head)
            append(literal, headCoordinate.last + 1, literal.length)
        }
    }
    
    override fun toString(): String = literal
    
    override fun get(key: String): String? = properties[key]
    
    override val keys: Set<String>
        get() = properties.keys
    
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
 * 将一个catcode字符串转化为 [Cat]。
 *
 * @throws IllegalArgumentException 当格式不匹配时
 */
@JvmName("of")
@JsName("catOfLiteral")
public fun catOf(codeValue: String): Cat {
    lateinit var head: String
    lateinit var headCoordinate: IntRange
    lateinit var type: String
    val properties = buildMap {
        walkCatCodeInternal(
            codeValue,
            { s, e ->
                headCoordinate = s until e
                head = codeValue.substring(s, e)
            },
            { s, e ->
                type = codeValue.substring(s, e)
            },
            { s, c, e ->
                val key = codeValue.substring(s, c)
                val value = codeValue.substring(c + 1, e)
                put(key, CatEscalator.decodeParam(value))
            }
        )
    }
    
    return LiteralCat(codeValue, head, headCoordinate, type, properties)
}

/**
 * 提供属性集并构建一个 [Cat]。
 */
@JvmName("of")
@JsName("catOfProperties")
public fun catOf(type: String, head: String = CAT_HEAD, properties: Map<String, String>): Cat {
    return buildCat(type, head) {
        properties.forEach { (k, v) ->
            set(k, v)
        }
    }
}


/**
 * 通过 [CatCodeBuilder] 构建一个 [Cat] 实例。
 *
 */
public inline fun buildCat(type: String, head: String = CAT_HEAD, block: CatCodeBuilder.() -> Unit): Cat {
    return CatCodeBuilder.of(type = type, head = head).apply(block).build()
}


/**
 * [Cat] 的构建器。
 */
public class CatCodeBuilder private constructor(private val head: String, private val type: String) :
    BaseCatCodeBuilder<Cat, CatCodeBuilder> {
    private val properties = mutableMapOf<String, String>()
    private lateinit var currentKey: String
    private lateinit var handle: KeyHandleImpl
    
    /**
     * 直接设置一个键值对。
     *
     * ```kotlin
     * buildCat(type = "foo") {
     *    set("key", "u&me")
     *    set("name", "forte", false)
     * }
     * ```
     */
    override fun set(key: String, value: String, encode: Boolean): CatCodeBuilder = apply {
        properties[key] = if (encode) CatEscalator.encodeParam(value) else value
    }
    
    /**
     * 提供一个 [key] 并得到此key的 [KeyHandle]。
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
     * 通过 [CatCodeBuilder.key] 得到对, 指定的key指设置结果。
     *
     * [CatCodeBuilder.key] 与 [KeyHandle] 不可交叉使用，否则可能会导致预期外的结果。
     * 当使用 [key] 与 [KeyHandle] 时，需要保证至少是一一对应的，例如：
     *
     * ```kotlin
     * key("key").value("value")
     * ```
     * [KeyHandle] 不是线程安全的，不能异步地使用
     *
     *
     */
    public interface KeyHandle : BaseCatCodeBuilder.KeyHandle<Cat, CatCodeBuilder> {
        /**
         * 为当前key设置一个value。如果 [value] 为null则跳过本次设置。
         *
         * 可以通过 [encode] 设置是否对 [value] 进行转义，默认为true。
         * 如果不进行转义则需要自行确保提供的值符合规则，否则可能会导致最终结果与预期不一致。
         *
         * @param encode 是否对 [value] 进行转义，默认为true
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
    
    override fun build(): Cat = catOrBuilder(head, type, properties)
    
    public companion object {
        /**
         * 创建一个 [CatCodeBuilder]。
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
 * 构建一个 catcode.
 */
private fun catOrBuilder(
    head: String = CAT_HEAD, type: String, properties: Map<String, String>,
): LiteralCat {
    require(requireCatHead(head).isNotEmpty()) { "head cannot be empty" }
    require(requireCatType(type).isNotEmpty()) { "type cannot be empty" }
    
    val headCoordinate = 1..head.length
    
    val catcode = buildString(2 + properties.size * 4 + head.length + type.length) {
        append(CAT_PREFIX)
        append(head)
        append(CAT_HEAD_SEPARATOR)
        append(type)
        properties.forEach { (k, v) ->
            append(CAT_PROPERTIES_SEPARATOR)
            append(k).append(CAT_PROPERTY_SEPARATOR).append(CatEscalator.encodeParam(v))
        }
        append(CAT_SUFFIX)
    }
    
    return LiteralCat(
        catcode,
        head,
        headCoordinate,
        type,
        properties.toMap()
    )
}