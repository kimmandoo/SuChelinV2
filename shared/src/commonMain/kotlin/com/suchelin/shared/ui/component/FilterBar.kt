package com.suchelin.shared.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.suchelin.shared.util.StoreFilter

@Composable
fun FilterBar(
    selected: StoreFilter,
    options: List<StoreFilter>,
    modifier: Modifier = Modifier,
    onSelect: (StoreFilter) -> Unit,
) {
    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(options) { filter ->
            val isSelected = selected == filter
            FilterChip(
                selected = isSelected,
                onClick = { onSelect(filter) },
                shape = RoundedCornerShape(20.dp),
                border = FilterChipDefaults.filterChipBorder(
                    borderColor = Color.Transparent,
                    enabled = true,
                    selected = isSelected,
                    selectedBorderColor = Color.Transparent,
                ),
                colors = FilterChipDefaults.filterChipColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    labelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    selectedContainerColor = MaterialTheme.colorScheme.primary,
                    selectedLabelColor = Color.White,
                ),
                label = {
                    Text(
                        text = when (filter) {
                            StoreFilter.ALL -> "전체"
                            StoreFilter.RESTAURANT -> "식당"
                            StoreFilter.CAFE -> "카페"
                            StoreFilter.PUB -> "주점"
                            StoreFilter.RANK -> "랭킹"
                            StoreFilter.SEARCH -> "검색"
                        },
                        style = MaterialTheme.typography.labelLarge,
                    )
                },
                modifier = Modifier.padding(vertical = 2.dp),
            )
        }
    }
}
