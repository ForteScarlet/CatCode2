package catcode2.cat

import kotlin.js.JsExport
import kotlin.js.JsName


/**
 *
 * 一个猫猫码。
 *
 * 猫猫码是一个类似于 `properties` 格式的单行字符串键值对数据结构，其格式如：
 * ```catcode
 * [HEAD:type,p1=v1,p2=v2]
 * ```
 *
 * 比如：
 * ```catcode
 * [CAT:user,name=forte]
 * ```
 *
 * ### HEAD
 *
 * 猫猫码的头标识，位于起始位置，只能为英文字母，区分大小写 (a-z, A-Z)。
 *
 * ### type
 *
 * 在 `HEAD` 后跟随一个英文冒号 `:`, 而后跟随此猫猫码所代表的 '类型'，只能为英文字母，区分大小写 (a-z, A-Z)。
 *
 * ### properties
 *
 * 如果存在键值对，在 `type` 后跟随一个英文逗号 `,`，而后为属性键值对
 *
 *
 * @author ForteScarlet
 */
@JsExport
public interface Cat {
    
    /**
     * 当前猫猫码的头类型。即 `[HEAD:type,p=v]` 中的 `HEAD`。
     *
     * 通常来讲，解析而得的 [Cat] 中 [head] 为解析目标的具体头类型，
     * 而手动构建的 [Cat] 中 [head] 如未指定则默认为 [CAT_HEAD][catcode2.CAT_HEAD]。
     */
    public val head: String
    
    /**
     * 当前猫猫码的类型。即 `[CAT:type,p=v]` 中的 `type`。
     */
    public val type: String
    
    /**
     * 得到当前猫猫码。
     *
     */
    override fun toString(): String
    
    /**
     * 提供一个 [head] 并得到当前所表示的猫猫码字符串。
     *
     * 当 [head] 为null时视为使用当前 [head][Cat.head]。
     *
     */
    public fun toCode(head: String? = null): String
    
    /**
     * 根据键获取对应的值。如果未寻得则得到null。
     */
    @JsName("get")
    public operator fun get(key: String): String?
    
    /**
     * 得到当前猫猫码中的全部键。
     */
    @Suppress("NON_EXPORTABLE_TYPE")
    @JsName("_keys")
    public val keys: Set<String>
    
    /**
     * 得到当前猫猫码中的全部键，以数组的形式呈现。主要服务于 JS 平台。
     */
    @JsName("keys")
    public val keysArray: Array<String> get() = keys.toTypedArray()
    
    /**
     * 得到当前猫猫码中所有属性的数量。应当与 [keys] 的元素数量一致。[size] 始终大于等于0。
     */
    public val size: Int
    
    /**
     * 得到指定[索引][index]下的属性键。
     * 如果索引超出范围则会抛出 [IndexOutOfBoundsException] 异常。
     */
    public fun keyAt(index: Int): String
    
    /**
     * 得到指定[索引][index]下的属性键。
     * 如果索引超出范围则会得到null。
     */
    public fun keyAtOrNull(index: Int): String?
    
    
    /**
     * 得到指定[索引][index]下的属性值。
     * 如果索引超出范围则会抛出 [IndexOutOfBoundsException] 异常。
     */
    public fun valueAt(index: Int): String
    
    /**
     * 得到指定[索引][index]下的属性值。
     * 如果索引超出范围则会得到null。
     */
    public fun valueAtOrNull(index: Int): String?
    
}

/**
 *
 * 等同于:
 *
 * ```kotlin
 * val catcode = cat.toCode()
 * ```
 */
public inline val Cat.code: String get() = toCode()

/**
 * [Cat] 中属性是否为空。
 */
public fun Cat.isEmpty(): Boolean = size == 0

/**
 * [Cat] 中是否存在任意属性。与 [Cat.isEmpty] 相反。
 */
public fun Cat.isNotEmpty(): Boolean = !isEmpty()
