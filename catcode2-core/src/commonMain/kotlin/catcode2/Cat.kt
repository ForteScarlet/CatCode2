package catcode2


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
 * @author ForteScarlet
 */
public interface Cat {
    
    /**
     * 当前猫猫码的类型。即 `[CAT:type,p=v]` 中的 `type`。
     */
    public val type: String
    
    /**
     * 提供一个 `head` 并得到当前猫猫码。
     *
     */
    public fun toString(head: String = CAT_HEAD): String
    
    /**
     * 得到当前猫猫码。
     *
     */
    override fun toString(): String
    
    /**
     * 根据键获取对应的值。如果未寻得则得到null。
     */
    public operator fun get(key: String): String?
    
    /**
     * 得到当前猫猫码中的全部键。
     */
    public val keys: Set<String>
    
}