@file:JvmName("CatCodes")
@file:JvmMultifileClass

package catcode2

import catcode2.annotation.Api4J
import java.util.function.BiConsumer
import java.util.function.Consumer

/**
 * 解析一个猫猫码字符串中的所有属性, 包括 head、type 和所有的属性键值对。
 *
 * @see walkCatCode
 */
@JvmOverloads
@Api4J
@JvmName("walkCatCodeContinuously")
public fun walkCatCodeContinuously4J(
    catCode: String,
    decodeValue: Boolean = true,
    perceiveHead: Consumer<String>? = null,
    perceiveType: Consumer<String>? = null,
    perceiveProperty: BiConsumer<String, String>,
) {
    walkCatCodeContinuously(
        catCode, decodeValue,
        perceiveHead = { perceiveHead?.accept(it) },
        perceiveType = { perceiveType?.accept(it) },
        perceiveProperty = { key, value -> perceiveProperty.accept(key, value) }
    )
}

/**
 * 解析一个猫猫码字符串中的所有属性键值对。
 *
 * @see walkCatCode
 */
@JvmOverloads
@Api4J
@JvmName("walkCatCodePropertiesContinuously")
public fun walkCatCodePropertiesContinuously4J(
    catCode: String,
    decodeValue: Boolean = true,
    perceiveProperty: BiConsumer<String, String>,
) {
    walkCatCodePropertiesContinuously(catCode, decodeValue) { key, value ->
        perceiveProperty.accept(key, value)
    }
}
