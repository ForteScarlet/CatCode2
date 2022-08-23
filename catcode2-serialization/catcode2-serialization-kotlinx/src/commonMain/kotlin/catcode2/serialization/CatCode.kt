package catcode2.serialization

import catcode2.cat.CatLiteralBuilder
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.StringFormat
import kotlinx.serialization.modules.EmptySerializersModule
import kotlinx.serialization.modules.SerializersModule

/**
 * 猫猫码的序列化器。
 *
 */
public sealed class CatCode(
    public val configuration: CatCodeConfiguration,
    override val serializersModule: SerializersModule,
) : StringFormat {
    override fun <T> decodeFromString(deserializer: DeserializationStrategy<T>, string: String): T {
        TODO("Not yet implemented")
    }
    
    override fun <T> encodeToString(serializer: SerializationStrategy<T>, value: T): String =
        encodeToCatCode(serializer, value)
    
    public companion object Default : CatCode(CatCodeConfiguration(), EmptySerializersModule())
    
    
}


private fun <T> CatCode.encodeToCatCode(serializer: SerializationStrategy<T>, value: T): String {
    val builder = CatLiteralBuilder.of(catTypeName(serializer), configuration.catHead)
    val encoder = CatCodeStringEncoder(this, builder)
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