package com.suchelin.domain.model

data class StoreData(
    val storeId: Int,
    val storeDetailData: StoreDetail
)

data class StoreDetail(
    val name: String,
    val detail: String,
    val imageUrl: String,
    val latitude: Double,
    val longitude: Double,
    val menuImageUrl: String?,
)