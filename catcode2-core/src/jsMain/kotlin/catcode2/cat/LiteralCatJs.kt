package catcode2.cat

import catcode2.CAT_HEAD


@JsExport
public data class Property(val key: String, val value: String)

private fun Array<Property>.toMap(): Map<String, String> {
    return when (size) {
        0 -> emptyMap()
        1 -> first().let { (k, v) -> mapOf(k to v) }
        else -> associate { (k, v) -> k to v }
    }
}

@JsExport
public fun catOfProperties(type: String, head: String = CAT_HEAD, properties: Array<Property>): Cat {
    return catOf(type, head, properties.toMap())
}

