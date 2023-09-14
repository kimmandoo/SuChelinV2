package com.suchelin.android.feature.view.map

import androidx.fragment.app.viewModels
import com.suchelin.android.R
import com.suchelin.android.base.BaseFragment
import com.suchelin.android.databinding.FragmentMapBinding

class MapFragment : BaseFragment<FragmentMapBinding, MapViewModel>(R.layout.fragment_map) {
    override val viewModel: MapViewModel by viewModels()
    override fun initView() {
        binding.apply {

        }
    }
}