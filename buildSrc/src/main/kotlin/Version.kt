object Version {
    const val GROUP = "love.forte.catcode2"
    const val VERSION = "0.1.3"
    const val DESCRIPTION = "Cat Code, the spirit of CQ code continues, a cute universal special code."
}


fun isRelease(): Boolean {
    if (System.getProperty("CATCODE2_RELEASE")?.toBoolean() == true) {
        return true
    }
    
    if (System.getenv("CATCODE2_RELEASE")?.toBoolean() == true) {
        return true
    }
    
    return false
}