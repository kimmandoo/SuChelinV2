package com.suchelin.android.feature.compose.list

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.suchelin.android.R
import com.suchelin.android.base.BaseFragment
import com.suchelin.android.container.MainViewModel
import com.suchelin.android.databinding.FragmentListBinding
import com.suchelin.domain.model.StoreData

class ListFragment : BaseFragment<FragmentListBinding, MainViewModel>(R.layout.fragment_list) {

    override val viewModel: MainViewModel by activityViewModels()

    override fun initView() {
        viewModel.storeData.observe(viewLifecycleOwner) { storeList ->
            storeList?.let {
                binding.progressCircular.isVisible = false
                binding.composeView.apply {
                    setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
                    setContent() {
                        StoreRecyclerView(it)
                    }
                }
            }
        }

        binding.apply {
            progressCircular.isVisible = true
        }
    }

    @Composable
    fun StoreRecyclerView(storeDataList: List<StoreData>) {
        val stores = remember { storeDataList }
        LazyColumn(contentPadding = PaddingValues(16.dp, 0.dp, 16.dp, 60.dp)) {
            items(
                count = stores.size,
                itemContent = { StoreListItem(stores[it]) }
            )
        }
    }

    @Composable
    fun StoreListItem(store: StoreData) {
        Box(modifier =
        Modifier
            .clickable {
                Toast
                    .makeText(context, "${store.storeDetailData.name}", Toast.LENGTH_SHORT)
                    .show()
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
                        modifier = Modifier.clip(RectangleShape)
                    )
                }
                Column(Modifier.padding(8.dp, 0.dp)) {
                    Text(
                        text = store.storeDetailData.name,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = store.storeDetailData.detail,
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }
        }
    }
}