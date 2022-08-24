package catcode2.serialization

import kotlinx.serialization.modules.EmptySerializersModule
import kotlinx.serialization.modules.SerializersModule


/**
 *
 * 用于构建 [CatCode] 的配置类。
 *
 * @author ForteScarlet
 */
public class CatCodeBuilder {
    /**
     * 配置 [SerializersModule]。
     */
    public var serializersModule: SerializersModule = EmptySerializersModule()
    
    // region configuration properties
    /**
     * encode时使用的 CAT HEAD。
     * @see CatCodeConfiguration.catHead
     */
    public var catHead: String = CatCodeConfiguration.Default.catHead
    
    /**
     * 多态情况下的类鉴别器属性名。
     * @see CatCodeConfiguration.classDiscriminator
     */
    public var classDiscriminator: String = CatCodeConfiguration.Default.classDiscriminator
    
    /**
     * 多态情况下的值鉴别器属性名。`。
     * @see CatCodeConfiguration.valueDiscriminator
     */
    public var valueDiscriminator: String = CatCodeConfiguration.Default.valueDiscriminator
    
    /**
     * 当值为null时, 是否输出空值。
     * @see CatCodeConfiguration.encodeNullAsEmpty
     */
    public var encodeNullAsEmpty: Boolean = CatCodeConfiguration.Default.encodeNullAsEmpty
    
    
    /**
     * 反序列化时，当值为空且属性可为null，是否输出null。例如 `[CAT:user,username=]`，
     * 如果为 `true` 则反序列化结果的 `username` 为 `null` 而不是空字符串。
     *
     * @see CatCodeConfiguration.decodeEmptyAsNull
     */
    public var decodeEmptyAsNull: Boolean = CatCodeConfiguration.Default.decodeEmptyAsNull
    
    /**
     * 是否在对指定catcode反序列化时使用更宽松的格式校验。
     *
     * @see CatCodeConfiguration.isLenient
     */
    public var isLenient: Boolean = CatCodeConfiguration.Default.isLenient
    
    // endregion
    
}