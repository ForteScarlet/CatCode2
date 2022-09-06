@file:JvmName("CatCodes")

package catcode2.serialization

import catcode2.cat.BaseCatCodeBuilder
import catcode2.cat.CatCodeLiteralBuilder
import catcode2.getCatParamEncode
import catcode2.requireCatCode
import catcode2.walkCatCodePropertiesLenientlyContinuously
import kotlinx.serialization.*
import kotlinx.serialization.descriptors.PolymorphicKind
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.StructureKind
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.modules.EmptySerializersModule
import kotlinx.serialization.modules.SerializersModule
import kotlin.jvm.JvmName

/**
 * 猫猫码的序列化器。
 *
 */
public sealed class CatCode(
    override val serializersModule: SerializersModule,
    public val configuration: CatCodeConfiguration,
) : StringFormat {
    public companion object Default : CatCode(EmptySerializersModule(), CatCodeConfiguration.Default)
    
    
    // region decode
    override fun <T> decodeFromString(deserializer: DeserializationStrategy<T>, string: String): T =
        decodeFromCatCodeLiteral(deserializer, string)
    
    
    private fun <T> decodeFromCatCodeLiteral(deserializer: DeserializationStrategy<T>, catcode: String): T {
        val properties = mutableMapOf<String, String>()
        
        if (configuration.isLenient) {
            requireCatCode(catcode)
        }
        
        walkCatCodePropertiesLenientlyContinuously(catcode, walk = properties::set)
        
        return deserializer.deserialize(CatCodeLiteralDecoder(this, deserializer.descriptor, properties))
    }
    // endregion
    
    // region encode
    /**
     * 序列化 [value] 为catcode字符串。
     */
    override fun <T> encodeToString(serializer: SerializationStrategy<T>, value: T): String =
        encodeToString(serializer, value, configuration.catHead)
    
    /**
     * 序列化 [value] 为catcode字符串。
     */
    @Suppress("MemberVisibilityCanBePrivate")
    public fun <T> encodeToString(serializer: SerializationStrategy<T>, value: T, catHead: String): String =
        encodeToCatCodeLiteral(serializer, value, catHead)
    
    
    private fun <T> encodeToCatCodeLiteral(serializer: SerializationStrategy<T>, value: T, catHead: String): String {
        val builder = CatCodeLiteralBuilder.of(catTypeName(serializer), catHead)
        val encoder = CatCodeLiteralEncoder(this, builder)
        encoder.encodeSerializableValue(serializer, value)
        return builder.build()
    }
    
    @OptIn(ExperimentalSerializationApi::class)
    private fun catTypeName(serializer: SerializationStrategy<*>): String {
        for (annotation in serializer.descriptor.annotations) {
            if (annotation is CatCodeTypeName) {
                return annotation.name
            }
        }
        
        return serializer.descriptor.serialName
    }
    
    // endregion
}


private class CatCodeLiteralEncoder(
    override val catCode: CatCode,
    private val catBuilder: BaseCatCodeBuilder<String, *>,
) : CatCodeEncoder<String>() {
    override val serializersModule: SerializersModule = catCode.serializersModule
    private val configuration = catCode.configuration
    
    override fun SerialDescriptor.getTag(index: Int): String = nested(elementName(this, index))
    
    private fun nested(nestedName: String): String = composeName(currentTagOrNull ?: "", nestedName)
    
    @OptIn(ExperimentalSerializationApi::class)
    private fun elementName(descriptor: SerialDescriptor, index: Int): String {
        if (descriptor.kind !is PolymorphicKind) {
            return descriptor.getElementName(index)
        }
        
        return when (val elementName = descriptor.getElementName(index)) {
            "type" -> configuration.classDiscriminator
            "value" -> configuration.valueDiscriminator
            else -> elementName
        }
    }
    
    
    private fun composeName(parentName: String, childName: String): String =
        if (parentName.isEmpty()) childName else "$parentName.$childName"
    
    
    override fun encodeTaggedValue(tag: String, value: Any) {
        encode(tag, value.toString())
    }
    
    override fun encodeTaggedBoolean(tag: String, value: Boolean) {
        encode(tag, value.toString(), false)
    }
    
    override fun encodeTaggedByte(tag: String, value: Byte) {
        encode(tag, value.toString(), false)
    }
    
    override fun encodeTaggedInt(tag: String, value: Int) {
        encode(tag, value.toString(), false)
    }
    
    override fun encodeTaggedDouble(tag: String, value: Double) {
        encode(tag, value.toString(), false)
    }
    
    override fun encodeTaggedFloat(tag: String, value: Float) {
        encode(tag, value.toString(), false)
    }
    
    override fun encodeTaggedShort(tag: String, value: Short) {
        encode(tag, value.toString(), false)
    }
    
    override fun encodeTaggedChar(tag: String, value: Char) {
        val encoded = getCatParamEncode(value) ?: value.toString()
        encode(tag, encoded, false)
    }
    
    override fun encodeTaggedLong(tag: String, value: Long) {
        encode(tag, value.toString(), false)
    }
    
    override fun encodeTaggedNull(tag: String) {
        if (configuration.encodeNullAsEmpty) {
            encode(tag, "", false)
        }
    }
    
    @OptIn(ExperimentalSerializationApi::class)
    override fun encodeTaggedEnum(tag: String, enumDescriptor: SerialDescriptor, ordinal: Int) {
        encode(key = tag, value = enumDescriptor.getElementName(ordinal))
    }
    
    private fun encode(key: String, value: String, encode: Boolean = true) {
        catBuilder.set(key, value, encode)
    }
}


private class CatCodeLiteralDecoder(
    override val catCode: CatCode,
    descriptor: SerialDescriptor,
    private val properties: Map<String, String>,
) : CatCodeDecoder<String>() {
    private var currentIndex = 0
    
    @OptIn(ExperimentalSerializationApi::class)
    private val isCollection = descriptor.kind == StructureKind.LIST || descriptor.kind == StructureKind.MAP
    
    @OptIn(ExperimentalSerializationApi::class)
    private val size = if (isCollection) Int.MAX_VALUE else descriptor.elementsCount
    
    override val serializersModule: SerializersModule = catCode.serializersModule
    override fun SerialDescriptor.getTag(index: Int): String = nested(elementName(this, index))
    
    private fun nested(nestedName: String): String = composeName(currentTagOrNull ?: "", nestedName)
    
    @OptIn(ExperimentalSerializationApi::class)
    private fun elementName(desc: SerialDescriptor, index: Int): String = desc.getElementName(index)
    private fun composeName(parentName: String, childName: String): String =
        if (parentName.isEmpty()) childName else "$parentName.$childName"
    
    private fun structure(descriptor: SerialDescriptor): CatCodeLiteralDecoder =
        CatCodeLiteralDecoder(catCode, descriptor, properties)
    
    
    override fun beginStructure(descriptor: SerialDescriptor): CompositeDecoder {
        return structure(descriptor).also { copyTagsTo(it) }
    }
    
    override fun decodeElementIndex(descriptor: SerialDescriptor): Int {
        while (currentIndex < size) {
            val name = descriptor.getTag(currentIndex++)
            if (properties.keys.any {
                    it.startsWith(name) && (it.length == name.length || it[name.length] == '.')
                }) return currentIndex - 1
            if (isCollection) {
                // if map does not contain key we look for, then indices in collection have ended
                break
            }
        }
        return CompositeDecoder.DECODE_DONE
    }
    
    @OptIn(ExperimentalSerializationApi::class)
    override fun decodeTaggedEnum(tag: String, enumDescriptor: SerialDescriptor): Int {
        val taggedValue = properties.getValue(tag)
        return enumDescriptor.getElementIndex(taggedValue)
            .also { if (it == CompositeDecoder.UNKNOWN_NAME) throw SerializationException("Enum '${enumDescriptor.serialName}' does not contain element with name '$taggedValue'") }
    }
    
    
    override fun decodeTaggedValue(tag: String): String = properties.getValue(tag)
    
    override fun decodeTaggedBoolean(tag: String): Boolean = decodeTaggedValue(tag).toBoolean()
    override fun decodeTaggedByte(tag: String): Byte = decodeTaggedValue(tag).toByte()
    override fun decodeTaggedShort(tag: String): Short = decodeTaggedValue(tag).toShort()
    override fun decodeTaggedInt(tag: String): Int = decodeTaggedValue(tag).toInt()
    override fun decodeTaggedLong(tag: String): Long = decodeTaggedValue(tag).toLong()
    override fun decodeTaggedFloat(tag: String): Float = decodeTaggedValue(tag).toFloat()
    override fun decodeTaggedDouble(tag: String): Double = decodeTaggedValue(tag).toDouble()
    override fun decodeTaggedChar(tag: String): Char = decodeTaggedValue(tag).single()
}


private class CatCodeImpl(serializersModule: SerializersModule, configuration: CatCodeConfiguration) :
    CatCode(serializersModule, configuration) {
    init {
        configuration.validate()
    }
    
    private fun CatCodeConfiguration.validate() {
        require(classDiscriminator.isNotEmpty()) { "'classDiscriminator' cannot be empty" }
        require(valueDiscriminator.isNotEmpty()) { "'valueDiscriminator' cannot be empty" }
        require(classDiscriminator != valueDiscriminator) { "'classDiscriminator' and 'valueDiscriminator' cannot be the same, but they are both '$classDiscriminator' now" }
        require(catHead.isNotEmpty()) { "'catHead' cannot be empty" }
        
    }
}


/**
 * 通过 [CatCodeBuilder] 构建一个 [CatCode]。
 *
 * ```kotlin
 * val catCode = CatCode {
 *     // config
 * }
 * ```
 *
 */
@JvmName("buildCatCode")
public fun CatCode(builder: CatCodeBuilderFunction): CatCode {
    return CatCodeBuilder().apply {
        builder.apply { invoke() }
    }.buildToCatCode()
}

/**
 * 使用在 [CatCode] 函数上的函数类型，相当于 `CatCodeBuilder.() -> Unit`
 *
 */
public fun interface CatCodeBuilderFunction {
    public operator fun CatCodeBuilder.invoke()
}


private fun CatCodeBuilder.buildToCatCode(): CatCode {
    return CatCodeImpl(
        serializersModule, CatCodeConfiguration(
            catHead = catHead,
            classDiscriminator = classDiscriminator,
            valueDiscriminator = valueDiscriminator,
            encodeNullAsEmpty = encodeNullAsEmpty,
            decodeEmptyAsNull = decodeEmptyAsNull,
            isLenient = isLenient
        )
    )
}

