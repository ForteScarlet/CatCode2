object Version {
    const val GROUP = "love.forte.catcode2"
    const val VERSION = "0.1.0"
    const val DESCRIPTION = "Cat code, the spirit of CQ code continues, a cute universal special code."
}


fun isRelease(): Boolean {
    if (System.getProperty("catcode2.release")?.toBoolean() == true) {
        return true
    }
    
    if (System.getenv("catcode2.release")?.toBoolean() == true) {
        return true
    }
    
    return false
}