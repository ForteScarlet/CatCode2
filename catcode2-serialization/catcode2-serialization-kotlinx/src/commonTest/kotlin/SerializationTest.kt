import catcode2.serialization.CatCode
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import kotlin.test.Test
import kotlin.test.assertTrue

/**
 *
 * @author ForteScarlet
 */
class SerializationTest {
    
    /**
     * 普通的实体类序列化测试
     */
    @Test
    fun serializationTest() {
        val original = User("forte", Foo.Tar("HI"))
        val code = CatCode.encodeToString(User.serializer(), original)
        println(code)
        val user = CatCode.decodeFromString(User.serializer(), code)
        println(user)
        assertTrue { user == original }
    }
    
    @Test
    fun listSerializationTest() {
        val original = listOf(Foo.Bar(15), Foo.Tar("forte"))
        
        val code = CatCode.encodeToString(ListSerializer(Foo.serializer()), original)
    
        println(code)
        
        
    }
    
    
}

@Serializable
@SerialName("user")
private data class User(val username: String, val foo: Foo)


@Serializable
private sealed class Foo {
    @Serializable
    data class Bar(val age: Int = 1) : Foo()
    
    @Serializable
    data class Tar(val title: String) : Foo()
}