package com.suchelin.shared.ui.navigation

enum class NavRoutes(val title: String, val showInBottomBar: Boolean) {
    LIST("리스트", true),
    MAP("지도", true),
    VOTE("좋아요", true),
//    FEED("피드", true),
    REPORT("제보", true),
    SCHOOL("학식", false),
    DETAIL("상세", false),
}
