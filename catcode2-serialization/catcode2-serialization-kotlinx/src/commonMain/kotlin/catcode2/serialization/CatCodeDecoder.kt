package catcode2.serialization

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.AbstractDecoder
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.modules.SerializersModule


public interface CatCodeDecoder : Decoder, CompositeDecoder {
    public val catCode: CatCode
    
}


@OptIn(ExperimentalSerializationApi::class)
public class CatCodeStringDecoder(
    private val value: String,
    override val catCode: CatCode,
) : CatCodeDecoder,
    AbstractDecoder() {
    override val serializersModule: SerializersModule = catCode.serializersModule
    
    private var currentIndex = -1
    
    override fun decodeElementIndex(descriptor: SerialDescriptor): Int {
        TODO("Not yet implemented")
    }
    
}