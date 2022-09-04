@file:JvmName("CatCodes")

package catcode2

import catcode2.annotation.Api4Jvm
import java.util.function.BiConsumer
import java.util.function.Consumer

// @Api4Jvm
// @JvmOverloads
// public fun walk(
//     codeValue: String,
//     decodeValue: Boolean = true,
//     perceiveHead: Consumer<String>? = null,
//     perceiveType: Consumer<String>? = null,
//     perceiveProperty: BiConsumer<String, String>? = null,
// ) {
//     walkCatCode(
//         codeValue = codeValue,
//         decodeValue = decodeValue,
//         perceiveHead = {
//             perceiveHead?.accept(it)
//         },
//         perceiveType = {
//             perceiveType?.accept(it)
//         },
//         perceiveProperty = { k, v ->
//             perceiveProperty?.accept(k, v)
//         }
//     )
// }