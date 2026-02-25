package com.suchelin.shared.viewmodel

import com.suchelin.shared.data.repository.MenuRepository
import com.suchelin.shared.data.repository.PostRepository
import com.suchelin.shared.data.repository.StoreRepository
import com.suchelin.shared.model.PostData
import com.suchelin.shared.model.StoreData
import com.suchelin.shared.model.StoreMenuDetail
import com.suchelin.shared.model.StoreMenuData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel(
    private val storeRepository: StoreRepository,
    private val menuRepository: MenuRepository,
    private val postRepository: PostRepository,
) {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    private val _isInit = MutableStateFlow(false)
    val isInit: StateFlow<Boolean> = _isInit.asStateFlow()

    private val _storeData = MutableStateFlow<List<StoreData>>(emptyList())
    val storeData: StateFlow<List<StoreData>> = _storeData.asStateFlow()

    private val _menuData = MutableStateFlow<Map<Int, StoreMenuData>>(emptyMap())
    val menuData: StateFlow<Map<Int, StoreMenuData>> = _menuData.asStateFlow()

    private val _postData = MutableStateFlow<List<PostData>>(emptyList())
    val postData: StateFlow<List<PostData>> = _postData.asStateFlow()

    private val _randomShown = MutableStateFlow(false)
    val randomShown: StateFlow<Boolean> = _randomShown.asStateFlow()

    suspend fun bootstrap() {
        if (_isInit.value) return
        _storeData.value = storeRepository.getStores()
        _menuData.value = menuRepository.getMenus()
        _postData.value = postRepository.getPosts()
        _isInit.value = true
    }

    fun initData() {
        scope.launch { bootstrap() }
    }

    fun postRefresh() {
        scope.launch {
            _postData.value = postRepository.getPosts()
        }
    }

    fun markRandomShown() {
        _randomShown.value = true
    }

    fun getStoreById(storeId: Int): StoreData? = _storeData.value.firstOrNull { it.storeId == storeId }

    fun getMenuDetailByStoreId(storeId: Int): List<StoreMenuDetail> {
        return _menuData.value[storeId]?.storeMenu?.filterIsInstance<StoreMenuDetail>() ?: emptyList()
    }

    fun getStoreTel(storeId: Int): String {
        return _menuData.value[storeId]?.tel ?: ""
    }
}
