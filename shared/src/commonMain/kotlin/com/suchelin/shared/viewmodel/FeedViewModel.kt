package com.suchelin.shared.viewmodel

import com.suchelin.shared.data.repository.DailyLimitRepository
import com.suchelin.shared.data.repository.PostRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FeedViewModel(
    private val postRepository: PostRepository,
    private val dailyLimitRepository: DailyLimitRepository,
) {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    private val _isLimited = MutableStateFlow(false)
    val isLimited: StateFlow<Boolean> = _isLimited.asStateFlow()

    private val _event = MutableSharedFlow<String>(extraBufferCapacity = 1)
    val event: SharedFlow<String> = _event.asSharedFlow()

    fun submitPost(text: String, onDone: () -> Unit = {}) {
        scope.launch {
            if (text.length < 10) {
                _event.tryEmit("10글자 이상 작성해주세요")
                return@launch
            }

            val available = dailyLimitRepository.canPostToday()
            _isLimited.value = !available
            if (!available) {
                _event.tryEmit("오늘은 더이상 글을 쓸 수 없어요")
                return@launch
            }

            postRepository.addPost(text)
            dailyLimitRepository.markPostUsed()
            _event.tryEmit("등록되었습니다")
            onDone()
        }
    }
}
