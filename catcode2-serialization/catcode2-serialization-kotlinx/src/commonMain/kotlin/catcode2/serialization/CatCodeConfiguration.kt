package catcode2.serialization

import catcode2.CAT_HEAD


/**
 *
 * @author ForteScarlet
 */
public class CatCodeConfiguration internal constructor(
    public val catHead: String = CAT_HEAD,
    public val encodeDefaults: Boolean = false,
    public val ignoreUnknownKeys: Boolean = false,
    public val isLenient: Boolean = false,
    public val classDiscriminator: String = "type",
    public val explicitNulls: Boolean = true,
) {

}