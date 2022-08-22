package catcode2.test

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

expect interface UserWrapper {
    suspend fun user(): User
}

expect val wrapper: UserWrapper

data class User(val name: String)

class CoroutineTest {
    
    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun test() = runTest {
        println(wrapper.user())
    }
    
}