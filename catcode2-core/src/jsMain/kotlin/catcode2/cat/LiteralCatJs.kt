package catcode2.cat

import catcode2.CAT_HEAD

private fun Array<dynamic>.toMap(): Map<String, String> {
    return when (size) {
        0 -> emptyMap()
        1 -> first().let { it ->
            val key = it.key!!.toString()
            val value = it.value!!.toString()
            mapOf(key to value)
        } as Map<String, String>
        
        else -> associate { it ->
            val key = it.key!!.toString()
            val value = it.value!!.toString()
            
            key to value
        }
    }
}

private fun toMap(dyn: dynamic): Map<String, String> {
    val keys = js("Object.keys(dyn)")
    
    if (keys == null || keys.length == 0) {
        return emptyMap()
    }
    
    return buildMap {
        for (key in keys) {
            this[key.toString()] = dyn[key].toString()
        }
    }
}


/**
 * 通过键值对数组来作为目标cat code的属性键值对。
 *
 * ```js
 * catOfProperties('foo-type', 'CAT', [
 *      { key: 'key', value: 'value' },
 *      { key: 'key', value: 'value' }
 * ])
 * ```
 *
 * 在 [properties] 中你需要至少保证其存在 `key` 和 `value` 属性。
 *
 */
@JsExport
public fun catOfProperties(type: String, head: String = CAT_HEAD, properties: Array<dynamic>): Cat {
    return catOf(type, head, properties.toMap())
}

/**
 * 通过键值对数组来作为目标cat code的属性键值对。
 *
 * ```js
 * catOfProperties('foo-type', 'CAT', {
 *     key1: 'value1'
 *     key2: 'value2'
 * })
 * ```
 *
 * [entity] 应为一个对象。
 *
 */
@JsExport
public fun catOfEntity(type: String, head: String = CAT_HEAD, entity: dynamic): Cat {
    return catOf(type, head, toMap(entity))
}

