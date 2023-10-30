package com.suchelin.android.util

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Rect
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
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
import com.suchelin.domain.model.StoreData
import com.suchelin.domain.model.StoreDetail
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.jsoup.Jsoup
import java.text.SimpleDateFormat

@SuppressLint("SimpleDateFormat")
val docPostName = SimpleDateFormat("yyyy-MM-dd")
val adRequest = AdRequest.Builder().build()

fun loadSchoolMealMenu(){
    CoroutineScope(Dispatchers.IO).launch {
        val url = SCHOOL_MEAL
        try {
            val docs = Jsoup.connect(url).get()
            val tableElement = docs.select("div[class=contents_table2]").select("table")
            val weekDays = tableElement.select("thead").select("th").text().split(" ").subList(1,6) // 월 화 수 목 금
            val meal = tableElement.select("tbody").select("td")
            val mealList = mutableListOf<String>()
            for(item in meal){
                mealList.add(item.text())
            }
            if(mealList.isNotEmpty()){
                val momsCook = mealList.subList(2,7)
                val littleKitchen = mealList.subList(9,14)
                val officer = mealList.subList(15,mealList.size)
                Log.d("Jsoup","$momsCook\n$littleKitchen\n$officer")
            }
        }catch (e: Exception){
            Log.e("Jsoup","${e.message}")
        }

    }
}

fun Fragment.sendMail(tag: String) {
    val sendMailDialog = SendMailDialog(requireActivity(), tag)
    sendMailDialog.showDialog()
}

fun Fragment.toastMessageShort(message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
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

fun ViewPager2.initViewPager() {
    this.apply {
        addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(
                outRect: Rect,
                view: View,
                parent: RecyclerView,
                state: RecyclerView.State,
            ) {
                outRect.right = VISIBLE_ITEM_SIZE_X
                outRect.left = VISIBLE_ITEM_SIZE_X
            }
        })
        offscreenPageLimit = 1
        setPageTransformer { page, position ->
            page.translationX = -PAGE_TRANSLATION_X * (position)
        }
    }
}

fun NaverMap.initMarker(context: Context, storeList: List<StoreData>, mapViewPager: ViewPager2) {
    apply {
        val storeDataList = mutableListOf(
            StoreData(
                MAIN_GATE,
                StoreDetail("수원대학교 정문", "", "", 37.214185, 126.978792, null, "default")
            )
        )
        val markerList = mutableListOf<Marker>()
        val infoWindowInstance = InfoWindow()

        mapViewPager.registerOnPageChangeCallback(
            object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    moveCamera(
                        CameraUpdate.scrollAndZoomTo(
                            LatLng(
                                storeList[position].storeDetailData.latitude,
                                storeList[position].storeDetailData.longitude
                            ),
                            CAMERA_ZOOM
                        ).animate(CameraAnimation.Easing)
                    )
                    infoWindowInstance.setInfoWindow(
                        context,
                        markerList,
                        storeList[position].storeId,
                        storeList[position].storeDetailData.name
                    )
                }
            }
        )


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
                        icon = OverlayImage.fromResource(R.drawable.tea)
                        map = this@initMarker
                        height = MARKER_ICON_HEIGHT
                        width = MARKER_ICON_WEIGHT
                    }
                }

                StoreFilter.PUB.type -> { // pub
                    Marker().apply {
                        position =
                            LatLng(data.storeDetailData.latitude, data.storeDetailData.longitude)
                        icon = OverlayImage.fromResource(R.drawable.beer)
                        map = this@initMarker
                        height = MARKER_ICON_HEIGHT
                        width = MARKER_ICON_WEIGHT
                    }
                }

                else -> { // restaurant
                    Marker().apply {
                        position =
                            LatLng(data.storeDetailData.latitude, data.storeDetailData.longitude)
                        icon = OverlayImage.fromResource(R.drawable.rice)
                        map = this@initMarker
                        height = MARKER_ICON_HEIGHT
                        width = MARKER_ICON_WEIGHT
                    }
                }
            }

            // 일반 마커 클릭 시
            marker.setOnClickListener {
                moveMarker(data.storeId, storeDataList)
                infoWindowInstance.setInfoWindow(
                    context,
                    markerList,
                    data.storeId,
                    data.storeDetailData.name
                )
                mapViewPager.currentItem = data.storeId-1
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
            ).animate(CameraAnimation.Easing)
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
        .addOnSuccessListener {  }
        .addOnFailureListener { _ ->  }
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
        .addOnSuccessListener { }
        .addOnFailureListener { _ ->  }

}