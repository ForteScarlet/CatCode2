package catcode

import kotlinx.serialization.Serializable
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue


/**
 *
 * @author ForteScarlet
 */
class CatTest {
    @Suppress("USELESS_IS_CHECK")
    @Test
    fun implTest() {
        assertTrue { Bar() is Foo }
        assertTrue { Tar() is Foo }
        
        fun isNotBar(foo: Foo): Boolean = foo !is Bar
        fun isBar(foo: Foo): Boolean = foo is Bar
        
        assertTrue { isNotBar(Tar()) }
        assertFalse { isBar(Tar()) }
        
    }
    
    @Serializable
    private data class Hei(val name: String)
 
    interface Foo
    
    class Bar : Foo
    class Tar : Foo
    
}