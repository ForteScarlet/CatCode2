package catcode2.cat

import kotlin.jvm.JvmInline
import kotlin.reflect.KProperty

/**
 * 用于通过 [CatDelegateProvider.getValue] 提供属性代理能力的 value class.
 *
 * ```kotlin
 * val cat: Cat = ...
 * val name: String? by cat.provider
 * ```
 *
 * [CatDelegateProvider] 是最基本的委托者，其提供 [String] 类型的属性委托。
 */
@JvmInline
public value class CatDelegateProvider(private val cat: Cat) {
    internal inline operator fun get(key: String): String? = cat[key]
}

// region non-null provider

/**
 * 获取当前 [Cat] 对应的属性代理器 [CatDelegateProvider]。
 *
 */
public inline val Cat.provider: CatDelegateProvider get() = CatDelegateProvider(this)

/**
 * 通过 `by` 来向 [Cat] 委托一个属性。
 *
 * ```kotlin
 * val cat: Cat = ...
 * val name: String by cat.provider
 * ```
 *
 * 当委托属性无法在目标 [Cat] 中被获取，则会抛出 [NoSuchElementException]。
 *
 */
public operator fun CatDelegateProvider.getValue(thisRef: Any?, property: KProperty<*>): String =
    get(property.name) ?: throw NoSuchElementException(property.name)


// region for primitive type

// region byte
/**
 * 将 [CatDelegateProvider.get] 的结果转化为 [Byte] 类型的委托者。
 *
 * ```kotlin
 * val value: Byte by cat.provider.byte
 * ```
 *
 * @throws NoSuchElementException 如果 [CatDelegateProvider.get] 得到的值为null。
 *
 */
@JvmInline
public value class CatDelegateByteProvider(@PublishedApi internal val provider: CatDelegateProvider)

/**
 * 将 [CatDelegateProvider] 转化为 [Byte] 目标。
 */
public inline val CatDelegateProvider.byte: CatDelegateByteProvider get() = CatDelegateByteProvider(this)


/**
 * 通过 `by` 来向 [Cat] 委托一个属性。
 *
 * ```kotlin
 * val cat: Cat = ...
 * val value: Byte by cat.provider.byte
 * ```
 *
 * 当委托属性无法在目标 [Cat] 中被获取，则会抛出 [NoSuchElementException]。
 *
 * 当委托属性无法转化为 [Byte]，则会抛出 [NumberFormatException]。
 *
 */
public operator fun CatDelegateByteProvider.getValue(thisRef: Any?, property: KProperty<*>): Byte =
    (provider[property.name] ?: throw NoSuchElementException(property.name)).toByte()
// endregion

// region short
/**
 * 将 [CatDelegateProvider.get] 的结果转化为 [Short] 类型的委托者。
 *
 * ```kotlin
 *  val value: Short by cat.provider.short
 * ```
 *
 * @throws NoSuchElementException 如果 [CatDelegateProvider.get] 得到的值为null。
 *
 */
@JvmInline
public value class CatDelegateShortProvider(@PublishedApi internal val provider: CatDelegateProvider)

/**
 * 将 [CatDelegateProvider] 转化为 [Short] 目标。
 */
public inline val CatDelegateProvider.short: CatDelegateShortProvider get() = CatDelegateShortProvider(this)


/**
 * 通过 `by` 来向 [Cat] 委托一个属性。
 *
 * ```kotlin
 * val cat: Cat = ...
 * val value: Short by cat.provider.short
 * ```
 *
 * 当委托属性无法在目标 [Cat] 中被获取，则会抛出 [NoSuchElementException]。
 *
 * 当委托属性无法转化为 [Short]，则会抛出 [NumberFormatException]。
 *
 */
public operator fun CatDelegateShortProvider.getValue(thisRef: Any?, property: KProperty<*>): Short =
    (provider[property.name] ?: throw NoSuchElementException(property.name)).toShort()
// endregion

// region int
/**
 * 将 [CatDelegateProvider.get] 的结果转化为 [Int] 类型的委托者。
 *
 * ```kotlin
 * val value: Int by cat.provider.int
 * ```
 *
 * @throws NoSuchElementException 如果 [CatDelegateProvider.get] 得到的值为null。
 *
 */
@JvmInline
public value class CatDelegateIntProvider(@PublishedApi internal val provider: CatDelegateProvider)

/**
 * 将 [CatDelegateProvider] 转化为 [Int] 目标。
 */
public inline val CatDelegateProvider.int: CatDelegateIntProvider get() = CatDelegateIntProvider(this)


/**
 * 通过 `by` 来向 [Cat] 委托一个属性。
 *
 * ```kotlin
 * val cat: Cat = ...
 * val value: Int by cat.provider.int
 * ```
 *
 * 当委托属性无法在目标 [Cat] 中被获取，则会抛出 [NoSuchElementException]。
 *
 * 当委托属性无法转化为 [Int]，则会抛出 [NumberFormatException]。
 *
 */
public operator fun CatDelegateIntProvider.getValue(thisRef: Any?, property: KProperty<*>): Int =
    (provider[property.name] ?: throw NoSuchElementException(property.name)).toInt()
// endregion

// region long
/**
 * 将 [CatDelegateProvider.get] 的结果转化为 [Long] 类型的委托者。
 *
 * ```kotlin
 * val value: Long by cat.provider.long
 * ```
 *
 * @throws NoSuchElementException 如果 [CatDelegateProvider.get] 得到的值为null。
 *
 */
@JvmInline
public value class CatDelegateLongProvider(@PublishedApi internal val provider: CatDelegateProvider)

/**
 * 将 [CatDelegateProvider] 转化为 [Long] 目标。
 */
public inline val CatDelegateProvider.long: CatDelegateLongProvider get() = CatDelegateLongProvider(this)


/**
 * 通过 `by` 来向 [Cat] 委托一个属性。
 *
 * ```kotlin
 * val cat: Cat = ...
 * val value: Long by cat.provider.long
 * ```
 *
 * 当委托属性无法在目标 [Cat] 中被获取，则会抛出 [NoSuchElementException]。
 *
 * 当委托属性无法转化为 [Long]，则会抛出 [NumberFormatException]。
 *
 */
public operator fun CatDelegateLongProvider.getValue(thisRef: Any?, property: KProperty<*>): Long =
    (provider[property.name] ?: throw NoSuchElementException(property.name)).toLong()
// endregion

// region double
/**
 * 将 [CatDelegateProvider.get] 的结果转化为 [Double] 类型的委托者。
 *
 * ```kotlin
 * val value: Double by cat.provider.double
 * ```
 *
 * @throws NoSuchElementException 如果 [CatDelegateProvider.get] 得到的值为null。
 *
 */
@JvmInline
public value class CatDelegateDoubleProvider(@PublishedApi internal val provider: CatDelegateProvider)

/**
 * 将 [CatDelegateProvider] 转化为 [Double] 目标。
 */
public inline val CatDelegateProvider.double: CatDelegateDoubleProvider get() = CatDelegateDoubleProvider(this)


/**
 * 通过 `by` 来向 [Cat] 委托一个属性。
 *
 * ```kotlin
 * val cat: Cat = ...
 * val value: Double by cat.provider.double
 * ```
 *
 * 当委托属性无法在目标 [Cat] 中被获取，则会抛出 [NoSuchElementException]。
 *
 * 当委托属性无法转化为 [Double]，则会抛出 [NumberFormatException]。
 *
 */
public operator fun CatDelegateDoubleProvider.getValue(thisRef: Any?, property: KProperty<*>): Double =
    (provider[property.name] ?: throw NoSuchElementException(property.name)).toDouble()
// endregion

// region float
/**
 * 将 [CatDelegateProvider.get] 的结果转化为 [Float] 类型的委托者。
 *
 * ```kotlin
 * val value: Float by cat.provider.float
 * ```
 *
 * @throws NoSuchElementException 如果 [CatDelegateProvider.get] 得到的值为null。
 * @throws
 *
 */
@JvmInline
public value class CatDelegateFloatProvider(@PublishedApi internal val provider: CatDelegateProvider)

/**
 * 将 [CatDelegateProvider] 转化为 [Float] 目标。
 */
public inline val CatDelegateProvider.float: CatDelegateFloatProvider get() = CatDelegateFloatProvider(this)


/**
 * 通过 `by` 来向 [Cat] 委托一个属性。
 *
 * ```kotlin
 * val cat: Cat = ...
 * val value: Float by cat.provider.float
 * ```
 *
 * 当委托属性无法在目标 [Cat] 中被获取，则会抛出 [NoSuchElementException]。
 *
 * 当委托属性无法转化为 [Float]，则会抛出 [NumberFormatException]。
 *
 */
public operator fun CatDelegateFloatProvider.getValue(thisRef: Any?, property: KProperty<*>): Float =
    (provider[property.name] ?: throw NoSuchElementException(property.name)).toFloat()
// endregion

// region boolean
/**
 * 将 [CatDelegateProvider.get] 的结果转化为 [Boolean] 类型的委托者。
 *
 * ```kotlin
 * val value: Boolean by cat.provider.boolean
 * ```
 *
 * @throws NoSuchElementException 如果 [CatDelegateProvider.get] 得到的值为null。
 * @throws
 *
 */
@JvmInline
public value class CatDelegateBooleanProvider(@PublishedApi internal val provider: CatDelegateProvider)

/**
 * 将 [CatDelegateProvider] 转化为 [Boolean] 目标。
 */
public inline val CatDelegateProvider.boolean: CatDelegateBooleanProvider get() = CatDelegateBooleanProvider(this)


/**
 * 通过 `by` 来向 [Cat] 委托一个属性。
 *
 * ```kotlin
 * val cat: Cat = ...
 * val value: Boolean by cat.provider.boolean
 * ```
 *
 * 当委托属性无法在目标 [Cat] 中被获取，则会抛出 [NoSuchElementException]。
 *
 * 当委托属性无法转化为 [Boolean]，则会抛出 [NumberFormatException]。
 *
 */
public operator fun CatDelegateBooleanProvider.getValue(thisRef: Any?, property: KProperty<*>): Boolean =
    provider[property.name].toBoolean()
// endregion


// endregion
// endregion


// region nullable provider

/**
 * 用于通过 [CatDelegateProvider.getValue] 提供属性代理能力的 value class.
 *
 * ```kotlin
 * val cat: Cat = ...
 * val name: String? by cat.provider
 * ```
 *
 * [CatDelegateProvider] 是最基本的委托者，其提供 [String?][String] 类型的属性委托。
 */
@JvmInline
public value class CatDelegateNullableProvider(internal val provider: CatDelegateProvider)


/**
 * 获取当前 [CatDelegateProvider] 对应的可控属性代理器 [CatDelegateProvider]。
 *
 */
public inline val CatDelegateProvider.nullable: CatDelegateNullableProvider get() = CatDelegateNullableProvider(this)

/**
 * 通过 `by` 来向 [Cat] 委托一个属性。
 *
 * ```kotlin
 * val cat: Cat = ...
 * val name: String? by cat.provider
 * ```
 */
public operator fun CatDelegateNullableProvider.getValue(thisRef: Any?, property: KProperty<*>): String? =
    provider[property.name]


// region for primitive type

// region byte
/**
 * 将 [CatDelegateProvider.get] 的结果转化为 [Byte?][Byte] 类型的委托者。
 *
 * ```kotlin
 * val value: Byte? by cat.provider.byte
 * ```
 *
 */
@JvmInline
public value class CatDelegateNullableByteProvider(internal val provider: CatDelegateProvider)

/**
 * 将 [CatDelegateByteProvider] 转化为 [Byte?][Byte] 目标。
 */
public inline val CatDelegateByteProvider.nullable: CatDelegateNullableByteProvider
    get() = CatDelegateNullableByteProvider(
        provider
    )


/**
 * 通过 `by` 来向 [Cat] 委托一个属性。
 *
 * ```kotlin
 * val cat: Cat = ...
 * val value: Byte? by cat.provider.byte.nullable
 * ```
 */
public operator fun CatDelegateNullableByteProvider.getValue(thisRef: Any?, property: KProperty<*>): Byte? =
    provider[property.name]?.toByteOrNull()
// endregion

// region short
/**
 * 将 [CatDelegateProvider.get] 的结果转化为 [Short?][Short] 类型的委托者。
 *
 * ```kotlin
 *  val value: Short? by cat.provider.short.nullable
 * ```
 *
 */
@JvmInline
public value class CatDelegateNullableShortProvider(internal val provider: CatDelegateProvider)

/**
 * 将 [CatDelegateShortProvider] 转化为 [Short] 目标。
 */
public inline val CatDelegateShortProvider.nullable: CatDelegateNullableShortProvider
    get() = CatDelegateNullableShortProvider(
        provider
    )


/**
 * 通过 `by` 来向 [Cat] 委托一个属性。
 *
 * ```kotlin
 * val cat: Cat = ...
 * val value: Short? by cat.provider.short.nullable
 * ```
 */
public operator fun CatDelegateNullableShortProvider.getValue(thisRef: Any?, property: KProperty<*>): Short? =
    provider[property.name]?.toShortOrNull()
// endregion

// region int
/**
 * 将 [CatDelegateProvider.get] 的结果转化为 [Int?][Int] 类型的委托者。
 *
 * ```kotlin
 * val value: Int? by cat.provider.int.nullable
 * ```
 *
 */
@JvmInline
public value class CatDelegateNullableIntProvider(internal val provider: CatDelegateProvider)

/**
 * 将 [CatDelegateIntProvider] 转化为 [Int?][Int] 目标。
 */
public inline val CatDelegateIntProvider.nullable: CatDelegateNullableIntProvider
    get() = CatDelegateNullableIntProvider(
        provider
    )


/**
 * 通过 `by` 来向 [Cat] 委托一个属性。
 *
 * ```kotlin
 * val cat: Cat = ...
 * val value: Int? by cat.provider.int.nullable
 * ```
 *
 */
public operator fun CatDelegateNullableIntProvider.getValue(thisRef: Any?, property: KProperty<*>): Int? =
    provider[property.name]?.toIntOrNull()
// endregion

// region long
/**
 * 将 [CatDelegateProvider.get] 的结果转化为 [[Long?]Long] 类型的委托者。
 *
 * ```kotlin
 * val value: Long? by cat.provider.long.nullable
 * ```
 */
@JvmInline
public value class CatDelegateNullableLongProvider(internal val provider: CatDelegateProvider)

/**
 * 将 [CatDelegateLongProvider] 转化为 [Long?][Long] 目标。
 */
public inline val CatDelegateLongProvider.nullable: CatDelegateNullableLongProvider
    get() = CatDelegateNullableLongProvider(
        provider
    )


/**
 * 通过 `by` 来向 [Cat] 委托一个属性。
 *
 * ```kotlin
 * val cat: Cat = ...
 * val value: Long? by cat.provider.long.nullable
 * ```
 */
public operator fun CatDelegateNullableLongProvider.getValue(thisRef: Any?, property: KProperty<*>): Long? =
    provider[property.name]?.toLongOrNull()
// endregion

// region double
/**
 * 将 [CatDelegateProvider.get] 的结果转化为 [Double?][Double] 类型的委托者。
 *
 * ```kotlin
 * val value: Double? by cat.provider.double.nullable
 * ```
 *
 */
@JvmInline
public value class CatDelegateNullableDoubleProvider(internal val provider: CatDelegateProvider)

/**
 * 将 [CatDelegateDoubleProvider] 转化为 [Double?][Double] 目标。
 */
public inline val CatDelegateDoubleProvider.nullable: CatDelegateNullableDoubleProvider
    get() = CatDelegateNullableDoubleProvider(
        provider
    )


/**
 * 通过 `by` 来向 [Cat] 委托一个属性。
 *
 * ```kotlin
 * val cat: Cat = ...
 * val value: Double? by cat.provider.double.nullable
 * ```
 *
 */
public operator fun CatDelegateNullableDoubleProvider.getValue(thisRef: Any?, property: KProperty<*>): Double? =
    provider[property.name]?.toDoubleOrNull()
// endregion

// region float
/**
 * 将 [CatDelegateProvider.get] 的结果转化为 [Float?][Float] 类型的委托者。
 *
 * ```kotlin
 * val value: Float? by cat.provider.float.nullable
 * ```
 *
 */
@JvmInline
public value class CatDelegateNullableFloatProvider(internal val provider: CatDelegateProvider)

/**
 * 将 [CatDelegateFloatProvider] 转化为 [Float?][Float] 目标。
 */
public inline val CatDelegateFloatProvider.nullable: CatDelegateNullableFloatProvider
    get() = CatDelegateNullableFloatProvider(
        provider
    )


/**
 * 通过 `by` 来向 [Cat] 委托一个属性。
 *
 * ```kotlin
 * val cat: Cat = ...
 * val value: Float? by cat.provider.float.nullable
 * ```
 *
 */
public operator fun CatDelegateNullableFloatProvider.getValue(thisRef: Any?, property: KProperty<*>): Float? =
    provider[property.name]?.toFloatOrNull()
// endregion

// region boolean strict
/**
 * 将 [CatDelegateProvider.get] 的结果转化为 [Boolean][Boolean] 类型的委托者。
 *
 * ```kotlin
 * val value: Boolean by cat.provider.boolean.strict
 * ```
 */
@JvmInline
public value class CatDelegateStrictBooleanProvider(@PublishedApi internal val provider: CatDelegateProvider)

/**
 * 将 [CatDelegateBooleanProvider] 转化为 [Boolean][Boolean] 目标。
 */
public inline val CatDelegateBooleanProvider.strict: CatDelegateStrictBooleanProvider
    get() = CatDelegateStrictBooleanProvider(
        provider
    )


/**
 * 通过 `by` 来向 [Cat] 委托一个属性。
 *
 * ```kotlin
 * val cat: Cat = ...
 * val value: Boolean by cat.provider.boolean.strict
 * ```
 *
 * 当委托属性无法在目标 [Cat] 中被获取，则会抛出 [NoSuchElementException]。
 *
 * 当委托属性无法转化为 [Boolean]，则会抛出 [NumberFormatException]。
 *
 */
public operator fun CatDelegateStrictBooleanProvider.getValue(thisRef: Any?, property: KProperty<*>): Boolean =
    provider[property.name]?.toBooleanStrict() ?: throw NoSuchElementException(property.name)
// endregion


// region boolean
/**
 * 将 [CatDelegateProvider.get] 的结果转化为 [Boolean?][Boolean] 类型的委托者。
 *
 * ```kotlin
 * val value: Boolean? by cat.provider.boolean.nullable
 * // or
 * val value: Boolean? by cat.provider.boolean.strict.nullable
 * ```
 */
@JvmInline
public value class CatDelegateNullableBooleanProvider(internal val provider: CatDelegateProvider)

/**
 * 将 [CatDelegateBooleanProvider] 转化为 [Boolean?][Boolean] 目标。
 */
public inline val CatDelegateBooleanProvider.nullable: CatDelegateNullableBooleanProvider
    get() = CatDelegateNullableBooleanProvider(provider)

/**
 * 将 [CatDelegateStrictBooleanProvider] 转化为 [Boolean?][Boolean] 目标。
 */
public inline val CatDelegateStrictBooleanProvider.nullable: CatDelegateNullableBooleanProvider
    get() = CatDelegateNullableBooleanProvider(provider)


/**
 * 通过 `by` 来向 [Cat] 委托一个属性。
 *
 * ```kotlin
 * val cat: Cat = ...
 * val value: Boolean? by cat.provider.boolean.nullable
 * // or
 * val value: Boolean? by cat.provider.boolean.strict.nullable
 * ```
 */
public operator fun CatDelegateNullableBooleanProvider.getValue(thisRef: Any?, property: KProperty<*>): Boolean? =
    provider[property.name]?.toBooleanStrictOrNull()
// endregion

// endregion
// endregion

