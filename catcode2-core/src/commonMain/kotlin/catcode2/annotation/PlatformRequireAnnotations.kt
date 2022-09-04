package catcode2.annotation

/**
 * 标记一个目标，代表此目标为供于 JavaScript 平台用户而使用的内容，而不应被其他平台使用。
 */
@RequiresOptIn(level = RequiresOptIn.Level.WARNING)
@Retention(AnnotationRetention.BINARY)
public annotation class Api4Js

/**
 * 标记一个目标，代表此目标为供于 Jvm 平台用户而使用的内容（通常代表为Java），而不应被其他平台使用。
 */
@RequiresOptIn(level = RequiresOptIn.Level.WARNING)
@Retention(AnnotationRetention.BINARY)
public annotation class Api4Jvm
