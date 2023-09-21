package com.suchelin.android.container

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.suchelin.android.base.BaseViewModel
import com.suchelin.android.util.docPostName
import com.suchelin.domain.model.PostData
import com.suchelin.domain.model.StoreData
import com.suchelin.domain.model.StoreDetail
import com.suchelin.domain.model.StoreMenuData
import com.suchelin.domain.model.StoreMenuDetail
import java.util.Date

class MainViewModel : BaseViewModel() {
    private val _db = Firebase.firestore
    private val _isInit = MutableLiveData<Boolean>(false)
    val isInit: LiveData<Boolean> = _isInit
    private val _storeList = mutableListOf<StoreData>()
    private val _storeData = MutableLiveData<List<StoreData>>()
    private val _menuDetailList = mutableListOf<StoreMenuDetail>()
    private val _menuList = mutableListOf<StoreMenuData>()
    private val _menuData = MutableLiveData<List<StoreMenuData>>()
    val storeData: LiveData<List<StoreData>> = _storeData
    val menuData: LiveData<List<StoreMenuData>> = _menuData

    private val _postList = mutableListOf<PostData>()
    private val _postData = MutableLiveData<List<PostData>>()
    val postData: LiveData<List<PostData>> = _postData

    fun loadMenuData() {
        _db.collection("menu").get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    for (items in document.data["menu"] as List<HashMap<String, String>>) {
                        _menuDetailList.add(
                            StoreMenuDetail(
                                menuPrice = items["menuPrice"] as String,
                                menuName = items["menuName"] as String
                            )
                        )
                    }
                    _menuList.add(
                        StoreMenuData(
                            document.id.toInt(),
                            _menuDetailList
                        )
                    )
                }
                _menuData.value = _menuList
            }
            .addOnFailureListener { exception ->
                Log.w("TAG", "Error getting documents.", exception)
            }
    }

    fun loadStoreData() {
        _db.collection("store")
            .orderBy("path", Query.Direction.ASCENDING).get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    _storeList.add(
                        StoreData(
                            document.id.toInt(),
                            StoreDetail(
                                name = document.data["name"] as String,
                                detail = document.data["detail"] as String,
                                imageUrl = document.data["imageUrl"] as String,
                                latitude = document.data["latitude"] as Double,
                                longitude = document.data["longitude"] as Double,
                                menuImageUrl = document.data["menuImageUrl"] as String?,
                                type = document.data["type"] as String
                            )
                        )
                    )
                }
                _storeData.value = _storeList
            }
            .addOnFailureListener { exception ->
                Log.w("TAG", "Error getting documents.", exception)
            }
    }

    fun loadPostData(initText: String) {
        _db.collection("suggest").document(docPostName.format(Date())).get()
            .addOnSuccessListener { result ->
                if(result.exists()){
                    val lines = result.data!!.asIterable().sortedByDescending { it.key }
                    for ((time, post) in lines) {
                        Log.d("TAG", "${time} : ${post}")
                        _postList.add(PostData(time, post.toString()))
                    }
                    _postData.value = _postList
                }else{
                    emptyPost(initText)
                }
            }
    }

    private fun emptyPost(initText: String){
        _postData.value = listOf(PostData("", initText))
    }

    fun postRefresh(initText: String){
        _postList.clear()
        loadPostData(initText)
    }

    fun initData(initText: String){
        loadPostData(initText)
        loadStoreData()
        loadMenuData()
        _isInit.value = true
    }

}