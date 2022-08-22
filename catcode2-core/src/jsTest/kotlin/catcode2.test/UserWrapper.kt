package catcode2.test

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.asPromise
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.js.Promise


/**
 *
 * @author ForteScarlet
 */
actual interface UserWrapper {
    actual suspend fun user(): User
    fun getUser(): Promise<User>
}


class UserWrapperImpl(override val coroutineContext: CoroutineContext = EmptyCoroutineContext) : UserWrapper,
    CoroutineScope {
    override suspend fun user(): User {
        delay(1000)
        return User("forte-js")
    }
    
    override fun getUser(): Promise<User> = async { user() }.asPromise()
}

actual val wrapper: UserWrapper get() = UserWrapperImpl()