package com.donderapps.friendscoreandy.domain.usecase

import com.donderapps.friendscoreandy.domain.model.FriendWithScore
import com.donderapps.friendscoreandy.domain.model.Trophy
import javax.inject.Inject

class GetTrophyRankingsUseCase @Inject constructor() {

    fun execute(friends: List<FriendWithScore>): Map<String, Trophy> {
        val ranked = friends
            .filter { it.score.allTime != null }
            .sortedByDescending { it.score.allTime }

        if (ranked.isEmpty()) return emptyMap()

        val trophies = mutableMapOf<String, Trophy>()
        val trophyValues = listOf(Trophy.GOLD, Trophy.SILVER, Trophy.BRONZE)
        var trophyIndex = 0
        var position = 0

        while (position < ranked.size && trophyIndex < trophyValues.size) {
            val currentScore = ranked[position].score.allTime
            while (position < ranked.size && ranked[position].score.allTime == currentScore) {
                trophies[ranked[position].friend.id] = trophyValues[trophyIndex]
                position++
            }
            trophyIndex = position.coerceAtMost(trophyValues.size)
        }

        return trophies
    }
}
