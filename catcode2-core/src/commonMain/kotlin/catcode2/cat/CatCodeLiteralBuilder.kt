package catcode2.cat

import catcode2.*
import kotlin.js.JsName
import kotlin.jvm.JvmName
import kotlin.jvm.JvmOverloads
import kotlin.jvm.JvmStatic

/**
 * 用于直接构建 CatCode 字符串地构建器。
 *
 * @author ForteScarlet
 */
public class CatCodeLiteralBuilder private constructor(private val appendable: Appendable) :
    BaseCatCodeBuilder<String, CatCodeLiteralBuilder> {
    private var finished = false
    private lateinit var currentKey: String
    private lateinit var handle: KeyHandle
    
    override fun set(key: String, value: String, encode: Boolean): CatCodeLiteralBuilder = apply {
        checkFinish()
        appendable.append(CAT_PROPERTIES_SEPARATOR).append(key).append(CAT_PROPERTY_SEPARATOR)
        appendable.append(if (encode) CatEscalator.encodeParam(value) else value)
    }
    
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
    public interface KeyHandle : BaseCatCodeBuilder.KeyHandle<String, CatCodeLiteralBuilder> {
        /**
         * 为当前key设置一个value。如果 [value] 为null则跳过本次设置。
         *
         * 可以通过 [encode] 设置是否对 [value] 进行转义，默认为true。
         * 如果不进行转义则需要自行确保提供的值符合规则，否则可能会导致最终结果与预期不一致。
         *
         * @param encode 是否对 [value] 进行转义，默认为true
         */
        override fun value(value: String?, encode: Boolean): CatCodeLiteralBuilder
    }
    
    private inner class KeyHandleImpl : KeyHandle {
        override fun value(value: String?, encode: Boolean): CatCodeLiteralBuilder {
            val key = currentKey
            if (value != null) {
                set(key, value, encode)
            }
            return this@CatCodeLiteralBuilder
        }
    }
    
    
    override fun build(): String {
        if (!finished) {
            appendable.append(CAT_SUFFIX)
        }
        finished = true
        return appendable.toString()
    }
    
    private fun checkFinish() {
        if (finished) {
            throw IllegalStateException("Current builder has finished")
        }
    }
    
    public companion object {
        
        /**
         * 直接构建一个 [CatCodeLiteralBuilder]。
         */
        @JvmStatic
        @JsName("of")
        @JvmOverloads
        public fun of(type: String, head: String = CAT_HEAD): CatCodeLiteralBuilder =
            CatCodeLiteralBuilder(StringBuilder().initAppendable(head, type))
        
        
        /**
         * 通过 [appendable] 构建一个 [CatCodeLiteralBuilder]。
         */
        @JvmStatic
        @JvmName("of")
        @JvmOverloads
        @JsName("ofAppendable")
        public fun of(
            type: String,
            head: String = CAT_HEAD,
            appendable: Appendable,
        ): CatCodeLiteralBuilder = CatCodeLiteralBuilder(appendable.initAppendable(head, type))
        
        
        /**
         * 通过提供 [Appendable] 的初始容量 [capacity] 构建一个 [CatCodeLiteralBuilder]。
         */
        @JvmStatic
        @JvmName("of")
        @JsName("ofCapacity")
        @JvmOverloads
        public fun of(type: String, head: String = CAT_HEAD, capacity: Int): CatCodeLiteralBuilder =
            CatCodeLiteralBuilder(StringBuilder(capacity).initAppendable(head, type))
        
        
        private fun Appendable.initAppendable(head: String, type: String): Appendable = apply {
            require(requireCatHead(head).isNotEmpty()) { "head cannot be empty" }
            require(requireCatType(type).isNotEmpty()) { "type cannot be empty" }
            append(CAT_PREFIX).append(head).append(CAT_HEAD_SEPARATOR).append(type)
            
        }
        
    }
}