package catcode2


public actual object CatEscalator {
    /**
     * 根据指定字符，得到它应当被转义为的结果。如果无需转义则得到null。
     */
    public actual fun getTextEncode(value: Char): String? = CatEscalatorInternalBaseImpl.getTextEncode(value)
    
    /**
     * 根据指定字符串，得到它转义前的结果。如果无需转义则得到null。
     */
    public actual fun getTextDecode(value: String): Char? = CatEscalatorInternalBaseImpl.getTextDecode(value)
    
    /**
     * 根据指定字符，得到它应当被转义为的结果。如果无需转义则得到null。
     */
    public actual fun getParamEncode(value: Char): String? = CatEscalatorInternalBaseImpl.getParamEncode(value)
    
    /**
     * 根据指定字符串，得到它转义前的结果。如果无需转义则得到null。
     */
    public actual fun getParamDecode(value: String): Char? = CatEscalatorInternalBaseImpl.getParamDecode(value)
    
    /**
     * 将 [text] 根据转义标准进行转义，例如将 `&` 被转义为 `&amp;`。
     */
    public actual fun encodeText(text: String): String = CatEscalatorInternalBaseImpl.encodeText(text)
    
    /**
     * 将 [text] 根据转义标准进行转义，例如将 `&` 被转义为 `&amp;`。
     */
    public actual fun encodeParam(text: String): String = CatEscalatorInternalBaseImpl.encodeParam(text)
    
    /**
     * 将 [text] 转为转义前的内容, 例如将 `&amp;` 转为 `&`。
     */
    public actual fun decodeText(text: String): String = CatEscalatorInternalBaseImpl.decodeText(text)
    
    /**
     * 将 [text] 转为转义前的内容, 例如将 `&amp;` 转为 `&`。
     */
    public actual fun decodeParam(text: String): String = CatEscalatorInternalBaseImpl.decodeParam(text)
    
}

