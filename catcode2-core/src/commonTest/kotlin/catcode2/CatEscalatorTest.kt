package catcode2

import kotlin.test.Test
import kotlin.test.assertTrue


/**
 *
 * @author ForteScarlet
 */
class CatEscalatorTest {

    @Test
    fun textEncodeTest() {
        fun String.assertEscalate() {
            val text = this
            val encoded = CatEscalator.encodeText(this)
            val decoded = CatEscalator.decodeText(encoded)
            assertTrue("text($text) encoded($encoded) decoded($decoded)") { text == decoded }
        }
        
        "前缀是'['后缀是']', 还有以及一个特殊的&".assertEscalate()
        "[前缀是'['后缀是']', 还有以及一个特殊的&".assertEscalate()
        "前缀是'['后缀是']', 还有以及一个特殊的".assertEscalate()
        
        "&前缀是'['后缀是']', 还有以及一个特殊的".assertEscalate()
        "&前缀是'['后缀是']', 还有以及一个特殊的]".assertEscalate()
    }
    

}