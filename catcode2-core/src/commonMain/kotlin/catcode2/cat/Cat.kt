package catcode2.cat

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
public interface Cat {
    
    /**
     * 当前猫猫码的头类型。即 `[HEAD:type,p=v]` 中的 `HEAD`。
     *
     * 通常来讲，解析而得的 [Cat] 中 [head] 为解析目标的具体头类型，
     * 而手动构建的 [Cat] 中 [head] 如未指定则默认为 [CAT_HEAD]。
     */
    public val head: String
    
    /**
     * 当前猫猫码的类型。即 `[CAT:type,p=v]` 中的 `type`。
     */
    public val type: String
    
    /**
     * 提供一个 `head` 并得到当前猫猫码。
     *
     */
    @JsName("toStringWithHead")
    public fun toString(head: String = this.head): String
    
    /**
     * 得到当前猫猫码。
     *
     */
    override fun toString(): String
    
    /**
     * 根据键获取对应的值。如果未寻得则得到null。
     */
    @JsName("get")
    public operator fun get(key: String): String?
    
    /**
     * 得到当前猫猫码中的全部键。
     */
    public val keys: Set<String>
    
}