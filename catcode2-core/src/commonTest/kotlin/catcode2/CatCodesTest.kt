package catcode2

import kotlin.test.Test
import kotlin.test.assertTrue


/**
 *
 * @author ForteScarlet
 */
class CatCodesTest {
    
    @Test
    fun walkTest() {
        
        val pairs1 = mutableMapOf<String, String>()
        walkCatCode(requireCatCode("[CAT:atall]"),
            perceiveHead = { assertTrue { it == "CAT" } },
            perceiveType = { assertTrue { it == "atall" } }
        ) { k, v ->
            pairs1[k] = v
        }
        assertTrue { pairs1.isEmpty() }
        
        
        val pairs2 = mutableMapOf<String, String>()
        walkCatCode(
            requireCatCode("[CQ:at,code=123,name=forte]"),
            perceiveHead = { assertTrue { it == "CQ" } },
            perceiveType = { assertTrue { it == "at" } }
        ) { k, v ->
            pairs2[k] = v
        }
        
        assertTrue { pairs2.size == 2 }
        assertTrue { pairs2["code"] == "123" }
        assertTrue { pairs2["name"] == "forte" }
        
        val pairs3 = mutableMapOf<String, String>()
        walkCatCode(requireCatCode("[OB:at_user,code=123]"),
            perceiveHead = { assertTrue { it == "OB" } },
            perceiveType = { assertTrue { it == "at_user" } }
        ) { k, v ->
            pairs3[k] = v
        }
        
        assertTrue { pairs3.size == 1 }
        assertTrue { pairs3["code"] == "123" }
    }
    
    
    @Test
    fun catCodeWalkTest() {
        val code = "[CAT:foo,tar=bar]"
        
        lateinit var head: String
        
        walkCatCode(code, true,
            perceiveHead = {
                head = it
                return@walkCatCode
            }
        ) { k, v ->
            error("no here.")
        }
    
        println("head: $head")
        
    }
    
}