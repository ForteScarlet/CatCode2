package catcode

// import kotlinx.coroutines.CoroutineScope
// import kotlinx.coroutines.async
// import kotlinx.coroutines.delay
// import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

// expect interface Expect<T> {
//     suspend fun await()
// }

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
    private data class Hei(val name: String) {
        // private val scope = CoroutineScope(EmptyCoroutineContext)
        suspend fun hello(): Int {
            // delay(123)
            return 1
        }
        
        suspend fun getHello() {
            println(hello())
        }
        
        // fun getHello2() = scope.async { hello() }
        // fun getHello3() = scope.launch { hello() }
    }
    
    interface Foo
    
    class Bar : Foo
    class Tar : Foo
    
}