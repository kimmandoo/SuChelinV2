package com.suchelin.android.feature.view_compose.detail

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.platform.rememberNestedScrollInteropConnection
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
import com.suchelin.android.util.parcelable.SingleMapDataArgs
import com.suchelin.android.util.parcelable.StoreDataArgs
import com.suchelin.domain.model.StoreMenuDetail


class DetailFragment :
    BaseFragment<FragmentDetailBinding, DetailViewModel>(R.layout.fragment_detail) {
    override val viewModel: DetailViewModel by viewModels()
    private val sharedViewModel: MainViewModel by activityViewModels()
    private val args: DetailFragmentArgs by navArgs()

    override fun initView() {
        val storeInfo: StoreDataArgs = args.storeInfo

        sharedViewModel.menuData.observe(viewLifecycleOwner) { menuData ->
            menuData?.let {
                val currentStoreMenu = menuData[storeInfo.storeId]?.storeMenu
                menuData[storeInfo.storeId]?.tel?.let { tel ->
                    binding.tvDetailToTel.text = tel
                    binding.detailToTel.setOnClickListener {
                        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("tel:$tel")))
                    }
                }
                if (menuData[storeInfo.storeId]?.tel.isNullOrEmpty()) {
                    binding.tvDetailToTel.text = getString(R.string.empty_store)
                }
                binding.detailStoreName.text = storeInfo.storeName
                Glide.with(this).load(storeInfo.imageUrl).centerCrop().into(binding.detailStoreImg)
                binding.composeView.apply {
                    setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
                    setContent() {
                        if (currentStoreMenu?.first() is StoreMenuDetail) {
                            MenuLazyColumn(currentStoreMenu as List<StoreMenuDetail>)
                        } else {
                            EmptyScreen()
                        }
                    }
                }
            }
        }

        binding.apply {
            btnBack.setOnClickListener {
                findNavController().popBackStack()
            }
            detailToMap.setOnClickListener {
                findNavController().navigate(
                    DetailFragmentDirections.actionNavigationDetailToSingleMapViewFragment(
                        SingleMapDataArgs(
                            storeInfo.storeName,
                            storeInfo.latitude,
                            storeInfo.longitude
                        )
                    )
                )
            }
        }
    }

    @Composable
    fun EmptyScreen() {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(20.dp, 20.dp), shape = RoundedCornerShape(10.dp)
        ) {
            Text(
                text = getString(R.string.empty_menu),
                textAlign = TextAlign.Center,
                fontFamily = jamsil,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .background(Color.White)
                    .fillMaxWidth()
            )
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(R.drawable.post_alert)
                    .crossfade(true)
                    .build(),
                alpha = 0.2f,
                placeholder = null,
                contentDescription = "img",
                contentScale = ContentScale.FillWidth,
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
            )
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
                Box(modifier = Modifier.width(240.dp)) {
                    Text(
                        text = menu.menuName,
                        fontFamily = jamsil,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        modifier = Modifier.padding(end = 12.dp)
                    )
                }
                Box() {
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