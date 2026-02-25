package com.suchelin.shared.ui.screen.vote

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.suchelin.shared.model.StoreData
import com.suchelin.shared.ui.component.FilterBar
import com.suchelin.shared.ui.component.PlatformRemoteImage
import com.suchelin.shared.ui.component.PremiumCard
import com.suchelin.shared.ui.component.ScreenContainer
import com.suchelin.shared.ui.component.TopBar
import com.suchelin.shared.ui.theme.SystemRed
import com.suchelin.shared.util.StoreFilter

@Composable
fun VoteScreen(
    stores: List<StoreData>,
    rankedStores: List<StoreData>,
    voteData: Map<String, Long>,
    isLimited: Boolean,
    onVote: (StoreData) -> Unit,
    onOpenDetail: (StoreData) -> Unit,
    modifier: Modifier = Modifier,
) {
    var filter by remember { mutableStateOf(StoreFilter.ALL) }

    val base = if (filter == StoreFilter.RANK) rankedStores else stores
    val filtered = when (filter) {
        StoreFilter.CAFE -> base.filter { it.storeDetailData.type == StoreFilter.CAFE.type }
        StoreFilter.RESTAURANT -> base.filter { it.storeDetailData.type == StoreFilter.RESTAURANT.type }
        StoreFilter.PUB -> base.filter { it.storeDetailData.type == StoreFilter.PUB.type }
        else -> base
    }

    ScreenContainer(modifier = modifier) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(bottom = 72.dp),
        ) {
            item(span = { GridItemSpan(maxLineSpan) }) {
                Column {
                    TopBar(title = "투표", subtitle = "좋아하는 가게에 마음을 남겨주세요")
                    Spacer(modifier = Modifier.height(12.dp))

                    FilterBar(
                        selected = filter,
                        options = listOf(StoreFilter.ALL, StoreFilter.RESTAURANT, StoreFilter.CAFE, StoreFilter.PUB, StoreFilter.RANK),
                        onSelect = { filter = it },
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }

            items(filtered) { store ->
                VoteCard(
                    store = store,
                    voteCount = voteData[store.storeId.toString()] ?: 0L,
                    isLimited = isLimited,
                    onVote = { onVote(store) },
                    onClick = { onOpenDetail(store) },
                )
            }

            item { Spacer(modifier = Modifier.height(72.dp)) }
        }

        if (isLimited) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "오늘의 투표를 모두 사용했어요",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(horizontal = 4.dp),
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
private fun VoteCard(
    store: StoreData,
    voteCount: Long,
    isLimited: Boolean,
    onVote: () -> Unit,
    onClick: () -> Unit,
) {
    PremiumCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
    ) {
        Column {
            PlatformRemoteImage(
                imageUrl = store.storeDetailData.imageUrl,
                contentDescription = store.storeDetailData.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio((5/2f))
                    .clip(RoundedCornerShape(topStart = 14.dp, topEnd = 14.dp)),
                placeholderDrawableName = "ic_launcher_foreground",
            )

            Column(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    store.storeDetailData.name,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    IconButton(
                        onClick = onVote,
                        enabled = !isLimited,
                        modifier = Modifier.size(32.dp),
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.FavoriteBorder,
                            contentDescription = "좋아요",
                            modifier = Modifier.size(20.dp),
                            tint = if (isLimited) MaterialTheme.colorScheme.onSurfaceVariant
                            else SystemRed,
                        )
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        "${voteCount}",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        }
    }
}
