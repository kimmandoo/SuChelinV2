package com.suchelin.android.util

import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.NaverMap
import com.naver.maps.map.overlay.Marker

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