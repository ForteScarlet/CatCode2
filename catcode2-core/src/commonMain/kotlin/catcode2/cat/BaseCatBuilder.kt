package catcode2.cat


/**
 * 对不同类型的 catcode 构建器的统一接口。
 * @author ForteScarlet
 */
public interface BaseCatBuilder<out C, out Builder : BaseCatBuilder<C, Builder>> {
    
    /**
     * 直接设置一个键值对。
     *
     * ```kotlin
     * buildCat(type = "foo") {
     *   "key" - "u&me"
     *   "name" - "forte"
     * }
     * ```
     *
     */
    public operator fun String.minus(value: String): Builder = set(this, value)
    
    /**
     * 直接设置一个键值对。
     *
     * ```kotlin
     * buildCat(type = "foo") {
     *    set("key", "u&me")
     *    set("name", "forte", false)
     * }
     * ```
     */
    public fun set(key: String, value: String, encode: Boolean = true): Builder
    
    /**
     * 直接设置一个键值对。
     *
     * ```kotlin
     * buildCat(type = "foo") {
     *    set("key", "u&me")
     *    set("name", "forte", false)
     * }
     * ```
     */
    public fun set(key: String, value: String): Builder = set(key, value, true)
    
    /**
     * 提供一个 [key] 并得到此key的 [KeyHandle]。
     *
     * ```kotlin
     * buildCat(type = "foo") {
     *   key("key") value "u&me"
     *   key("name").value("forte", false)
     * }
     * ```
     *
     * @see KeyHandle
     */
    public fun key(key: String): KeyHandle<C, Builder>
    
    /**
     * 通过 [CatBuilder.key] 得到对, 指定的key指设置结果。
     *
     * [CatBuilder.key] 与 [KeyHandle] 不可交叉使用，否则可能会导致预期外的结果。
     * 当使用 [key] 与 [KeyHandle] 时，需要保证至少是一一对应的，例如：
     *
     * ```kotlin
     * key("key").value("value")
     * ```
     * [KeyHandle] 不是线程安全的，不能异步地使用
     *
     *
     */
    public interface KeyHandle<out C, out Builder : BaseCatBuilder<C, Builder>> {
        /**
         * 为当前key设置一个value。如果 [value] 为null则跳过本次设置。
         *
         * 可以通过 [encode] 设置是否对 [value] 进行转义，默认为true。
         * 如果不进行转义则需要自行确保提供的值符合规则，否则可能会导致最终结果与预期不一致。
         *
         * @param encode 是否对 [value] 进行转义，默认为true
         */
        public fun value(value: String?, encode: Boolean = true): Builder
        
        /**
         * 为当前key设置一个value。如果 [value] 为null则跳过本次设置。
         */
        public infix fun value(value: String?): Builder = value(value, true)
    }
    
    /**
     * 构建并得到结果。
     */
    public fun build(): C
    
}