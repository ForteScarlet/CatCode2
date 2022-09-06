package catcode2.serialization

import catcode2.CAT_HEAD
import catcode2.checkCatCode
import catcode2.walkCatCodeProperties
import catcode2.walkCatCodePropertiesLeniently
import kotlin.js.JsExport
import kotlin.jvm.JvmField


/**
 *
 * [CatCode] 使用配置类。
 *
 * @author ForteScarlet
 */
@JsExport
public data class CatCodeConfiguration internal constructor(
    public val catHead: String = CAT_HEAD,
    public val classDiscriminator: String = "type",
    public val valueDiscriminator: String = "value",
    /**
     * 序列化时，当值为null, 是否输出空值。例如 `User(username=null)`，则输出的属性为 `[CAT:user,username=]`。
     *
     * 默认情况下值为null时不输出此属性。
     */
    public val encodeNullAsEmpty: Boolean = false,
    
    /**
     * 反序列化时，当值为空且属性可为null，是否输出null。例如 `[CAT:user,username=]`，
     * 如果为 `true` 则反序列化结果的 `username` 为 `null` 而不是空字符串。
     */
    public val decodeEmptyAsNull: Boolean = true,
    
    /**
     * 是否在对指定catcode反序列化时使用更宽松的格式校验。
     * 如果开启宽松模式，则解析catcode时将不会对HEAD和type进行校验，
     * 只要能够通过 [walkCatCodePropertiesLeniently] 的校验即可。
     *
     * 默认情况下会使用 [walkCatCodeProperties], 在遍历前会先通过 [checkCatCode] 校验其格式。
     */
    public val isLenient: Boolean = false,
) {
    public companion object {
        @JvmField
        public val Default: CatCodeConfiguration = CatCodeConfiguration()
    }
}

