package com.donderapps.friendscoreandy.domain.usecase

import com.donderapps.friendscoreandy.data.repository.EventRepository
import com.donderapps.friendscoreandy.data.repository.FriendRepository
import com.donderapps.friendscoreandy.domain.model.Friend
import com.donderapps.friendscoreandy.domain.model.FriendWithScore
import com.donderapps.friendscoreandy.domain.model.Score
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class GetFriendsWithScoresUseCase @Inject constructor(
    private val friendRepository: FriendRepository,
    private val eventRepository: EventRepository,
    private val getWeekBoundsUseCase: GetWeekBoundsUseCase
) {

    @OptIn(ExperimentalCoroutinesApi::class)
    fun execute(): Flow<List<FriendWithScore>> {
        return friendRepository.getAll().flatMapLatest { friends ->
            if (friends.isEmpty()) return@flatMapLatest flowOf(emptyList())

            val friendFlows = friends.map { friend -> buildFriendWithScore(friend) }
            combine(friendFlows) { it.toList() }
        }
    }

    private fun buildFriendWithScore(friend: Friend): Flow<FriendWithScore> {
        val (weekStart, weekEnd) = getWeekBoundsUseCase.execute()
        return combine(
            friendRepository.getAllTimeScore(friend.id),
            friendRepository.getWeeklyScore(friend.id, weekStart, weekEnd),
            eventRepository.getLatestByFriendId(friend.id),
            friendRepository.getTagsForFriend(friend.id)
        ) { allTime, weekly, lastEvent, tags ->
            FriendWithScore(
                friend = friend.copy(tags = tags),
                score = Score(allTime = allTime, weekly = weekly),
                lastEvent = lastEvent,
                trophy = null
            )
        }
    }
}
