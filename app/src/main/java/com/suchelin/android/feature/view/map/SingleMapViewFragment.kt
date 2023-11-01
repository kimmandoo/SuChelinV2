package com.suchelin.android.feature.view.map

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.naver.maps.map.MapView
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.suchelin.android.R
import com.suchelin.android.base.BaseFragment
import com.suchelin.android.databinding.FragmentSingleMapBinding
import com.suchelin.android.util.AdManager
import com.suchelin.android.util.parcelable.SingleMapDataArgs
import com.suchelin.android.util.sendMail
import com.suchelin.android.util.singleMarker

private const val TAG = "SINGLE_MAP"

class SingleMapViewFragment :
    BaseFragment<FragmentSingleMapBinding, MapViewModel>(R.layout.fragment_single_map),
    OnMapReadyCallback {
    override val viewModel: MapViewModel by viewModels()
    private val args: SingleMapViewFragmentArgs by navArgs()
    private lateinit var mapViewInstance: MapView
    private lateinit var naverMap: NaverMap

    override fun initView() {
        val mapInfo: SingleMapDataArgs = args.mapInfo

        binding.apply {
            adView.loadAd(AdManager.createAdRequest())
            btnBack.setOnClickListener { findNavController().popBackStack() }
            contact.setOnClickListener {
                sendMail(TAG)
            }
            tvToolbar.text = mapInfo.storeName
            mapViewInstance = mapView
            mapViewInstance.getMapAsync { map ->
                naverMap = map
                naverMap.singleMarker(requireContext(), mapInfo)
            }
        }
    }

    override fun onMapReady(p0: NaverMap) {

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