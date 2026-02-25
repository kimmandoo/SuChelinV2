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
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.suchelin.android.R
import com.suchelin.android.feature.view.mail.SendMailDialog
import com.suchelin.android.util.parcelable.SingleMapDataArgs
import com.suchelin.shared.model.SchoolMealData
import com.suchelin.shared.model.StoreData
import com.suchelin.shared.model.StoreDetail
import org.jsoup.Jsoup
import java.text.SimpleDateFormat
import java.util.Date

@SuppressLint("SimpleDateFormat")
val docPostName = SimpleDateFormat("yyyy-MM-dd")

fun todayDate(): String {
    return docPostName.format(Date())
}

fun loadSchoolMealMenu(): SchoolMealData {
    val url = JONGHAP

    val docs = Jsoup.connect(url).get()
    val tableElement = docs.select("div[class=contents_table2]").select("table")
    val weekDays = tableElement.select("thead").select("th").text().split(" ")
        .subList(1, 6) // 월 화 수 목 금
    val meal = tableElement.select("tbody").select("td")
    val mealList = mutableListOf<String>()
    for (item in meal) {
        mealList.add(item.text())
    }
    if (mealList.isNotEmpty()) {
        Log.d("Jsoup", "${mealList}")
        val momsCook = mealList.subList(2, 7)
        val littleKitchen = mealList.subList(9, 14)
        val officer = mealList.subList(15, mealList.size)
        return SchoolMealData(weekDays, momsCook, littleKitchen, officer)
        Log.d("Jsoup", "$momsCook\n$littleKitchen\n$officer")
    }

    return SchoolMealData(emptyList(), emptyList(), emptyList(), emptyList())
}

fun Fragment.sendMail(tag: String) {
    val sendMailDialog = SendMailDialog(requireActivity(), tag)
    sendMailDialog.showDialog()
}

fun Fragment.toastMessageShort(message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

fun GoogleMap.initMap() {
    uiSettings.apply {
        isCompassEnabled = true
        isZoomControlsEnabled = false
        isMyLocationButtonEnabled = false
    }
    moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(37.214185, 126.978792), CAMERA_ZOOM.toFloat()))
}

fun GoogleMap.singleMarker(singleStoreData: SingleMapDataArgs) {
    initMap()
    clear()
    moveCamera(
        CameraUpdateFactory.newLatLngZoom(
            LatLng(singleStoreData.latitude, singleStoreData.longitude),
            (CAMERA_ZOOM + 1).toFloat()
        )
    )
    addMarker(
        MarkerOptions()
            .position(LatLng(singleStoreData.latitude, singleStoreData.longitude))
            .title(singleStoreData.storeName)
            .icon(BitmapDescriptorFactory.fromResource(R.drawable.restaurant))
    )?.showInfoWindow()
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

fun GoogleMap.initMarker(storeList: List<StoreData>, mapViewPager: ViewPager2) {
    val storeDataMap = mutableMapOf(
        MAIN_GATE to StoreData(
            MAIN_GATE,
            StoreDetail("수원대학교 정문", "", "", 37.214185, 126.978792, null, "default")
        )
    )
    val markerStoreIdMap = mutableMapOf<Marker, Int>()
    val markerByStoreId = mutableMapOf<Int, Marker>()

    val gateMarker = addMarker(
        MarkerOptions()
            .position(LatLng(37.214185, 126.978792))
            .title(storeDataMap[MAIN_GATE]?.storeDetailData?.name)
            .icon(BitmapDescriptorFactory.fromResource(R.drawable.school))
    )
    if (gateMarker != null) {
        markerStoreIdMap[gateMarker] = MAIN_GATE
        markerByStoreId[MAIN_GATE] = gateMarker
    }

    storeList.forEach { data ->
        storeDataMap[data.storeId] = data
        val marker = addMarker(
            MarkerOptions()
                .position(LatLng(data.storeDetailData.latitude, data.storeDetailData.longitude))
                .title(data.storeDetailData.name)
                .icon(markerIconByType(data.storeDetailData.type))
        )
        if (marker != null) {
            markerStoreIdMap[marker] = data.storeId
            markerByStoreId[data.storeId] = marker
        }
    }

    mapViewPager.registerOnPageChangeCallback(
        object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                val store = storeList[position]
                moveMarker(store.storeId, storeDataMap)
                markerByStoreId[store.storeId]?.showInfoWindow()
            }
        }
    )

    setOnMarkerClickListener { marker ->
        val storeId = markerStoreIdMap[marker] ?: return@setOnMarkerClickListener false
        moveMarker(storeId, storeDataMap)
        marker.showInfoWindow()

        if (storeId != MAIN_GATE) {
            val index = storeList.indexOfFirst { it.storeId == storeId }
            if (index >= 0 && mapViewPager.currentItem != index) {
                mapViewPager.currentItem = index
            }
        }
        true
    }
}

fun GoogleMap.moveMarker(id: Int, storeDataMap: Map<Int, StoreData>) {
    val storeData = storeDataMap[id] ?: return
    moveCamera(
        CameraUpdateFactory.newLatLngZoom(
            LatLng(storeData.storeDetailData.latitude, storeData.storeDetailData.longitude),
            CAMERA_ZOOM.toFloat()
        )
    )
}

private fun markerIconByType(type: String) =
    when (type) {
        StoreFilter.CAFE.type -> BitmapDescriptorFactory.fromResource(R.drawable.tea)
        StoreFilter.PUB.type -> BitmapDescriptorFactory.fromResource(R.drawable.beer)
        else -> BitmapDescriptorFactory.fromResource(R.drawable.rice)
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
        .addOnSuccessListener { }
        .addOnFailureListener { _ -> }
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
        .addOnFailureListener { _ -> }

}
