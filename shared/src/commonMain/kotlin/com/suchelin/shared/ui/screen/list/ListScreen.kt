package com.suchelin.shared.ui.screen.list

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material.icons.rounded.LocalCafe
import androidx.compose.material.icons.rounded.Restaurant
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.SportsBar
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.suchelin.shared.model.StoreData
import com.suchelin.shared.ui.component.FilterBar
import com.suchelin.shared.ui.component.PlatformRemoteImage
import com.suchelin.shared.ui.component.PremiumCard
import com.suchelin.shared.ui.component.ScreenContainer
import com.suchelin.shared.ui.component.TopBar
import com.suchelin.shared.ui.theme.SystemGray3
import com.suchelin.shared.ui.theme.SystemGray5
import com.suchelin.shared.util.StoreFilter

@Composable
fun ListScreen(
    stores: List<StoreData>,
    onStoreClick: (StoreData) -> Unit,
    onContactClick: () -> Unit = {},
    modifier: Modifier = Modifier,
) {
    var filter by remember { mutableStateOf(StoreFilter.ALL) }
    var query by remember { mutableStateOf("") }
    var showRandom by remember { mutableStateOf(false) }
    var randomStore by remember { mutableStateOf<StoreData?>(null) }

    LaunchedEffect(stores) {
        if (stores.isNotEmpty() && randomStore == null) {
            randomStore = stores.random()
            showRandom = true
        }
    }

    val searched = if (query.isBlank()) stores else stores.filter {
        it.storeDetailData.name.contains(query, ignoreCase = true)
    }

    val filtered = when (filter) {
        StoreFilter.CAFE -> searched.filter { it.storeDetailData.type == StoreFilter.CAFE.type }
        StoreFilter.RESTAURANT -> searched.filter { it.storeDetailData.type == StoreFilter.RESTAURANT.type }
        StoreFilter.PUB -> searched.filter { it.storeDetailData.type == StoreFilter.PUB.type }
        else -> searched
    }

    ScreenContainer(modifier = modifier) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(0.dp),
        ) {
            item {
                TopBar(
                    title = "수슐랭",
                    subtitle = "캠퍼스 근처 맛집을 탐색해요",
                    contactLabel = "문의",
                    onContactClick = onContactClick,
                )
                Spacer(modifier = Modifier.height(12.dp))
            }

            // Search bar
            item {
                TextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp)),
                    value = query,
                    onValueChange = { query = it },
                    placeholder = {
                        Text(
                            "검색",
                            style = MaterialTheme.typography.bodyLarge,
                            color = SystemGray3,
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Rounded.Search,
                            contentDescription = "검색",
                            tint = SystemGray3,
                            modifier = Modifier.size(20.dp),
                        )
                    },
                    singleLine = true,
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        cursorColor = MaterialTheme.colorScheme.primary,
                    ),
                    shape = RoundedCornerShape(12.dp),
                )
                Spacer(modifier = Modifier.height(12.dp))
            }

            // Filter chips
            item {
                FilterBar(
                    selected = filter,
                    options = listOf(StoreFilter.ALL, StoreFilter.RESTAURANT, StoreFilter.CAFE, StoreFilter.PUB),
                    onSelect = { filter = it },
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            if (filtered.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 40.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = "검색결과가 없습니다",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
            } else {
                // Store list in grouped card style
                item {
                    PremiumCard(modifier = Modifier.fillMaxWidth()) {
                        Column {
                            filtered.forEachIndexed { index, store ->
                                StoreListItem(
                                    store = store,
                                    onClick = { onStoreClick(store) },
                                )
                                if (index < filtered.lastIndex) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(start = 80.dp)
                                            .height(0.5.dp)
                                            .background(MaterialTheme.colorScheme.outlineVariant),
                                    )
                                }
                            }
                        }
                    }
                }

                item { Spacer(modifier = Modifier.height(16.dp)) }
            }

            // Bottom spacing for floating navbar
            item { Spacer(modifier = Modifier.height(72.dp)) }
        }
    }

    val picked = randomStore
    if (picked != null && showRandom) {
        RandomDialog(
            visible = true,
            store = picked,
            onDismiss = { showRandom = false },
            onOpenDetail = {
                showRandom = false
                onStoreClick(it)
            },
        )
    }
}

@Composable
private fun StoreListItem(
    store: StoreData,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        PlatformRemoteImage(
            imageUrl = store.storeDetailData.imageUrl,
            contentDescription = store.storeDetailData.name,
            modifier = Modifier
                .size(56.dp)
                .clip(RoundedCornerShape(12.dp)),
            placeholderDrawableName = "ic_launcher_foreground",
        )

        Spacer(modifier = Modifier.width(14.dp))

        Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = store.storeDetailData.name,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Spacer(modifier = Modifier.width(6.dp))
                Icon(
                    imageVector = when (store.storeDetailData.type) {
                        StoreFilter.CAFE.type -> Icons.Rounded.LocalCafe
                        StoreFilter.PUB.type -> Icons.Rounded.SportsBar
                        else -> Icons.Rounded.Restaurant
                    },
                    contentDescription = store.storeDetailData.type,
                    modifier = Modifier.size(14.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = store.storeDetailData.detail,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
            )
        }

        Icon(
            imageVector = Icons.Rounded.ChevronRight,
            contentDescription = null,
            modifier = Modifier.size(20.dp),
            tint = SystemGray3,
        )
    }
}
