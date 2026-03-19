package com.donderapps.friendscoreandy.domain.usecase

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.util.Calendar
import java.util.TimeZone

class GetWeekBoundsUseCaseTest {

    private lateinit var useCase: GetWeekBoundsUseCase

    @Before
    fun setup() {
        useCase = GetWeekBoundsUseCase()
    }

    @Test
    fun `monday returns same monday as start`() {
        val monday = calendarFor(2026, Calendar.MARCH, 16, 10, 30) // Monday
        val (start, _) = useCase.execute(monday.timeInMillis)

        val startCal = Calendar.getInstance().apply { timeInMillis = start }
        assertEquals(Calendar.MONDAY, startCal.get(Calendar.DAY_OF_WEEK))
        assertEquals(0, startCal.get(Calendar.HOUR_OF_DAY))
        assertEquals(0, startCal.get(Calendar.MINUTE))
        assertEquals(0, startCal.get(Calendar.SECOND))
        assertEquals(0, startCal.get(Calendar.MILLISECOND))
    }

    @Test
    fun `sunday returns previous monday as start`() {
        val sunday = calendarFor(2026, Calendar.MARCH, 22, 23, 59) // Sunday
        val (start, _) = useCase.execute(sunday.timeInMillis)

        val startCal = Calendar.getInstance().apply { timeInMillis = start }
        assertEquals(Calendar.MONDAY, startCal.get(Calendar.DAY_OF_WEEK))
        assertEquals(16, startCal.get(Calendar.DAY_OF_MONTH))
    }

    @Test
    fun `mid week returns correct monday`() {
        val wednesday = calendarFor(2026, Calendar.MARCH, 18, 14, 0) // Wednesday
        val (start, _) = useCase.execute(wednesday.timeInMillis)

        val startCal = Calendar.getInstance().apply { timeInMillis = start }
        assertEquals(Calendar.MONDAY, startCal.get(Calendar.DAY_OF_WEEK))
        assertEquals(16, startCal.get(Calendar.DAY_OF_MONTH))
    }

    @Test
    fun `week end is exactly 7 days after start`() {
        val wednesday = calendarFor(2026, Calendar.MARCH, 18, 14, 0)
        val (start, end) = useCase.execute(wednesday.timeInMillis)

        val diffDays = (end - start) / (24 * 60 * 60 * 1000)
        assertEquals(7, diffDays)
    }

    @Test
    fun `start is always before end`() {
        val now = System.currentTimeMillis()
        val (start, end) = useCase.execute(now)
        assertTrue(start < end)
    }

    @Test
    fun `week boundary at midnight monday`() {
        val mondayMidnight = calendarFor(2026, Calendar.MARCH, 16, 0, 0)
        val (start, _) = useCase.execute(mondayMidnight.timeInMillis)

        assertEquals(mondayMidnight.timeInMillis, start)
    }

    private fun calendarFor(year: Int, month: Int, day: Int, hour: Int, minute: Int): Calendar {
        return Calendar.getInstance(TimeZone.getDefault()).apply {
            set(year, month, day, hour, minute, 0)
            set(Calendar.MILLISECOND, 0)
        }
    }
}
