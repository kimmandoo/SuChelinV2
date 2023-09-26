package com.suchelin.android.feature.compose.detail

import android.util.Log
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.suchelin.android.R
import com.suchelin.android.base.BaseFragment
import com.suchelin.android.container.MainViewModel
import com.suchelin.android.databinding.FragmentDetailBinding

class DetailFragment :
    BaseFragment<FragmentDetailBinding, DetailViewModel>(R.layout.fragment_detail) {
    override val viewModel: DetailViewModel by viewModels()
    val sharedViewModel: MainViewModel by activityViewModels()
    private val args: DetailFragmentArgs by navArgs()
    override fun initView() {
        val storeId: Int = args.storeId
        Log.d("id","${storeId}")
    }
}