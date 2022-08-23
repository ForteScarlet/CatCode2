package catcode2.serialization

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialInfo

@ExperimentalSerializationApi
@SerialInfo
@Target(AnnotationTarget.CLASS)
public annotation class CatCodeTypeName(val name: String)
