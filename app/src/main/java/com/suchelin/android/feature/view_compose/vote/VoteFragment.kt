package com.suchelin.android.feature.view_compose.vote

import android.graphics.drawable.Drawable
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
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
import androidx.fragment.app.viewModels
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.suchelin.android.R
import com.suchelin.android.base.BaseFragment
import com.suchelin.android.container.MainViewModel
import com.suchelin.android.databinding.FragmentVoteBinding
import com.suchelin.android.feature.compose.ui.jamsil
import com.suchelin.android.util.StoreFilter
import com.suchelin.android.util.parcelable.StoreDataArgs
import com.suchelin.android.util.room.LikeDbInstance
import com.suchelin.android.util.sendMail
import com.suchelin.android.util.toastMessageShort
import com.suchelin.domain.model.StoreData

private const val TAG = "VOTE"

class VoteFragment : BaseFragment<FragmentVoteBinding, VoteViewModel>(R.layout.fragment_vote) {
    override val viewModel: VoteViewModel by viewModels()
    private val sharedViewModel: MainViewModel by activityViewModels()
    private lateinit var storeListReference: List<StoreData>
    private lateinit var sendStoreInfo: NavDirections

    override fun initView() {
        viewModel.readRTDB()

        sharedViewModel.storeData.observe(viewLifecycleOwner) { storeList ->
            storeList?.let {
                storeListReference = it
            }
        }

        viewModel.isLimited.observe(viewLifecycleOwner){ isFull ->
            if(isFull){
                toastMessageShort(getString(R.string.likeLimit))
            }
        }

        viewModel.rtData.observe(viewLifecycleOwner) { votedData ->
            votedData?.let {
                binding.loading.isVisible = false
                binding.filterBar.isVisible = true
                if (::storeListReference.isInitialized) {
                    setComposeView(storeListReference, viewModel.currentFilter)
                }
            }
        }

        binding.apply {
            binding.loading.isVisible = true

            contact.setOnClickListener {
                sendMail(TAG)
            }

            val filterButtons = mapOf(
                all to StoreFilter.ALL,
                restaurant to StoreFilter.RESTAURANT,
                pub to StoreFilter.PUB,
                cafe to StoreFilter.CAFE,
                rank to StoreFilter.RANK
            )

            filterButtons.forEach { (button, filter) ->
                button.setOnClickListener {
                    setComposeView(storeListReference, filter)
                    viewModel.currentFilter = filter
                }
            }
        }
    }

    private fun setComposeView(storeList: List<StoreData>, filter: StoreFilter) {
        binding.composeView.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnDetachedFromWindowOrReleasedFromPool)
            setContent {
                VoteGrid(storeList, filter)
            }
        }
    }

    private fun rankingFilter(storeDataList: List<StoreData>): List<StoreData> {
        val sortedRT = viewModel.rtData.value?.entries?.sortedBy { it.value }?.map { it.key }
        return storeDataList.sortedByDescending { sortedRT!!.indexOf(it.storeId.toString()) }
    }

    @Composable
    fun VoteGrid(storeDataList: List<StoreData>, filter: StoreFilter) {
        val filteredStores = when (filter) {
            StoreFilter.CAFE -> storeDataList.filter { it.storeDetailData.type == StoreFilter.CAFE.type }
            StoreFilter.RESTAURANT -> storeDataList.filter { it.storeDetailData.type == StoreFilter.RESTAURANT.type }
            StoreFilter.PUB -> storeDataList.filter { it.storeDetailData.type == StoreFilter.PUB.type }
            StoreFilter.ALL -> storeDataList
            StoreFilter.RANK -> rankingFilter(storeDataList)
            else -> {
                storeDataList
            }
        }

        val nestedScrollInterop = rememberNestedScrollInteropConnection()
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.nestedScroll(nestedScrollInterop),
        ) {
            items(count = filteredStores.size,
                itemContent = {
                    StoreListItem(
                        filteredStores[it]
                    ) { viewModel.addVote(filteredStores[it].storeId.toString(), LikeDbInstance.getDatabase(requireContext()).likeDao()) }
                }
            )
        }
    }

    @Composable
    fun StoreListItem(store: StoreData, onClick: () -> Unit) {
        Box(
            modifier =
            Modifier
                .padding(8.dp, 0.dp, 8.dp, 8.dp)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Card(
                    modifier = Modifier
                        .padding(4.dp)
                        .height(80.dp)
                        .fillMaxWidth(), shape = RoundedCornerShape(5.dp)
                ) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(store.storeDetailData.imageUrl)
                            .crossfade(true)
                            .build(),
                        placeholder = painterResource(R.drawable.ic_launcher_foreground),
                        contentDescription = "img",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxSize()
                            .clickable {
                                sendStoreInfo =
                                    VoteFragmentDirections.actionNavigationVoteToNavigationDetail(
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
                    )
                }
                Column(Modifier.padding(0.dp, 8.dp)) {
                    Text(
                        text = store.storeDetailData.name,
                        fontFamily = jamsil,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .height(24.dp),
                        maxLines = 1
                    )
                }
                Row(modifier = Modifier.align(CenterHorizontally)) {
                    Text(
                        text = if (viewModel.rtData.value!![store.storeId.toString()] != null) {
                            viewModel.rtData.value!![store.storeId.toString()].toString()
                        } else {
                            "0"
                        },
                        fontFamily = jamsil,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 12.sp,
                        modifier = Modifier
                            .align(CenterVertically)
                            .padding(end = 8.dp)
                    )
                    Vote(resources.getDrawable(R.drawable.heart, null), onClick)
                }
            }
        }
    }

    @Composable
    fun Vote(
        img: Drawable, onClick: () -> Unit
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(img)
                .crossfade(true)
                .build(),
            placeholder = painterResource(R.drawable.heart),
            contentDescription = "img",
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .height(24.dp)
                .width(24.dp)
                .clickable(onClick = onClick)
        )
    }
}