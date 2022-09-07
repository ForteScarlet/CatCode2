package catcode2

import catcode2.cat.*
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue


private const val catNum = 12345
private const val catFloat = 55.66
private const val catBool = true

/**
 *
 * @author ForteScarlet
 */
class CatProviderTest {
    private val cat = catOf("[CAT:provider,number=$catNum,float=$catFloat,bool=$catBool]")
    
    @Test
    fun stringProviderTest() {
        val number by cat.provider
        val float by cat.provider
        val bool by cat.provider
        
        assertTrue { number == catNum.toString() }
        assertTrue { float == catFloat.toString() }
        assertTrue { bool == catBool.toString() }
    }
    
    @Test
    fun intProviderTest() {
        run {
            val number by cat.provider.short
            assertTrue { number == catNum.toShort() }
        }
        run {
            val number by cat.provider.int
            assertTrue { number == catNum }
        }
        run {
            val number by cat.provider.long
            assertTrue { number == catNum.toLong() }
        }
    }
    
    
    @Test
    fun floatProviderTest() {
        run {
            val float by cat.provider.double
            assertTrue { float == catFloat }
        }
        run {
            val float by cat.provider.float
            assertTrue { float == catFloat.toFloat() }
        }
    }
    
    
    @Test
    fun boolProviderTest() {
        run {
            val bool by cat.provider.boolean
            assertTrue { bool == catBool }
        }
        run {
            val bool by cat.provider.boolean.strict
            assertTrue { bool == catBool }
        }
    }
    
    @Test
    fun nullableProviderTest() {
        val a by cat.provider.nullable
        assertNull(a)

        val b by cat.provider.int.nullable
        assertNull(b)
        
        val c by cat.provider.boolean
        assertFalse(c)
        
        val d1 by cat.provider.boolean.nullable
        assertNull(d1)
        
        val d2 by cat.provider.boolean.strict.nullable
        assertNull(d2)
        
        val e by cat.provider.double.nullable
        assertNull(e)
        
        val f by cat.provider.float.nullable
        assertNull(f)
    }
    
}