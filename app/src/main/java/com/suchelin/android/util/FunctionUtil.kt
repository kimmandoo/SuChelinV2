package com.suchelin.android.util

import android.util.Log
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.NaverMap
import com.naver.maps.map.overlay.Marker

const val TOTAL_STORE_SIZE = 2

fun NaverMap.initMap() {
    apply {
        uiSettings.apply {
            isCompassEnabled = true
            isScaleBarEnabled = false
            isZoomControlEnabled = false
            isLocationButtonEnabled = false
        }
        cameraPosition = CameraPosition(
            // 초기 위치 정문
            LatLng(37.214185, 126.978792),
            18.0
        )
    }
}

fun NaverMap.initMarker(){
    apply {

    }
}

fun setStoreData(documentPath: Int, name: String, mainMenu: String, imageUrl: String, menuImageUrl: String? = null, latitude: Double, longitude: Double, type: String){
    val db = Firebase.firestore
    val docData = hashMapOf(
        "name" to name,
        "detail" to mainMenu,
        // imageUrl, menuImageUrl -> Copy Image Address
        "imageUrl" to imageUrl,
        "latitude" to latitude, // 30~
        "longitude" to longitude, // 120~
        "menuImageUrl" to menuImageUrl,
        "type" to type // restaurant or cafe
    )
    // 3까지 입력 됐음
    db.collection("store").document(documentPath.toString())
        .set(docData)
        .addOnSuccessListener { Log.d("TAG", "DocumentSnapshot successfully written!") }
        .addOnFailureListener { e -> Log.w("TAG", "Error writing document", e) }
}