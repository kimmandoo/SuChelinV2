package com.suchelin.domain.model

data class StoreMenuData(
    val storeId: Int,
    val storeMenu: List<StoreMenuDetail>
)

data class StoreMenuDetail(
    val menuName: String,
    val menuPrice: String
)
