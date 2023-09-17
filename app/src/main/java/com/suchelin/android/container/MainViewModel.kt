package com.suchelin.android.container

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.suchelin.android.base.BaseViewModel

class MainViewModel: BaseViewModel() {
    private val db = Firebase.firestore
    val storeData = db.collection("store")

}