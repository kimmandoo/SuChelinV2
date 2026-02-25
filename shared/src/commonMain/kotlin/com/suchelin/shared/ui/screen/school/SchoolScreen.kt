package com.suchelin.shared.ui.screen.school

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Restaurant
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.suchelin.shared.ui.component.PremiumCard
import com.suchelin.shared.ui.component.ScreenContainer
import com.suchelin.shared.ui.component.TopBar
import com.suchelin.shared.ui.component.WebViewScreen
import com.suchelin.shared.util.AMARENCE
import com.suchelin.shared.util.JONGHAP

@Composable
fun SchoolScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var currentUrl by remember { mutableStateOf(JONGHAP) }

    ScreenContainer(modifier = modifier, contentPadding = PaddingValues(horizontal = 0.dp)) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                TopBar(
                    title = "학식",
                    subtitle = "이번 주 식단표를 확인해요",
                    showBackButton = true,
                    onBackClick = onBack,
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    FilterChip(
                        selected = currentUrl == JONGHAP,
                        onClick = { currentUrl = JONGHAP },
                        shape = RoundedCornerShape(20.dp),
                        border = FilterChipDefaults.filterChipBorder(
                            borderColor = Color.Transparent,
                            enabled = true,
                            selected = currentUrl == JONGHAP,
                            selectedBorderColor = Color.Transparent,
                        ),
                        label = {
                            Row {
                                Icon(
                                    imageVector = Icons.Rounded.Restaurant,
                                    contentDescription = null,
                                    modifier = Modifier.padding(end = 4.dp).size(14.dp),
                                )
                                Text("종합", style = MaterialTheme.typography.labelLarge)
                            }
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant,
                            selectedContainerColor = MaterialTheme.colorScheme.primary,
                            selectedLabelColor = Color.White,
                        ),
                    )
                    FilterChip(
                        selected = currentUrl == AMARENCE,
                        onClick = { currentUrl = AMARENCE },
                        shape = RoundedCornerShape(20.dp),
                        border = FilterChipDefaults.filterChipBorder(
                            borderColor = Color.Transparent,
                            enabled = true,
                            selected = currentUrl == AMARENCE,
                            selectedBorderColor = Color.Transparent,
                        ),
                        label = {
                            Row {
                                Icon(
                                    imageVector = Icons.Rounded.Restaurant,
                                    contentDescription = null,
                                    modifier = Modifier.padding(end = 4.dp).size(14.dp),
                                )
                                Text("아마랜스", style = MaterialTheme.typography.labelLarge)
                            }
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant,
                            selectedContainerColor = MaterialTheme.colorScheme.primary,
                            selectedLabelColor = Color.White,
                        ),
                    )
                }
            }
            WebViewScreen(url = currentUrl, modifier = Modifier.fillMaxWidth().weight(1f))
        }
    }
}
