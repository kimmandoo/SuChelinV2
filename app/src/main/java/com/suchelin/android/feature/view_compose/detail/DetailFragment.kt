package com.suchelin.android.feature.view_compose.detail

import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.platform.rememberNestedScrollInteropConnection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.bumptech.glide.Glide
import com.suchelin.android.R
import com.suchelin.android.base.BaseFragment
import com.suchelin.android.container.MainViewModel
import com.suchelin.android.databinding.FragmentDetailBinding
import com.suchelin.android.feature.compose.ui.jamsil
import com.suchelin.android.feature.view_compose.list.ListFragmentDirections
import com.suchelin.android.feature.view_compose.list.StoreFilter
import com.suchelin.android.util.parcelable.StoreDataArgs
import com.suchelin.domain.model.StoreData
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
                // rv에 데이터 값 넣기
//                val storeData = menuData[storeInfo.storeId]?.storeMenu
                val currentStoreMenu = menuData[storeInfo.storeId]?.storeMenu
                storeTel = menuData[storeInfo.storeId]?.tel
                binding.tvDetailToTel.text = storeTel
                binding.detailStoreName.text = storeInfo.storeName
                Glide.with(this).load(storeInfo.imageUrl).centerCrop().into(binding.detailStoreImg)

                binding.composeView.apply {
                    setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
                    setContent() {
                        if(currentStoreMenu?.first() is StoreMenuDetail){
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
            contentPadding = PaddingValues(12.dp, 12.dp, 12.dp, 0.dp)
        ) {
            items(
                count = menu.size,
                itemContent = { MenuListItem(menu[it]) }
            )
        }
    }

    @Composable
    fun MenuListItem(menu: StoreMenuDetail) {
        Box(modifier =
        Modifier
            .fillMaxWidth()
            .padding(0.dp, 0.dp, 0.dp, 8.dp)) {
            Row(modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween) {
                Text(
                    text = menu.menuName,
                    fontFamily = jamsil,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(end = 12.dp)
                )
                Text(
                    text = menu.menuPrice,
                    fontFamily = jamsil,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                )
            }
        }
    }
}