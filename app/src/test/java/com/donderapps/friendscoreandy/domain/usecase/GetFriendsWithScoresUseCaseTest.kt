package com.donderapps.friendscoreandy.domain.usecase

import app.cash.turbine.test
import com.donderapps.friendscoreandy.data.repository.EventRepository
import com.donderapps.friendscoreandy.data.repository.FriendRepository
import com.donderapps.friendscoreandy.domain.model.Event
import com.donderapps.friendscoreandy.domain.model.Friend
import com.donderapps.friendscoreandy.domain.model.Tag
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito

class GetFriendsWithScoresUseCaseTest {

    private lateinit var friendRepository: FriendRepository
    private lateinit var eventRepository: EventRepository
    private lateinit var getWeekBoundsUseCase: GetWeekBoundsUseCase
    private lateinit var useCase: GetFriendsWithScoresUseCase

    private val weekStart = 1710720000000L // arbitrary fixed week
    private val weekEnd = weekStart + 7 * 24 * 60 * 60 * 1000

    @Before
    fun setup() {
        friendRepository = Mockito.mock(FriendRepository::class.java)
        eventRepository = Mockito.mock(EventRepository::class.java)
        getWeekBoundsUseCase = Mockito.mock(GetWeekBoundsUseCase::class.java)

        Mockito.`when`(getWeekBoundsUseCase.execute(Mockito.anyLong()))
            .thenReturn(weekStart to weekEnd)
        Mockito.`when`(getWeekBoundsUseCase.execute())
            .thenReturn(weekStart to weekEnd)

        useCase = GetFriendsWithScoresUseCase(
            friendRepository,
            eventRepository,
            getWeekBoundsUseCase
        )
    }

    @Test
    fun `empty friends list emits empty list`() = runTest {
        Mockito.`when`(friendRepository.getAll()).thenReturn(flowOf(emptyList()))

        useCase.execute().test {
            assertEquals(emptyList<Any>(), awaitItem())
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `correct scores attached to friend`() = runTest {
        val friend = testFriend("1")
        val event = testEvent("e1", "1")

        Mockito.`when`(friendRepository.getAll()).thenReturn(flowOf(listOf(friend)))
        Mockito.`when`(friendRepository.getAllTimeScore("1")).thenReturn(flowOf(4.5f))
        Mockito.`when`(friendRepository.getWeeklyScore("1", weekStart, weekEnd)).thenReturn(flowOf(3.0f))
        Mockito.`when`(eventRepository.getLatestByFriendId("1")).thenReturn(flowOf(event))
        Mockito.`when`(friendRepository.getTagsForFriend("1")).thenReturn(flowOf(emptyList()))

        useCase.execute().test {
            val result = awaitItem()
            assertEquals(1, result.size)
            assertEquals(4.5f, result[0].score.allTime)
            assertEquals(3.0f, result[0].score.weekly)
            assertEquals(event, result[0].lastEvent)
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `null weekly when no events this week`() = runTest {
        val friend = testFriend("1")

        Mockito.`when`(friendRepository.getAll()).thenReturn(flowOf(listOf(friend)))
        Mockito.`when`(friendRepository.getAllTimeScore("1")).thenReturn(flowOf(4.0f))
        Mockito.`when`(friendRepository.getWeeklyScore("1", weekStart, weekEnd)).thenReturn(flowOf(null))
        Mockito.`when`(eventRepository.getLatestByFriendId("1")).thenReturn(flowOf(null))
        Mockito.`when`(friendRepository.getTagsForFriend("1")).thenReturn(flowOf(emptyList()))

        useCase.execute().test {
            val result = awaitItem()
            assertEquals(4.0f, result[0].score.allTime)
            assertNull(result[0].score.weekly)
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `tags attached to friend`() = runTest {
        val friend = testFriend("1")
        val tags = listOf(Tag("t1", "work"), Tag("t2", "friend"))

        Mockito.`when`(friendRepository.getAll()).thenReturn(flowOf(listOf(friend)))
        Mockito.`when`(friendRepository.getAllTimeScore("1")).thenReturn(flowOf(3.0f))
        Mockito.`when`(friendRepository.getWeeklyScore("1", weekStart, weekEnd)).thenReturn(flowOf(null))
        Mockito.`when`(eventRepository.getLatestByFriendId("1")).thenReturn(flowOf(null))
        Mockito.`when`(friendRepository.getTagsForFriend("1")).thenReturn(flowOf(tags))

        useCase.execute().test {
            val result = awaitItem()
            assertEquals(2, result[0].friend.tags.size)
            assertEquals("work", result[0].friend.tags[0].name)
            cancelAndConsumeRemainingEvents()
        }
    }

    private fun testFriend(id: String): Friend {
        return Friend(
            id = id,
            name = "Friend $id",
            photoUri = null,
            description = null,
            createdAt = 0L,
            tags = emptyList()
        )
    }

    private fun testEvent(id: String, friendId: String): Event {
        return Event(
            id = id,
            friendId = friendId,
            rating = 4,
            description = "test event",
            createdAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis()
        )
    }
}
