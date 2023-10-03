package com.suchelin.android.feature.view_compose.list

import android.widget.Toast
import androidx.compose.foundation.clickable
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
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.suchelin.android.R
import com.suchelin.android.base.BaseFragment
import com.suchelin.android.container.MainViewModel
import com.suchelin.android.databinding.FragmentListBinding
import com.suchelin.android.feature.compose.ui.jamsil
import com.suchelin.android.feature.view.mail.SendMailDialog
import com.suchelin.android.util.parcelable.StoreDataArgs
import com.suchelin.domain.model.StoreData


enum class StoreFilter {
    CAFE,
    RESTAURANT,
    PUB,
    ALL
}

class ListFragment : BaseFragment<FragmentListBinding, MainViewModel>(R.layout.fragment_list) {

    override val viewModel: MainViewModel by activityViewModels()
    private val TAG = "LIST"
    private lateinit var sendStoreInfo: NavDirections
    private lateinit var storeListReference: List<StoreData>

    override fun initView() {
        val sendMailDialog = SendMailDialog(requireActivity(), TAG)
        viewModel.storeData.observe(viewLifecycleOwner) { storeList ->
            storeList?.let {
                storeListReference = it
                binding.progressCircular.isVisible = false
                setComposeView(it, StoreFilter.ALL)
            }
        }

        binding.apply {
            progressCircular.isVisible = true
            contact.setOnClickListener {
                sendMailDialog.showDialog()
            }

            val filterButtons = mapOf(
                all to StoreFilter.ALL,
                restaurant to StoreFilter.RESTAURANT,
                pub to StoreFilter.PUB,
                cafe to StoreFilter.CAFE
            )

            filterButtons.forEach { (button, filter) ->
                button.setOnClickListener {
                    setComposeView(storeListReference, filter)
                }
            }
        }
    }

    private fun setComposeView(storeList: List<StoreData>, filter: StoreFilter) {
        binding.composeView.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent() {
                StoreRecyclerView(storeList, filter)
            }
        }
    }

    @Composable
    fun StoreRecyclerView(storeDataList: List<StoreData>, filter: StoreFilter) {
        val stores by remember { mutableStateOf(storeDataList) }
        val filteredStores = when (filter) {
            StoreFilter.CAFE -> stores.filter { it.storeDetailData.type == "cafe" }
            StoreFilter.RESTAURANT -> stores.filter { it.storeDetailData.type == "restaurant" }
            StoreFilter.PUB -> stores.filter { it.storeDetailData.type == "pub" }
            StoreFilter.ALL -> stores
        }
        val nestedScrollInterop = rememberNestedScrollInteropConnection()
        LazyColumn(
            modifier = Modifier.nestedScroll(nestedScrollInterop),
            contentPadding = PaddingValues(16.dp, 0.dp, 16.dp, 60.dp)
        ) {
            items(
                count = filteredStores.size,
                itemContent = { StoreListItem(filteredStores[it]) }
            )
        }
    }

    @Composable
    fun StoreListItem(store: StoreData) {
        Box(modifier =
        Modifier
            .clickable {
                sendStoreInfo =
                    ListFragmentDirections.actionNavigationMainToNavigationDetail(
                        StoreDataArgs(
                            store.storeId,
                            store.storeDetailData.name,
                            store.storeDetailData.imageUrl
                        )
                    )
                Toast
                    .makeText(
                        context, "${store.storeId}: ${store.storeDetailData.name}\n${
                            viewModel.menuData.value?.get(store.storeId)
                        }", Toast.LENGTH_SHORT
                    )
                    .show()
                findNavController().navigate(sendStoreInfo)
            }
            .fillMaxWidth()
            .padding(0.dp, 0.dp, 0.dp, 8.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Card(
                    modifier = Modifier
                        .padding(4.dp)
                        .size(80.dp), shape = RoundedCornerShape(5.dp)
                ) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(store.storeDetailData.imageUrl)
                            .crossfade(true)
                            .build(),
                        placeholder = painterResource(R.drawable.ic_launcher_foreground),
                        contentDescription = "img",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }
                Column(Modifier.padding(8.dp, 0.dp)) {
                    Text(
                        text = store.storeDetailData.name,
                        fontFamily = jamsil,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                    )
                    Box(modifier = Modifier.size(4.dp))
                    Text(
                        text = store.storeDetailData.detail,
                        fontFamily = jamsil,
                        fontWeight = FontWeight.Normal
                    )
                }
            }
        }
    }
}