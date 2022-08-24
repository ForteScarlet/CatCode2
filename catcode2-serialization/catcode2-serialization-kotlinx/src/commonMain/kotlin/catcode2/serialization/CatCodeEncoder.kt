package catcode2.serialization

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerializationException
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.CompositeEncoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.modules.SerializersModule


@OptIn(ExperimentalSerializationApi::class)
internal abstract class CatCodeEncoder<Tag : Any> : Encoder, CompositeEncoder { // , NamedValueEncoder()
    abstract val catCode: CatCode
    /*
     *  [CatCodeEncoder] 内大部分实现灵感与参考来源自 `kotlinx.serialization.internal.Tagged.kt`, 版权信息:
     *
     *  > Copyright 2017-2020 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
     */
    
    /**
     * Provides a tag object for given serial descriptor and index.
     * Tag object allows associating given user information with a particular element of composite serializable entity.
     */
    protected abstract fun SerialDescriptor.getTag(index: Int): Tag
    
    abstract override val serializersModule: SerializersModule
    
    // ---- API ----
    protected abstract fun encodeTaggedValue(tag: Tag, value: Any)
    
    protected open fun encodeTaggedNonNullMark(tag: Tag) {}
    protected open fun encodeTaggedNull(tag: Tag): Unit = throw SerializationException("null is not supported")
    protected open fun encodeTaggedInt(tag: Tag, value: Int): Unit = encodeTaggedValue(tag, value)
    protected open fun encodeTaggedByte(tag: Tag, value: Byte): Unit = encodeTaggedValue(tag, value)
    protected open fun encodeTaggedShort(tag: Tag, value: Short): Unit = encodeTaggedValue(tag, value)
    protected open fun encodeTaggedLong(tag: Tag, value: Long): Unit = encodeTaggedValue(tag, value)
    protected open fun encodeTaggedFloat(tag: Tag, value: Float): Unit = encodeTaggedValue(tag, value)
    protected open fun encodeTaggedDouble(tag: Tag, value: Double): Unit = encodeTaggedValue(tag, value)
    protected open fun encodeTaggedBoolean(tag: Tag, value: Boolean): Unit = encodeTaggedValue(tag, value)
    protected open fun encodeTaggedChar(tag: Tag, value: Char): Unit = encodeTaggedValue(tag, value)
    protected open fun encodeTaggedString(tag: Tag, value: String): Unit = encodeTaggedValue(tag, value)
    
    protected open fun encodeTaggedEnum(
        tag: Tag,
        enumDescriptor: SerialDescriptor,
        ordinal: Int,
    ): Unit = encodeTaggedValue(tag, ordinal)
    
    protected open fun encodeTaggedInline(tag: Tag, inlineDescriptor: SerialDescriptor): Encoder =
        this.apply { pushTag(tag) }
    
    final override fun encodeInline(descriptor: SerialDescriptor): Encoder =
        encodeTaggedInline(popTag(), descriptor)
    
    // ---- Implementation of low-level API ----
    
    private fun encodeElement(desc: SerialDescriptor, index: Int): Boolean {
        val tag = desc.getTag(index)
        pushTag(tag)
        return true
    }
    
    override fun encodeNotNullMark(): Unit = encodeTaggedNonNullMark(currentTag)
    override fun encodeNull(): Unit = encodeTaggedNull(popTag())
    override fun encodeBoolean(value: Boolean): Unit = encodeTaggedBoolean(popTag(), value)
    override fun encodeByte(value: Byte): Unit = encodeTaggedByte(popTag(), value)
    override fun encodeShort(value: Short): Unit = encodeTaggedShort(popTag(), value)
    override fun encodeInt(value: Int): Unit = encodeTaggedInt(popTag(), value)
    override fun encodeLong(value: Long): Unit = encodeTaggedLong(popTag(), value)
    override fun encodeFloat(value: Float): Unit = encodeTaggedFloat(popTag(), value)
    override fun encodeDouble(value: Double): Unit = encodeTaggedDouble(popTag(), value)
    override fun encodeChar(value: Char): Unit = encodeTaggedChar(popTag(), value)
    override fun encodeString(value: String): Unit = encodeTaggedString(popTag(), value)
    
    final override fun encodeEnum(
        enumDescriptor: SerialDescriptor,
        index: Int,
    ): Unit = encodeTaggedEnum(popTag(), enumDescriptor, index)
    
    override fun beginStructure(descriptor: SerialDescriptor): CompositeEncoder = this
    
    final override fun endStructure(descriptor: SerialDescriptor) {
        if (tagStack.isNotEmpty()) {
            popTag()
        }
        endEncode(descriptor)
    }
    
    /**
     * Format-specific replacement for [endStructure], because latter is overridden to manipulate tag stack.
     */
    protected open fun endEncode(descriptor: SerialDescriptor) {}
    
    final override fun encodeBooleanElement(descriptor: SerialDescriptor, index: Int, value: Boolean): Unit =
        encodeTaggedBoolean(descriptor.getTag(index), value)
    
    final override fun encodeByteElement(descriptor: SerialDescriptor, index: Int, value: Byte): Unit =
        encodeTaggedByte(descriptor.getTag(index), value)
    
    final override fun encodeShortElement(descriptor: SerialDescriptor, index: Int, value: Short): Unit =
        encodeTaggedShort(descriptor.getTag(index), value)
    
    final override fun encodeIntElement(descriptor: SerialDescriptor, index: Int, value: Int): Unit =
        encodeTaggedInt(descriptor.getTag(index), value)
    
    final override fun encodeLongElement(descriptor: SerialDescriptor, index: Int, value: Long): Unit =
        encodeTaggedLong(descriptor.getTag(index), value)
    
    final override fun encodeFloatElement(descriptor: SerialDescriptor, index: Int, value: Float): Unit =
        encodeTaggedFloat(descriptor.getTag(index), value)
    
    final override fun encodeDoubleElement(descriptor: SerialDescriptor, index: Int, value: Double): Unit =
        encodeTaggedDouble(descriptor.getTag(index), value)
    
    final override fun encodeCharElement(descriptor: SerialDescriptor, index: Int, value: Char): Unit =
        encodeTaggedChar(descriptor.getTag(index), value)
    
    final override fun encodeStringElement(descriptor: SerialDescriptor, index: Int, value: String): Unit =
        encodeTaggedString(descriptor.getTag(index), value)
    
    final override fun encodeInlineElement(
        descriptor: SerialDescriptor,
        index: Int,
    ): Encoder {
        return encodeTaggedInline(descriptor.getTag(index), descriptor.getElementDescriptor(index))
    }
    
    override fun <T : Any?> encodeSerializableElement(
        descriptor: SerialDescriptor,
        index: Int,
        serializer: SerializationStrategy<T>,
        value: T,
    ) {
        if (encodeElement(descriptor, index))
            encodeSerializableValue(serializer, value)
    }
    
    @OptIn(ExperimentalSerializationApi::class)
    override fun <T : Any> encodeNullableSerializableElement(
        descriptor: SerialDescriptor,
        index: Int,
        serializer: SerializationStrategy<T>,
        value: T?,
    ) {
        if (encodeElement(descriptor, index))
            encodeNullableSerializableValue(serializer, value)
    }
    
    private val tagStack = arrayListOf<Tag>()
    private val currentTag: Tag
        get() = tagStack.last()
    
    protected val currentTagOrNull: Tag?
        get() = tagStack.lastOrNull()
    
    private fun pushTag(name: Tag) {
        tagStack.add(name)
    }
    
    private fun popTag(): Tag =
        if (tagStack.isNotEmpty())
            tagStack.removeAt(tagStack.lastIndex)
        else
            throw SerializationException("No tag in stack for requested element")
}
