package catcode2

import catcode2.cat.buildCat
import catcode2.cat.buildCatLiteral
import kotlin.test.Test
import kotlin.test.assertTrue


/**
 *
 * @author ForteScarlet
 */
class CatBuildTest {
    
    @Test
    fun catBuilderTest() {
        val cat1 = buildCat("at") {
            "code" - "123"
            "display" - "@forte"
            "name" - "forliy"
        }
        
        assertTrue { cat1["code"] == "123" }
        assertTrue { cat1["display"] == "@forte" }
        assertTrue { cat1["name"] == "forliy" }
    
        println(cat1.toString())
        println(cat1.toString("CQ"))
        
        val cat2 = buildCat("at") {
            "display" - "@forte"
            "name" - "forliy"
            "code" - "123"
        }
        
        assertTrue { cat1 == cat2 }
        
        val cat3 = buildCat("at") {
            key("display") value "@forte"
            key("name") value "forliy"
            key("code") value "123"
        }
        
        assertTrue { cat1 == cat3 }
        assertTrue { cat2 == cat3 }
    }
    
    
    @Test
    fun catLiteralBuilderTest() {
        val catLiteral1 = buildCatLiteral("at") {
            "code" - "123"
            "display" - "@forte&"
            "name" - "forliy"
        }
        
        assertTrue { catLiteral1.startsWith("[CAT:at,") }
        assertTrue { CatEscalator.getParamEncode('&')!! in catLiteral1 }
        
        val catLiteral2 = buildCatLiteral("at") {
            key("code") value "123"
            key("display") value "@forte&"
            key("name") value "forliy"
        }
        
        assertTrue { catLiteral1 == catLiteral2 }
    }
    
}