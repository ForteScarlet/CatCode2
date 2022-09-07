import java.time.LocalDate
import java.time.LocalTime

object Version {
    const val GROUP = "love.forte.catcode2"
    const val VERSION = "0.3.1"
    const val DESCRIPTION = "Cat Code, the spirit of CQ code continues, a cute universal special code."
    val currentTimeValue: String = currentTimeValue()
    
}

/**
 * yyyyMMdd.secondsOfDay
 */
private fun currentTimeValue(): String {
    val date = LocalDate.now()
    val secondOfDay = LocalTime.now().toSecondOfDay()
    return buildString {
        append(date.year)
        val monthValue = date.monthValue
        if (monthValue < 10) {
            append(0)
        }
        append(monthValue)
        val dayOfMonth = date.dayOfMonth
        if (dayOfMonth < 10) {
            append(0)
        }
        append(dayOfMonth)
        append('.')
        append(secondOfDay)
    }
}