package catcode2.test

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.future.asCompletableFuture
import java.util.concurrent.Future
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

actual interface UserWrapper {
    actual suspend fun user(): User
    fun getUser(): Future<User>
}

class UserWrapperImpl(override val coroutineContext: CoroutineContext = EmptyCoroutineContext): UserWrapper, CoroutineScope {
    override suspend fun user(): User {
        delay(1000)
        return User("forte-jvm")
    }
    
    override fun getUser(): Future<User> = async { user() }.asCompletableFuture()
}

actual val wrapper: UserWrapper get() = UserWrapperImpl()
