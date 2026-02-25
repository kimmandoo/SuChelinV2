package com.suchelin.shared.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.suchelin.shared.model.StoreData
import com.suchelin.shared.ui.component.makePhoneCall
import com.suchelin.shared.ui.screen.detail.DetailScreen
import com.suchelin.shared.ui.screen.feed.FeedScreen
import com.suchelin.shared.ui.screen.list.ListScreen
import com.suchelin.shared.ui.screen.map.MapScreen
import com.suchelin.shared.ui.screen.school.SchoolScreen
import com.suchelin.shared.ui.screen.vote.VoteScreen
import com.suchelin.shared.viewmodel.FeedViewModel
import com.suchelin.shared.viewmodel.MainViewModel
import com.suchelin.shared.viewmodel.VoteViewModel

@Composable
fun AppNavHost(
    route: NavRoutes,
    onRouteChange: (NavRoutes) -> Unit,
    onBack: () -> Unit,
    selectedStoreId: Int?,
    onSelectStore: (StoreData) -> Unit,
    mainViewModel: MainViewModel,
    feedViewModel: FeedViewModel,
    voteViewModel: VoteViewModel,
    modifier: Modifier = Modifier,
) {
    val stores by mainViewModel.storeData.collectAsState()
    val posts by mainViewModel.postData.collectAsState()
    val voteData by voteViewModel.rtData.collectAsState()
    val isVoteLimited by voteViewModel.isLimited.collectAsState()
    val isFeedLimited by feedViewModel.isLimited.collectAsState()
    val menuData by mainViewModel.menuData.collectAsState()
    val selected = selectedStoreId?.let { id -> stores.firstOrNull { it.storeId == id } }
    val feedMessage by feedViewModel.event.collectAsState(initial = null)

    Box(modifier = modifier.fillMaxSize()) {
        when (route) {
            NavRoutes.LIST -> ListScreen(
                stores = stores,
                onStoreClick = {
                    onSelectStore(it)
                    onRouteChange(NavRoutes.DETAIL)
                },
                onContactClick = { onRouteChange(NavRoutes.FEED) },
            )
            NavRoutes.FEED -> FeedScreen(
                posts = posts,
                isLimited = isFeedLimited,
                onSubmitPost = {
                    feedViewModel.submitPost(it) {
                        mainViewModel.postRefresh()
                    }
                },
                message = feedMessage,
            )
            NavRoutes.VOTE -> VoteScreen(
                stores = stores,
                rankedStores = voteViewModel.sortByRank(stores),
                voteData = voteData,
                isLimited = isVoteLimited,
                onVote = { voteViewModel.vote(it.storeId, it.storeId.toString()) },
                onOpenDetail = {
                    onSelectStore(it)
                    onRouteChange(NavRoutes.DETAIL)
                },
            )
            NavRoutes.MAP -> MapScreen(
                stores = stores,
                onStoreClick = {
                    onSelectStore(it)
                    onRouteChange(NavRoutes.DETAIL)
                },
            )
            NavRoutes.SCHOOL -> SchoolScreen()
            NavRoutes.DETAIL -> DetailScreen(
                store = selected,
                menu = selected?.let { mainViewModel.getMenuDetailByStoreId(it.storeId) } ?: emptyList(),
                tel = selected?.let { menuData[it.storeId]?.tel } ?: "",
                onOpenMap = { onRouteChange(NavRoutes.MAP) },
                onCall = {
                    val tel = selected?.let { menuData[it.storeId]?.tel } ?: ""
                    makePhoneCall(tel)
                },
                onBack = onBack,
            )
        }
    }
}
