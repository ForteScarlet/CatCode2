fun prop(name: String): String? {
    return System.getProperty(name, System.getenv(name))
}

data class SonatypeUserInfo(val sonatypeUsername: String, val sonatypePassword: String)

private val _sonatypeUserInfo: SonatypeUserInfo? by lazy {
    val sonatypeUsername: String? = prop("OSSRH_USER")
    val sonatypePassword: String? = prop("OSSRH_PASSWORD")
    
    if (sonatypeUsername != null && sonatypePassword != null) {
        SonatypeUserInfo(sonatypeUsername, sonatypePassword)
    } else {
        null
    }
}

public val sonatypeUserInfo: SonatypeUserInfo get() = _sonatypeUserInfo!!
public val sonatypeUserInfoOrNull: SonatypeUserInfo? get() = _sonatypeUserInfo


fun isPublishConfigurable(): Boolean {
    return sonatypeUserInfoOrNull != null
}