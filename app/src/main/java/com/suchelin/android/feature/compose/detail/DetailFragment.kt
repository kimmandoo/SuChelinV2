package com.suchelin.android.feature.compose.detail

import android.util.Log
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.suchelin.android.R
import com.suchelin.android.base.BaseFragment
import com.suchelin.android.container.MainViewModel
import com.suchelin.android.databinding.FragmentDetailBinding
import com.suchelin.domain.model.StoreMenuData
import com.suchelin.domain.model.StoreMenuDetail

class DetailFragment :
    BaseFragment<FragmentDetailBinding, DetailViewModel>(R.layout.fragment_detail) {
    override val viewModel: DetailViewModel by viewModels()
    val sharedViewModel: MainViewModel by activityViewModels()
    private val args: DetailFragmentArgs by navArgs()
    override fun initView() {
        val storeId: Int = args.storeId
        Log.d("id","${storeId}")

        sharedViewModel.menuData.observe(viewLifecycleOwner){ menuData->
            menuData?.let {
                // rv에 데이터 값 넣기
                val currentStoreMenu = menuData[storeId]?.storeMenu
                // rv에 넣을때 item 타입이 StoreMenuDetail 인지, String인지 확인해서 rv 돌리기
            }
        }

        binding.apply {
            btnBack.setOnClickListener {
                findNavController().popBackStack()
            }
        }
    }
}