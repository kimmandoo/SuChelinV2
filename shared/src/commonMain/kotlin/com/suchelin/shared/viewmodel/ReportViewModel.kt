package com.suchelin.shared.viewmodel

import com.suchelin.shared.data.repository.ReportRepository
import com.suchelin.shared.model.ReportData
import com.suchelin.shared.model.ReportType
import com.suchelin.shared.util.StoreFilter
import com.suchelin.shared.util.todayDocName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ReportViewModel(
    private val reportRepository: ReportRepository,
) {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    private val _isSubmitting = MutableStateFlow(false)
    val isSubmitting: StateFlow<Boolean> = _isSubmitting.asStateFlow()

    private val _reports = MutableStateFlow<List<ReportData>>(emptyList())
    val reports: StateFlow<List<ReportData>> = _reports.asStateFlow()

    private val _event = MutableSharedFlow<String>(extraBufferCapacity = 1)
    val event: SharedFlow<String> = _event.asSharedFlow()

    fun loadReports() {
        scope.launch {
            _reports.value = reportRepository.getReports()
        }
    }

    fun submitReport(
        type: ReportType,
        storeId: Int?,
        storeName: String,
        newStoreType: String? = null,
        content: String,
        targetMenuName: String? = null,
        previousPrice: String? = null,
        newPrice: String? = null,
    ) {
        if (storeName.isBlank()) {
            _event.tryEmit("가게 이름을 입력해주세요")
            return
        }
        if (type == ReportType.PRICE_CHANGE && (targetMenuName.isNullOrBlank() || newPrice.isNullOrBlank())) {
            _event.tryEmit("가격 변동은 기존 메뉴와 변동 가격을 입력해주세요")
            return
        }

        if (type == ReportType.NEW_STORE && newStoreType.isNullOrBlank()) {
            _event.tryEmit("신규 가게 유형을 선택해주세요")
            return
        }
        if (type == ReportType.NEW_STORE) {
            val allowed = setOf(StoreFilter.RESTAURANT.type, StoreFilter.CAFE.type, StoreFilter.PUB.type)
            if (newStoreType !in allowed) {
                _event.tryEmit("유효한 가게 유형을 선택해주세요")
                return
            }
        }

        if (type != ReportType.PRICE_CHANGE && content.isBlank()) {
            _event.tryEmit("상세 내용을 입력해주세요")
            return
        }

        val normalizedContent = if (type == ReportType.PRICE_CHANGE) {
            if (content.isBlank()) {
                "가격 변동: ${targetMenuName ?: "-"} ${previousPrice ?: "-"} -> ${newPrice ?: "-"}"
            } else {
                content
            }
        } else {
            content
        }

        scope.launch {
            _isSubmitting.value = true
            runCatching {
                reportRepository.submitReport(
                    ReportData(
                        type = type,
                        storeId = storeId,
                        storeName = storeName,
                        newStoreType = newStoreType,
                        content = normalizedContent,
                        targetMenuName = targetMenuName,
                        previousPrice = previousPrice,
                        newPrice = newPrice,
                        date = todayDocName(),
                    )
                )
            }.onSuccess {
                _event.tryEmit("제보가 등록되었습니다")
                loadReports()
            }.onFailure {
                _event.tryEmit("제보 등록에 실패했습니다")
            }
            _isSubmitting.value = false
        }
    }

    fun upvote(reportId: String) {
        scope.launch {
            reportRepository.upvoteReport(reportId)
            loadReports()
        }
    }

    fun downvote(reportId: String) {
        scope.launch {
            reportRepository.downvoteReport(reportId)
            loadReports()
        }
    }
}
