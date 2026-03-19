package com.donderapps.friendscoreandy.domain.usecase

import java.util.Calendar
import java.util.TimeZone
import javax.inject.Inject

class GetWeekBoundsUseCase @Inject constructor() {

    fun execute(currentTimeMillis: Long = System.currentTimeMillis()): Pair<Long, Long> {
        val calendar = Calendar.getInstance(TimeZone.getDefault()).apply {
            timeInMillis = currentTimeMillis
            firstDayOfWeek = Calendar.MONDAY
            set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        val weekStart = calendar.timeInMillis

        calendar.add(Calendar.DAY_OF_WEEK, 7)
        val weekEnd = calendar.timeInMillis

        return weekStart to weekEnd
    }
}
