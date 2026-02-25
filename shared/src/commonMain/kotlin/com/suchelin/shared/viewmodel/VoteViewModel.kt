package com.suchelin.shared.viewmodel

import com.suchelin.shared.data.repository.DailyLimitRepository
import com.suchelin.shared.data.repository.VoteRepository
import com.suchelin.shared.model.StoreData
import com.suchelin.shared.util.StoreFilter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class VoteViewModel(
    private val voteRepository: VoteRepository,
    private val dailyLimitRepository: DailyLimitRepository,
) {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    val rtData = voteRepository.votes.stateIn(scope, SharingStarted.Eagerly, emptyMap())

    private val _isLimited = MutableStateFlow(false)
    val isLimited: StateFlow<Boolean> = _isLimited.asStateFlow()

    private val _currentFilter = MutableStateFlow(StoreFilter.ALL)
    val currentFilter: StateFlow<StoreFilter> = _currentFilter.asStateFlow()

    fun setFilter(filter: StoreFilter) {
        _currentFilter.value = filter
    }

    fun refreshVotes() {
        scope.launch { voteRepository.refresh() }
    }

    fun sortByRank(stores: List<StoreData>): List<StoreData> {
        return stores.sortedByDescending { rtData.value[it.storeId.toString()] ?: 0L }
    }

    fun vote(storeId: Int, key: String) {
        scope.launch {
            val available = dailyLimitRepository.canVoteToday(storeId)
            _isLimited.value = !available
            if (!available) return@launch

            voteRepository.vote(key)
            dailyLimitRepository.markVoteUsed(storeId)
        }
    }
}
