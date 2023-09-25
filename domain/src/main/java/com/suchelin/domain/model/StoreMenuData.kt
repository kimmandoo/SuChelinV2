package com.suchelin.domain.model

data class StoreMenuData(
    val image: Boolean,
    val storeMenu: List<Any>
)

data class StoreMenuDetail(
    val menuName: String,
    val menuPrice: String
)
