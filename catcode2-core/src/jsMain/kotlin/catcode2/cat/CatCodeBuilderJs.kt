@file:JsExport

package catcode2.cat

import catcode2.CAT_HEAD


/**
 * 得到一个使用 [BaseCatCodeBuilder] 作为类型的 [CatCodeBuilder]。
 */
public fun newCatCodeBuilder(
    type: String,
    head: String = CAT_HEAD,
): BaseCatCodeBuilder<Cat, BaseCatCodeBuilder<Cat, *>> {
    return CatCodeBuilder.of(type, head)
}


/**
 * 得到一个使用 [BaseCatCodeBuilder] 作为类型的 [CatCodeLiteralBuilder]。
 */
public fun newCatCodeLiteralBuilder(
    type: String,
    head: String = CAT_HEAD,
): BaseCatCodeBuilder<String, BaseCatCodeBuilder<String, *>> {
    return CatCodeLiteralBuilder.of(type, head)
}

