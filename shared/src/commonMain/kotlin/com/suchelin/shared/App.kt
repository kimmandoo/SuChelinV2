package com.suchelin.shared

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ChatBubbleOutline
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material.icons.rounded.FormatListBulleted
import androidx.compose.material.icons.rounded.Campaign
import androidx.compose.material.icons.rounded.Map
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.suchelin.shared.ui.navigation.AppNavHost
import com.suchelin.shared.ui.navigation.NavRoutes
import com.suchelin.shared.ui.theme.GroupedBackground
import com.suchelin.shared.ui.theme.SuChelinTheme
import com.suchelin.shared.ui.theme.SystemGray3
import com.suchelin.shared.viewmodel.FeedViewModel
import com.suchelin.shared.viewmodel.MainViewModel
import com.suchelin.shared.viewmodel.ReportViewModel
import com.suchelin.shared.viewmodel.VoteViewModel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

@Composable
fun App() {
    val mainViewModel: MainViewModel = koinInject()
    val feedViewModel: FeedViewModel = koinInject()
    val voteViewModel: VoteViewModel = koinInject()
    val reportViewModel: ReportViewModel = koinInject()
    var route by remember { mutableStateOf(NavRoutes.LIST) }
    var previousRoute by remember { mutableStateOf<NavRoutes?>(null) }
    var selectedStoreId by remember { mutableStateOf<Int?>(null) }

    LaunchedEffect(Unit) {
        coroutineScope {
            launch { mainViewModel.bootstrap() }
            launch { voteViewModel.refreshVotes() }
        }
    }

    SuChelinTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(GroupedBackground),
        ) {
            // Content fills entire screen
            AppNavHost(
                route = route,
                onRouteChange = { newRoute ->
                    previousRoute = route
                    route = newRoute
                },
                onBack = {
                    val prev = previousRoute
                    if (prev != null) {
                        route = prev
                        previousRoute = null
                    } else {
                        route = NavRoutes.LIST
                    }
                },
                selectedStoreId = selectedStoreId,
                onSelectStore = { selectedStoreId = it.storeId },
                mainViewModel = mainViewModel,
                feedViewModel = feedViewModel,
                voteViewModel = voteViewModel,
                reportViewModel = reportViewModel,
                modifier = Modifier
                    .fillMaxSize()
                    .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Top + WindowInsetsSides.Horizontal)),
            )

            // Floating pill navbar overlaid on top of content
            if (route.showInBottomBar) {
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .windowInsetsPadding(WindowInsets.navigationBars.only(WindowInsetsSides.Bottom))
                        .padding(bottom = 12.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Row(
                        modifier = Modifier
                            .shadow(
                                elevation = 12.dp,
                                shape = RoundedCornerShape(22.dp),
                                ambientColor = Color.Black.copy(alpha = 0.12f),
                                spotColor = Color.Black.copy(alpha = 0.24f),
                            )
                            .background(
                                color = MaterialTheme.colorScheme.surface.copy(alpha = 0.72f),
                                shape = RoundedCornerShape(22.dp),
                            )
                            .background(
                                brush = androidx.compose.ui.graphics.Brush.verticalGradient(
                                    colors = listOf(
                                        Color.White.copy(alpha = 0.45f),
                                        Color.White.copy(alpha = 0.15f),
                                    ),
                                ),
                                shape = RoundedCornerShape(22.dp),
                            )
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        NavRoutes.entries.filter { it.showInBottomBar }.forEach { item ->
                            val isSelected = route == item
                            val icon: ImageVector = when (item) {
                                NavRoutes.LIST -> Icons.Rounded.FormatListBulleted
                                NavRoutes.MAP -> Icons.Rounded.Map
                                NavRoutes.VOTE -> Icons.Rounded.FavoriteBorder
//                                NavRoutes.FEED -> Icons.Rounded.ChatBubbleOutline
                                NavRoutes.REPORT -> Icons.Rounded.Campaign
                                else -> Icons.Rounded.FormatListBulleted
                            }
                            val tint by animateColorAsState(
                                targetValue = if (isSelected) MaterialTheme.colorScheme.onPrimary
                                else SystemGray3,
                            )
                            val bgColor by animateColorAsState(
                                targetValue = if (isSelected) MaterialTheme.colorScheme.primary.copy(
                                    alpha = 0.6f
                                )
                                else MaterialTheme.colorScheme.surface.copy(alpha = 0.6f),
                            )

                            Box(
                                modifier = Modifier
                                    .clickable(
                                        interactionSource = remember { MutableInteractionSource() },
                                        indication = null,
                                    ) { route = item }
                                    .background(bgColor, RoundedCornerShape(16.dp))
                                    .padding(horizontal = 16.dp, vertical = 8.dp),
                                contentAlignment = Alignment.Center,
                            ) {
                                Icon(
                                    imageVector = icon,
                                    contentDescription = item.title,
                                    modifier = Modifier.size(24.dp),
                                    tint = tint,
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
