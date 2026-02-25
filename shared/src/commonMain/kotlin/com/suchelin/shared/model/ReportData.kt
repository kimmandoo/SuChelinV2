package com.suchelin.shared.model

data class ReportData(
    val id: String = "",
    val type: ReportType,
    val storeId: Int?,
    val storeName: String,
    val newStoreType: String? = null,
    val content: String,
    val targetMenuName: String? = null,
    val previousPrice: String? = null,
    val newPrice: String? = null,
    val date: String,
    val uid: String = "",
    val upvotes: Int = 0,
    val downvotes: Int = 0,
    val votedUpUids: List<String> = emptyList(),
    val votedDownUids: List<String> = emptyList(),
    val applied: Boolean = false,
)

enum class ReportType(val label: String) {
    MENU_ADD("메뉴 추가"),
    MENU_REMOVE("메뉴 제거"),
    PRICE_CHANGE("가격 변동"),
    CLOSED("폐업 신고"),
    NEW_STORE("신규 가게"),
    OTHER("기타"),
}
