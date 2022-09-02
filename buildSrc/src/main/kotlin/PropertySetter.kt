import org.gradle.api.provider.Property
import java.time.Duration
import java.util.concurrent.TimeUnit

infix fun <T> Property<T>.by(value: T) {
    set(value)
}


infix fun Property<Duration>.by(value: Long): DurationHolder = DurationHolder(this, value)

data class DurationHolder(private val property: Property<Duration>, private val time: Long) {
    infix fun unit(unit: TimeUnit) {
        if (unit == TimeUnit.SECONDS) {
            property.set(Duration.ofSeconds(time))
        } else {
            property.set(Duration.ofMillis(unit.toMillis(time)))
        }
    }
}