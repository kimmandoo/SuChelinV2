package com.suchelin.android.util

import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.NaverMap

fun NaverMap.initMap() {
    apply {
        uiSettings.apply {
            isCompassEnabled = true
            isScaleBarEnabled = false
            isZoomControlEnabled = false
            isLocationButtonEnabled = false
        }
        cameraPosition = CameraPosition(
            LatLng(37.214200, 126.978750),
            18.0
        )
    }
}

fun NaverMap.initMarker(){
    apply {

    }
}