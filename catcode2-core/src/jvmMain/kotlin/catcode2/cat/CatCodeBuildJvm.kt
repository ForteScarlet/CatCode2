@file:JvmName("Cats")
@file:JvmMultifileClass

package catcode2.cat

import catcode2.CAT_HEAD
import java.util.*

/**
 * 提供属性集并构建一个 [Cat]。
 */
@JvmName("of")
public fun catOf(type: String, head: String = CAT_HEAD, properties: Properties): Cat {
    return buildCat(type, head) {
        properties.stringPropertyNames().forEach { name ->
            set(name, properties.getProperty(name))
        }
    }
}