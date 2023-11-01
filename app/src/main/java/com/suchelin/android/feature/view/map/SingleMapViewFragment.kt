package com.suchelin.android.feature.view.map

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.naver.maps.map.MapView
import com.naver.maps.map.NaverMap
import com.suchelin.android.R
import com.suchelin.android.base.BaseFragment
import com.suchelin.android.databinding.FragmentSingleMapBinding
import com.suchelin.android.util.adRequest
import com.suchelin.android.util.parcelable.SingleMapDataArgs
import com.suchelin.android.util.sendMail
import com.suchelin.android.util.singleMarker

private const val TAG = "SINGLE_MAP"

class SingleMapViewFragment :
    BaseFragment<FragmentSingleMapBinding, MapViewModel>(R.layout.fragment_single_map) {
    override val viewModel: MapViewModel by viewModels()
    private val args: SingleMapViewFragmentArgs by navArgs()
    private lateinit var mapViewInstance: MapView
    private lateinit var naverMap: NaverMap

    override fun initView() {
        val mapInfo: SingleMapDataArgs = args.mapInfo

        binding.apply {
            adView.loadAd(adRequest)
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
}