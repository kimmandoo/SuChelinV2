package com.suchelin.android.util

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.gms.ads.AdRequest
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraAnimation
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.NaverMap
import com.naver.maps.map.overlay.InfoWindow
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import com.suchelin.android.R
import com.suchelin.android.feature.view.mail.SendMailDialog
import com.suchelin.android.feature.view_compose.list.StoreFilter
import com.suchelin.domain.model.StoreData
import com.suchelin.domain.model.StoreDetail
import java.text.SimpleDateFormat

const val MARKER_ICON_HEIGHT = 60
const val MARKER_ICON_WEIGHT = 60
const val CAMERA_ZOOM = 18.0
const val MAIN_GATE = 0

@SuppressLint("SimpleDateFormat")
val docPostName = SimpleDateFormat("yyyy-MM-dd")
val adRequest = AdRequest.Builder().build()

fun Fragment.sendMail(tag: String){
    val sendMailDialog = SendMailDialog(requireActivity(), tag)
    sendMailDialog.showDialog()
}

fun Fragment.toastMessageShort(message: String){
    Toast.makeText(context, message ,Toast.LENGTH_SHORT).show()
}
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
            CAMERA_ZOOM
        )
    }
}

fun NaverMap.initMarker(context: Context, storeList: List<StoreData>) {
    apply {
        val storeDataList = mutableListOf(
            StoreData(
                MAIN_GATE,
                StoreDetail("수원대학교 정문", "", "", 37.214185, 126.978792, null, "default")
            )
        )
        val markerList = mutableListOf<Marker>()
        val infoWindowInstance = InfoWindow()

        markerList.add(Marker().apply {
            position = LatLng(37.214185, 126.978792)
            icon = OverlayImage.fromResource(R.drawable.school)
            map = this@initMarker
            height = MARKER_ICON_HEIGHT
            width = MARKER_ICON_WEIGHT
        })

        // 정문 마커 클릭 시
        markerList[MAIN_GATE].setOnClickListener {
            moveMarker(MAIN_GATE, storeDataList)
            Log.d("MAP", storeDataList[MAIN_GATE].storeDetailData.name)
            infoWindowInstance.setInfoWindow(
                context,
                markerList,
                MAIN_GATE,
                storeDataList[MAIN_GATE].storeDetailData.name
            )
            true
        }

        storeList.forEachIndexed { _, data ->
            val marker = when (data.storeDetailData.type) {
                StoreFilter.CAFE.type -> {
                    Marker().apply {
                        position =
                            LatLng(data.storeDetailData.latitude, data.storeDetailData.longitude)
                        icon =  OverlayImage.fromResource(R.drawable.tea)
                        map = this@initMarker
                        height = MARKER_ICON_HEIGHT
                        width = MARKER_ICON_WEIGHT
                    }
                }

                StoreFilter.PUB.type -> { // pub
                    Marker().apply {
                        position =
                            LatLng(data.storeDetailData.latitude, data.storeDetailData.longitude)
                        icon =  OverlayImage.fromResource(R.drawable.beer)
                        map = this@initMarker
                        height = MARKER_ICON_HEIGHT
                        width = MARKER_ICON_WEIGHT
                    }
                }

                else -> { // restaurant
                    Marker().apply {
                        position =
                            LatLng(data.storeDetailData.latitude, data.storeDetailData.longitude)
                        icon =  OverlayImage.fromResource(R.drawable.rice)
                        map = this@initMarker
                        height = MARKER_ICON_HEIGHT
                        width = MARKER_ICON_WEIGHT
                    }
                }
            }

            // 일반 마커 클릭 시
            marker.setOnClickListener {
                moveMarker(data.storeId, storeDataList)
                Log.d("MAP", storeDataList[data.storeId].storeDetailData.name)
                infoWindowInstance.setInfoWindow(
                    context,
                    markerList,
                    data.storeId,
                    data.storeDetailData.name
                )
                true
            }
            storeDataList.add(data.storeId, data)
            markerList.add(marker)
        }
    }
}

fun NaverMap.moveMarker(id: Int, storeDataList: List<StoreData>) {
    apply {
        moveCamera(
            CameraUpdate.scrollAndZoomTo(
                LatLng(
                    storeDataList[id].storeDetailData.latitude,
                    storeDataList[id].storeDetailData.longitude
                ),
                CAMERA_ZOOM
            ).animate(CameraAnimation.Fly)
        )
    }
}

private fun InfoWindow.setInfoWindow(
    context: Context,
    markerList: List<Marker>,
    markerIndex: Int,
    infoString: String,
) {
    adapter = object : InfoWindow.DefaultTextAdapter(context) {
        override fun getText(infoWindow: InfoWindow): CharSequence {
            return infoString
        }
    }
    open(markerList[markerIndex])
}

fun setStoreData(
    path: Int,
    name: String,
    mainMenu: String,
    imageUrl: String,
    menuImageUrl: String? = null,
    latitude: Double,
    longitude: Double,
    type: String,
) {
    val db = Firebase.firestore
    val docData = hashMapOf(
        "path" to path,
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
    db.collection("store").document(path.toString())
        .set(docData)
        .addOnSuccessListener { Log.d("TAG", "DocumentSnapshot successfully written!") }
        .addOnFailureListener { e -> Log.w("TAG", "Error writing document", e) }
}

fun setStoreMenu(
    image: Boolean = false,
    menu: List<Any>,
    tel: String,
    path: Int,
) {
    val db = Firebase.firestore

    val docData = hashMapOf(
        "image" to image,
        "menu" to menu,
        "path" to path,
        "tel" to tel
    )

    // 3까지 입력 됐음
    db.collection("menu").document(path.toString())
        .set(docData)
        .addOnSuccessListener { Log.d("TAG", "DocumentSnapshot successfully written!") }
        .addOnFailureListener { e -> Log.w("TAG", "Error writing document", e) }

}