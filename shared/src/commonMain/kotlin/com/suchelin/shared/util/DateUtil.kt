package com.suchelin.shared.util

import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

fun todayDate(): String {
    return todayDocName()
}

fun todayDocName(): String {
    val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
    val month = now.monthNumber.toString().padStart(2, '0')
    val day = now.dayOfMonth.toString().padStart(2, '0')
    return "${now.year}-$month-$day"
}

fun todayKoreanLabel(): String {
    val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
    val month = now.monthNumber.toString().padStart(2, '0')
    val day = now.dayOfMonth.toString().padStart(2, '0')
    return "${now.year}년 ${month}월 ${day}일"
}

fun nowTimeKey(): String {
    val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).time
    val hour = now.hour.toString().padStart(2, '0')
    val minute = now.minute.toString().padStart(2, '0')
    val second = now.second.toString().padStart(2, '0')
    return "$hour-$minute-$second"
}
