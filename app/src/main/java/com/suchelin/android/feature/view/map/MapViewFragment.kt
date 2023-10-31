package com.suchelin.android.feature.view.map

import android.location.LocationManager
import android.os.Bundle
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.google.android.gms.ads.MobileAds
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
import com.suchelin.android.util.parcelable.StoreDataArgs
import com.suchelin.android.util.sendMail
import com.suchelin.domain.model.StoreData

private const val TAG = "MAP"

class MapViewFragment : BaseFragment<FragmentMapBinding, MainViewModel>(R.layout.fragment_map),
    OnMapReadyCallback {
    override val viewModel: MainViewModel by activityViewModels()
    private lateinit var mapViewInstance: MapView
    private lateinit var naverMap: NaverMap
    private lateinit var sendStoreInfo: NavDirections
    private lateinit var locationSource: FusedLocationSource
    private lateinit var locationManager: LocationManager

    override fun initView() {
        MobileAds.initialize(requireContext())
        binding.apply {
            adView.loadAd(adRequest)
            val mapStoreAdapter = MapViewAdapter(viewModel.storeData.value!!) { store: StoreData ->
                setOnMapItemClick(store)
            }

            mapViewpager.adapter = mapStoreAdapter
            mapViewpager.initViewPager()

            mapViewInstance = mapView
            mapViewInstance.getMapAsync { map ->
                naverMap = map
                naverMap.initMap()
                naverMap.initMarker(requireContext(), viewModel.storeData.value!!, mapViewpager)
            }

            contact.setOnClickListener {
                sendMail(TAG)
            }
        }
    }

    override fun onMapReady(p0: NaverMap) {

    }

    private fun setOnMapItemClick(store: StoreData) {
        sendStoreInfo =
            MapViewFragmentDirections.actionNavigationMapToNavigationDetail(
                StoreDataArgs(
                    store.storeId,
                    store.storeDetailData.name,
                    store.storeDetailData.imageUrl,
                    store.storeDetailData.latitude,
                    store.storeDetailData.longitude
                )
            )
        findNavController().navigate(sendStoreInfo)
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
                naverMap.locationTrackingMode = LocationTrackingMode.None
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
