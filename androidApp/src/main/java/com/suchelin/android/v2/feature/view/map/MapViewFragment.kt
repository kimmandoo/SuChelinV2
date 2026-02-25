package com.suchelin.android.v2.feature.view.map

import android.os.Bundle
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.suchelin.android.R
import com.suchelin.android.base.BaseFragment
import com.suchelin.android.v2.container.MainViewModel
import com.suchelin.android.databinding.FragmentMapBinding
import com.suchelin.android.util.AdManager
import com.suchelin.android.util.initMap
import com.suchelin.android.util.initMarker
import com.suchelin.android.util.initViewPager
import com.suchelin.android.util.parcelable.StoreDataArgs
import com.suchelin.android.util.sendMail
import com.suchelin.shared.model.StoreData

private const val TAG = "MAP"

class MapViewFragment : BaseFragment<FragmentMapBinding, MainViewModel>(R.layout.fragment_map),
    OnMapReadyCallback {
    override val viewModel: MainViewModel by activityViewModels()
    private lateinit var mapViewInstance: MapView
    private lateinit var googleMap: GoogleMap
    private lateinit var sendStoreInfo: NavDirections

    override fun initView() {
        binding.apply {
            adView.loadAd(AdManager.createAdRequest())
            val mapStoreAdapter = MapViewAdapter(viewModel.storeData.value!!) { store: StoreData ->
                setOnMapItemClick(store)
            }

            mapViewpager.adapter = mapStoreAdapter
            mapViewpager.initViewPager()

            mapViewInstance = mapView
            mapViewInstance.onCreate(null)
            mapViewInstance.getMapAsync(this@MapViewFragment)

            contact.setOnClickListener {
                sendMail(TAG)
            }
        }
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        googleMap.initMap()
        googleMap.initMarker(viewModel.storeData.value!!, binding.mapViewpager)
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
