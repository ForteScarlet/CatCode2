package catcode2.serialization

import catcode2.cat.CatLiteralBuilder
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.AbstractEncoder
import kotlinx.serialization.encoding.CompositeEncoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.modules.SerializersModule


@OptIn(ExperimentalSerializationApi::class)
internal class CatCodeStringEncoder(
    catCode: CatCode,
    val catBuilder: CatLiteralBuilder,
) : Encoder, CompositeEncoder, AbstractEncoder() {
    override val serializersModule: SerializersModule = catCode.serializersModule
    private val configuration = catCode.configuration
    private var currentElement: CatLiteralBuilder.KeyHandle? = null
    
    override fun shouldEncodeElementDefault(descriptor: SerialDescriptor, index: Int): Boolean {
        return configuration.encodeDefaults
    }
    
    override fun <T> encodeSerializableValue(serializer: SerializationStrategy<T>, value: T) {
        println("encodeSerializableValue(serializer = $serializer, value = $value)")
        super<AbstractEncoder>.encodeSerializableValue(serializer, value)
    }
    
    override fun beginStructure(descriptor: SerialDescriptor): CompositeEncoder {
        println("beginStructure(descriptor = $descriptor)")
        println("descriptor.serialName = ${descriptor.serialName}")
        return super.beginStructure(descriptor)
    }
    
    override fun endStructure(descriptor: SerialDescriptor) {
        println("endStructure(descriptor = $descriptor)")
        super.endStructure(descriptor)
    }
    
    
    override fun encodeElement(descriptor: SerialDescriptor, index: Int): Boolean {
        println("encodeElement(descriptor = $descriptor, index = $index)")
        println("descriptor.serialName = ${descriptor.serialName}")
        println("descriptor.getElementName($index) = ${descriptor.getElementName(index)}")
    
        currentElement = catBuilder.key(descriptor.getElementName(index))
        return true
        // return super.encodeElement(descriptor, index)
    }
    
    override fun <T : Any> encodeNullableSerializableElement(
        descriptor: SerialDescriptor,
        index: Int,
        serializer: SerializationStrategy<T>,
        value: T?,
    ) {
        super.encodeNullableSerializableElement(descriptor, index, serializer, value)
    }
    
    override fun encodeValue(value: Any) {
        currentElement?.value(value.toString())
        // println("encodeValue(value = $value)")
        // super.encodeValue(value)
    }
    
}

