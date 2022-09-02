/**
 * CI相关常量
 */
object CiConstant {
    const val RELEASE = "CATCODE2_RELEASE"
    const val CI = "CATCODE2_CI"
    const val GPG_KEY_ID = "GPG_KEY_ID"
    const val GPG_SECRET_KEY = "GPG_SECRET_KEY"
    const val GPG_PASSWORD = "GPG_PASSWORD"
}


/**
 * 是否发布 release
 */
fun isRelease(): Boolean = prop(CiConstant.RELEASE).toBoolean()

/**
 * 是否在CI中
 */
fun isCi(): Boolean = prop(CiConstant.CI).toBoolean()