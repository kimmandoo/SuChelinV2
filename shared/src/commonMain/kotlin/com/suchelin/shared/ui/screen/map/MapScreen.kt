package com.suchelin.shared.ui.screen.map

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.suchelin.shared.model.StoreData
import com.suchelin.shared.ui.component.BannerAd
import com.suchelin.shared.ui.component.NativeMapView
import com.suchelin.shared.ui.component.PremiumCard
import com.suchelin.shared.ui.component.ScreenContainer
import com.suchelin.shared.ui.component.TopBar

@Composable
fun MapScreen(
    stores: List<StoreData>,
    onStoreClick: (StoreData) -> Unit,
    modifier: Modifier = Modifier,
) {
    ScreenContainer(modifier = modifier) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            TopBar(title = "지도", subtitle = "장소를 직관적으로 찾아보세요")

            BannerAd(modifier = Modifier.fillMaxWidth())

            PremiumCard(modifier = Modifier.fillMaxWidth()) {
                NativeMapView(
                    stores = stores,
                    onStoreClick = onStoreClick,
                    modifier = Modifier.fillMaxWidth().height(360.dp),
                )
            }

            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                items(stores) { store ->
                    Surface(
                        modifier = Modifier.clickable { onStoreClick(store) },
                        shape = RoundedCornerShape(20.dp),
                        color = MaterialTheme.colorScheme.surface,
                        tonalElevation = 0.dp,
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.LocationOn,
                                contentDescription = "위치",
                                modifier = Modifier.size(16.dp),
                                tint = MaterialTheme.colorScheme.primary,
                            )
                            Spacer(Modifier.width(4.dp))
                            Text(
                                store.storeDetailData.name,
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.onSurface,
                            )
                        }
                    }
                }
            }

            // Bottom spacing for floating navbar
            Spacer(modifier = Modifier.height(72.dp))
        }
    }
}
