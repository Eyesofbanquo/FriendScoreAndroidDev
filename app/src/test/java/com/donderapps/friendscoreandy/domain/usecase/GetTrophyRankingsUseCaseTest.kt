package com.donderapps.friendscoreandy.domain.usecase

import com.donderapps.friendscoreandy.domain.model.Friend
import com.donderapps.friendscoreandy.domain.model.FriendWithScore
import com.donderapps.friendscoreandy.domain.model.Score
import com.donderapps.friendscoreandy.domain.model.Trophy
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class GetTrophyRankingsUseCaseTest {

    private lateinit var useCase: GetTrophyRankingsUseCase

    @Before
    fun setup() {
        useCase = GetTrophyRankingsUseCase()
    }

    @Test
    fun `standard top 3 get gold silver bronze`() {
        val friends = listOf(
            friendWithScore("a", 5.0f),
            friendWithScore("b", 4.0f),
            friendWithScore("c", 3.0f),
            friendWithScore("d", 2.0f)
        )
        val result = useCase.execute(friends)

        assertEquals(Trophy.GOLD, result["a"])
        assertEquals(Trophy.SILVER, result["b"])
        assertEquals(Trophy.BRONZE, result["c"])
        assertEquals(null, result["d"])
    }

    @Test
    fun `two tied for 1st both get gold, next gets bronze`() {
        val friends = listOf(
            friendWithScore("a", 5.0f),
            friendWithScore("b", 5.0f),
            friendWithScore("c", 3.0f),
            friendWithScore("d", 2.0f)
        )
        val result = useCase.execute(friends)

        assertEquals(Trophy.GOLD, result["a"])
        assertEquals(Trophy.GOLD, result["b"])
        assertEquals(Trophy.BRONZE, result["c"])
        assertEquals(null, result["d"])
    }

    @Test
    fun `three tied for 1st all get gold, no silver or bronze`() {
        val friends = listOf(
            friendWithScore("a", 5.0f),
            friendWithScore("b", 5.0f),
            friendWithScore("c", 5.0f),
            friendWithScore("d", 2.0f)
        )
        val result = useCase.execute(friends)

        assertEquals(Trophy.GOLD, result["a"])
        assertEquals(Trophy.GOLD, result["b"])
        assertEquals(Trophy.GOLD, result["c"])
        assertEquals(null, result["d"])
    }

    @Test
    fun `two tied for 2nd both get silver`() {
        val friends = listOf(
            friendWithScore("a", 5.0f),
            friendWithScore("b", 4.0f),
            friendWithScore("c", 4.0f),
            friendWithScore("d", 2.0f)
        )
        val result = useCase.execute(friends)

        assertEquals(Trophy.GOLD, result["a"])
        assertEquals(Trophy.SILVER, result["b"])
        assertEquals(Trophy.SILVER, result["c"])
        assertEquals(null, result["d"])
    }

    @Test
    fun `fewer than 3 friends only assigns available trophies`() {
        val friends = listOf(
            friendWithScore("a", 5.0f),
            friendWithScore("b", 3.0f)
        )
        val result = useCase.execute(friends)

        assertEquals(Trophy.GOLD, result["a"])
        assertEquals(Trophy.SILVER, result["b"])
        assertEquals(2, result.size)
    }

    @Test
    fun `single friend gets gold`() {
        val friends = listOf(friendWithScore("a", 4.0f))
        val result = useCase.execute(friends)

        assertEquals(Trophy.GOLD, result["a"])
        assertEquals(1, result.size)
    }

    @Test
    fun `all same score all get gold`() {
        val friends = listOf(
            friendWithScore("a", 3.0f),
            friendWithScore("b", 3.0f),
            friendWithScore("c", 3.0f)
        )
        val result = useCase.execute(friends)

        assertEquals(Trophy.GOLD, result["a"])
        assertEquals(Trophy.GOLD, result["b"])
        assertEquals(Trophy.GOLD, result["c"])
    }

    @Test
    fun `empty list returns empty map`() {
        val result = useCase.execute(emptyList())
        assertTrue(result.isEmpty())
    }

    @Test
    fun `null scores excluded from ranking`() {
        val friends = listOf(
            friendWithScore("a", 5.0f),
            friendWithScore("b", null),
            friendWithScore("c", 3.0f)
        )
        val result = useCase.execute(friends)

        assertEquals(Trophy.GOLD, result["a"])
        assertEquals(Trophy.SILVER, result["c"])
        assertEquals(null, result["b"])
    }

    private fun friendWithScore(id: String, allTime: Float?): FriendWithScore {
        return FriendWithScore(
            friend = Friend(
                id = id,
                name = "Friend $id",
                photoUri = null,
                description = null,
                createdAt = 0L,
                tags = emptyList()
            ),
            score = Score(allTime = allTime, weekly = null),
            lastEvent = null,
            trophy = null
        )
    }
}
