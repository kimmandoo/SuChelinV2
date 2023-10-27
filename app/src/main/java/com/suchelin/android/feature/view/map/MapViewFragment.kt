package com.suchelin.android.feature.view.map

import android.graphics.Rect
import android.location.LocationManager
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.MapView
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.util.FusedLocationSource
import com.suchelin.android.R
import com.suchelin.android.base.BaseFragment
import com.suchelin.android.container.MainViewModel
import com.suchelin.android.databinding.FragmentMapBinding
import com.suchelin.android.util.adRequest
import com.suchelin.android.util.initMap
import com.suchelin.android.util.initMarker
import com.suchelin.android.util.initViewPager
import com.suchelin.android.util.sendMail

private const val LOCATION_PERMISSION_REQUEST_CODE = 10002

class MapViewFragment : BaseFragment<FragmentMapBinding, MainViewModel>(R.layout.fragment_map),
    OnMapReadyCallback {
    override val viewModel: MainViewModel by activityViewModels()
    private val TAG = "MAP"
    private lateinit var mapViewInstance: MapView
    private lateinit var naverMap: NaverMap
    private lateinit var locationSource: FusedLocationSource
    private lateinit var locationManager: LocationManager

    override fun initView() {
        binding.apply {
            adView.loadAd(adRequest)
            val mapStoreAdapter = MapViewAdapter(viewModel.storeData.value!!)
            mapViewpager.adapter = mapStoreAdapter
            mapViewpager.initViewPager()

            mapViewInstance = mapView
            mapViewInstance.getMapAsync { map ->
                naverMap = map
                naverMap.initMap()
                naverMap.initMarker(requireContext(), viewModel.storeData.value!!)
            }

            contact.setOnClickListener {
                sendMail(TAG)
            }
        }
    }

    override fun onMapReady(p0: NaverMap) {

    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray,
    ) {
        if (locationSource.onRequestPermissionsResult(
                requestCode, permissions,
                grantResults
            )
        ) {
            if (!locationSource.isActivated) { // 권한 거부됨
                naverMap?.locationTrackingMode = LocationTrackingMode.None
            }
            return
        }
    }


    override fun onStart() {
        super.onStart()
        mapViewInstance.onStart()
    }

    override fun onResume() {
        super.onResume()
        mapViewInstance.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapViewInstance.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapViewInstance.onSaveInstanceState(outState)
    }

    override fun onStop() {
        super.onStop()
        mapViewInstance.onStop()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mapViewInstance.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapViewInstance.onLowMemory()
    }
}
