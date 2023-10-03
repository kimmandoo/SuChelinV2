package com.suchelin.android.feature.view_compose.detail

import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.platform.rememberNestedScrollInteropConnection
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.suchelin.android.R
import com.suchelin.android.base.BaseFragment
import com.suchelin.android.container.MainViewModel
import com.suchelin.android.databinding.FragmentDetailBinding
import com.suchelin.android.feature.compose.ui.jamsil
import com.suchelin.android.util.parcelable.StoreDataArgs
import com.suchelin.domain.model.StoreMenuDetail


class DetailFragment :
    BaseFragment<FragmentDetailBinding, DetailViewModel>(R.layout.fragment_detail) {
    override val viewModel: DetailViewModel by viewModels()
    private val sharedViewModel: MainViewModel by activityViewModels()
    private val args: DetailFragmentArgs by navArgs()
    private var storeTel: String? = null
    override fun initView() {
        val storeInfo: StoreDataArgs = args.storeInfo
        Log.d("id", "${storeInfo}")

        sharedViewModel.menuData.observe(viewLifecycleOwner) { menuData ->
            menuData?.let {
                val currentStoreMenu = menuData[storeInfo.storeId]?.storeMenu
                storeTel = menuData[storeInfo.storeId]?.tel
                binding.tvDetailToTel.text = storeTel
                binding.detailStoreName.text = storeInfo.storeName
                Glide.with(this).load(storeInfo.imageUrl).centerCrop().into(binding.detailStoreImg)

                binding.composeView.apply {
                    setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
                    setContent() {
                        if (currentStoreMenu?.first() is StoreMenuDetail) {
                            MenuLazyColumn(currentStoreMenu as List<StoreMenuDetail>)
                        }
                    }
                }
                Log.d("Tag", "${currentStoreMenu}, ${storeTel}")
                // rv에 넣을때 item 타입이 StoreMenuDetail 인지, String인지 확인해서 rv 돌리기
            }
        }

        binding.apply {
            detailToTel.setOnClickListener {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("tel:${storeTel}")))
            }

            btnBack.setOnClickListener {
                findNavController().popBackStack()
            }
        }
    }

    @Composable
    fun MenuLazyColumn(menuDataList: List<StoreMenuDetail>) {
        val menu by remember { mutableStateOf(menuDataList) }

        val nestedScrollInterop = rememberNestedScrollInteropConnection()
        LazyColumn(
            modifier = Modifier.nestedScroll(nestedScrollInterop),
            contentPadding = PaddingValues(12.dp, 4.dp, 12.dp, 0.dp)
        ) {
            items(
                count = menu.size,
                itemContent = { MenuListItem(menu[it]) }
            )
        }
    }

    @Composable
    fun MenuListItem(menu: StoreMenuDetail) {
        Box(
            modifier =
            Modifier
                .fillMaxWidth()
                .padding(0.dp, 4.dp, 0.dp, 4.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Box(modifier = Modifier.width(240.dp)){
                    Text(
                        text = menu.menuName,
                        fontFamily = jamsil,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        modifier = Modifier.padding(end = 12.dp)
                    )
                }
                Box(){
                    Text(
                        text = menu.menuPrice,
                        fontFamily = jamsil,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                    )
                }
            }
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .padding(0.dp, 0.dp, 0.dp, 0.dp)
                .background(color = Color.LightGray)
        )

    }
}