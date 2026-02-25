package com.suchelin.shared.model

data class StoreMenuData(
    val image: Boolean,
    val storeMenu: List<Any>,
    val tel: String
)

data class StoreMenuDetail(
    val menuName: String,
    val menuPrice: String
)
