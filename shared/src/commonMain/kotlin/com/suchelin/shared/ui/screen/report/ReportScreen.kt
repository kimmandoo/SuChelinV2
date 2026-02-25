package com.suchelin.shared.ui.screen.report

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.ArrowUpward
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.KeyboardArrowUp
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.ThumbDown
import androidx.compose.material.icons.rounded.ThumbDownOffAlt
import androidx.compose.material.icons.rounded.ThumbUp
import androidx.compose.material.icons.rounded.ThumbUpOffAlt
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import com.suchelin.shared.model.ReportData
import com.suchelin.shared.model.ReportType
import com.suchelin.shared.model.StoreData
import com.suchelin.shared.model.StoreMenuData
import com.suchelin.shared.model.StoreMenuDetail
import com.suchelin.shared.ui.component.AppDialog
import com.suchelin.shared.ui.component.PremiumCard
import com.suchelin.shared.ui.component.ScreenContainer
import com.suchelin.shared.ui.component.SectionHeader
import com.suchelin.shared.ui.component.TopBar
import com.suchelin.shared.ui.theme.SystemGray3
import com.suchelin.shared.ui.theme.SystemGreen
import com.suchelin.shared.ui.theme.SystemRed
import com.suchelin.shared.util.StoreFilter
import com.suchelin.shared.viewmodel.ReportViewModel

@Composable
fun ReportScreen(
    stores: List<StoreData>,
    menuData: Map<Int, StoreMenuData>,
    reportViewModel: ReportViewModel,
    modifier: Modifier = Modifier,
) {
    var selectedType by remember { mutableStateOf(ReportType.MENU_ADD) }
    var selectedStore by remember { mutableStateOf<StoreData?>(null) }
    var storeQuery by remember { mutableStateOf("") }
    var newStoreName by remember { mutableStateOf("") }
    var newStoreType by remember { mutableStateOf<String?>(null) }
    var content by remember { mutableStateOf("") }
    var selectedMenuName by remember { mutableStateOf<String?>(null) }
    var previousPrice by remember { mutableStateOf<String?>(null) }
    var newPrice by remember { mutableStateOf("") }
    var showConfirm by remember { mutableStateOf(false) }
    var showStoreSearch by remember { mutableStateOf(false) }
    var isFormExpanded by remember { mutableStateOf(false) }

    val isSubmitting by reportViewModel.isSubmitting.collectAsState()
    val reports by reportViewModel.reports.collectAsState()
    val event by reportViewModel.event.collectAsState(initial = null)

    val isNewStore = selectedType == ReportType.NEW_STORE
    val isPriceChange = selectedType == ReportType.PRICE_CHANGE
    val storeName = if (isNewStore) newStoreName else (selectedStore?.storeDetailData?.name ?: "")
    val currentMenus = if (isPriceChange && selectedStore != null) {
        menuData[selectedStore!!.storeId]
            ?.storeMenu
            ?.filterIsInstance<StoreMenuDetail>()
            ?: emptyList()
    } else {
        emptyList()
    }

    LaunchedEffect(Unit) {
        reportViewModel.loadReports()
    }

    ScreenContainer(modifier = modifier) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
        ) {
            item {
                TopBar(title = "제보", subtitle = "가게 정보를 알려주세요")
                Spacer(modifier = Modifier.height(12.dp))
            }

            // Expandable form toggle
            item {
                PremiumCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { isFormExpanded = !isFormExpanded },
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Add,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp),
                            tint = MaterialTheme.colorScheme.primary,
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            "새 제보 작성하기",
                            style = MaterialTheme.typography.titleSmall,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.weight(1f),
                        )
                        Icon(
                            imageVector = if (isFormExpanded) Icons.Rounded.KeyboardArrowUp
                            else Icons.Rounded.KeyboardArrowDown,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
            }

            // Expandable form content
            item {
                AnimatedVisibility(
                    visible = isFormExpanded,
                    enter = expandVertically(),
                    exit = shrinkVertically(),
                ) {
                    Column {
                        // Type chips
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            items(ReportType.entries) { type ->
                                val isSelected = selectedType == type
                                FilterChip(
                                    selected = isSelected,
                                    onClick = {
                                        selectedType = type
                                        selectedStore = null
                                        storeQuery = ""
                                        showStoreSearch = false
                                        selectedMenuName = null
                                        previousPrice = null
                                        newPrice = ""
                                        newStoreType = null
                                    },
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
                                        Text(type.label, style = MaterialTheme.typography.labelLarge)
                                    },
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(12.dp))

                        // Store selection
                        PremiumCard(modifier = Modifier.fillMaxWidth()) {
                            Column {
                                if (isNewStore) {
                                    TextField(
                                        modifier = Modifier.fillMaxWidth(),
                                        value = newStoreName,
                                        onValueChange = { newStoreName = it },
                                        placeholder = {
                                            Text("가게 이름", style = MaterialTheme.typography.bodyLarge, color = SystemGray3)
                                        },
                                        singleLine = true,
                                        colors = transparentTextFieldColors(),
                                    )

                                    Spacer(modifier = Modifier.height(8.dp))

                                    Text(
                                        text = "가게 유형 선택 (필수)",
                                        style = MaterialTheme.typography.labelLarge,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier.padding(horizontal = 16.dp),
                                    )

                                    Spacer(modifier = Modifier.height(6.dp))

                                    LazyRow(
                                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                                        modifier = Modifier.padding(horizontal = 12.dp),
                                    ) {
                                        items(listOf(StoreFilter.RESTAURANT, StoreFilter.CAFE, StoreFilter.PUB)) { filter ->
                                            val selected = newStoreType == filter.type
                                            FilterChip(
                                                selected = selected,
                                                onClick = { newStoreType = filter.type },
                                                label = {
                                                    Text(
                                                        when (filter) {
                                                            StoreFilter.RESTAURANT -> "식당"
                                                            StoreFilter.CAFE -> "카페"
                                                            StoreFilter.PUB -> "주점"
                                                            else -> filter.name
                                                        }
                                                    )
                                                },
                                                colors = FilterChipDefaults.filterChipColors(
                                                    selectedContainerColor = MaterialTheme.colorScheme.primary,
                                                    selectedLabelColor = Color.White,
                                                ),
                                            )
                                        }
                                    }
                                } else if (selectedStore != null && !showStoreSearch) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable { showStoreSearch = true }
                                            .padding(16.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                    ) {
                                        Icon(Icons.Rounded.Check, null, Modifier.size(18.dp), tint = SystemGreen)
                                        Spacer(Modifier.width(10.dp))
                                        Text(
                                            selectedStore!!.storeDetailData.name,
                                            style = MaterialTheme.typography.bodyLarge,
                                            fontWeight = FontWeight.Medium,
                                            modifier = Modifier.weight(1f),
                                        )
                                        Text("변경", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)
                                    }
                                } else {
                                    TextField(
                                        modifier = Modifier.fillMaxWidth(),
                                        value = storeQuery,
                                        onValueChange = { storeQuery = it; showStoreSearch = true },
                                        placeholder = {
                                            Text("가게 검색", style = MaterialTheme.typography.bodyLarge, color = SystemGray3)
                                        },
                                        leadingIcon = {
                                            Icon(Icons.Rounded.Search, "검색", Modifier.size(20.dp), tint = SystemGray3)
                                        },
                                        singleLine = true,
                                        colors = transparentTextFieldColors(),
                                    )
                                    if (showStoreSearch && storeQuery.isNotBlank()) {
                                        val results = stores.filter {
                                            it.storeDetailData.name.contains(storeQuery, ignoreCase = true)
                                        }.take(5)
                                        results.forEach { store ->
                                            Row(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .clickable {
                                                        selectedStore = store
                                                        storeQuery = ""
                                                        showStoreSearch = false
                                                    }
                                                    .padding(horizontal = 16.dp, vertical = 12.dp),
                                                verticalAlignment = Alignment.CenterVertically,
                                            ) {
                                                Text(store.storeDetailData.name, style = MaterialTheme.typography.bodyLarge, modifier = Modifier.weight(1f))
                                                Icon(Icons.Rounded.ChevronRight, null, Modifier.size(16.dp), tint = SystemGray3)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(12.dp))

                        if (isPriceChange && selectedStore != null) {
                            PremiumCard(modifier = Modifier.fillMaxWidth()) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Text(
                                        "기존 메뉴 선택",
                                        style = MaterialTheme.typography.labelLarge,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))

                                    if (currentMenus.isEmpty()) {
                                        Text(
                                            "선택한 가게의 메뉴 데이터가 없습니다",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = SystemGray3,
                                        )
                                    } else {
                                        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                            items(currentMenus) { menu ->
                                                val selected = selectedMenuName == menu.menuName
                                                FilterChip(
                                                    selected = selected,
                                                    onClick = {
                                                        selectedMenuName = menu.menuName
                                                        previousPrice = menu.menuPrice
                                                    },
                                                    label = { Text("${menu.menuName} (${menu.menuPrice})") },
                                                    colors = FilterChipDefaults.filterChipColors(
                                                        selectedContainerColor = MaterialTheme.colorScheme.primary,
                                                        selectedLabelColor = Color.White,
                                                    ),
                                                )
                                            }
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(10.dp))

                                    TextField(
                                        modifier = Modifier.fillMaxWidth(),
                                        value = newPrice,
                                        onValueChange = { newPrice = it },
                                        placeholder = {
                                            Text(
                                                "변경 가격 입력 (예: 8500)",
                                                style = MaterialTheme.typography.bodyLarge,
                                                color = SystemGray3,
                                            )
                                        },
                                        singleLine = true,
                                        colors = transparentTextFieldColors(),
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                        }

                        // Content + submit
                        PremiumCard(modifier = Modifier.fillMaxWidth()) {
                            Column(modifier = Modifier.padding(4.dp)) {
                                TextField(
                                    modifier = Modifier.fillMaxWidth().heightIn(min = 80.dp),
                                    value = content,
                                    onValueChange = { content = it },
                                    placeholder = {
                                        Text(
                                            text = when (selectedType) {
                                                ReportType.MENU_ADD -> "추가된 메뉴 이름과 가격"
                                                ReportType.MENU_REMOVE -> "없어진 메뉴"
                                                ReportType.PRICE_CHANGE -> "추가 메모(선택)"
                                                ReportType.CLOSED -> "폐업 관련 내용"
                                                ReportType.NEW_STORE -> "위치, 종류, 추천 메뉴 등"
                                                ReportType.OTHER -> "기타 제보 내용을 입력해주세요"
                                            },
                                            style = MaterialTheme.typography.bodyLarge,
                                            color = SystemGray3,
                                        )
                                    },
                                    colors = transparentTextFieldColors(),
                                )
                                Row(
                                    modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                ) {
                                    if (!event.isNullOrBlank()) {
                                        Text(event!!, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.primary)
                                    }
                                    Spacer(modifier = Modifier.weight(1f))
                                    FilledIconButton(
                                        onClick = { showConfirm = true },
                                        enabled = storeName.isNotBlank() &&
                                            ((isPriceChange && selectedMenuName != null && newPrice.isNotBlank()) ||
                                                (isNewStore && newStoreType != null && content.isNotBlank()) ||
                                                (!isPriceChange && !isNewStore && content.isNotBlank())) &&
                                            !isSubmitting,
                                        shape = CircleShape,
                                        modifier = Modifier.size(36.dp),
                                        colors = IconButtonDefaults.filledIconButtonColors(
                                            containerColor = MaterialTheme.colorScheme.primary,
                                            contentColor = Color.White,
                                            disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                                        ),
                                    ) {
                                        Icon(Icons.Rounded.ArrowUpward, "제출", Modifier.size(18.dp))
                                    }
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }

            // Feed section header
            item {
                SectionHeader(title = "제보 현황 (${reports.size}건)")
                Spacer(modifier = Modifier.height(4.dp))
            }

            // Report feed
            if (reports.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 40.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            "아직 제보가 없습니다",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
            } else {
                item {
                    PremiumCard(modifier = Modifier.fillMaxWidth()) {
                        Column {
                            reports.forEachIndexed { index, report ->
                                ReportFeedItem(
                                    report = report,
                                    onUpvote = { reportViewModel.upvote(report.id) },
                                    onDownvote = { reportViewModel.downvote(report.id) },
                                )
                                if (index < reports.lastIndex) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(start = 16.dp)
                                            .height(0.5.dp)
                                            .background(MaterialTheme.colorScheme.outlineVariant),
                                    )
                                }
                            }
                        }
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(72.dp)) }
        }
    }

    AppDialog(
        visible = showConfirm,
        title = "제보 등록",
        message = "${selectedType.label} 제보를 등록합니다.\n가게: $storeName",
        confirmText = "등록",
        dismissText = "취소",
        onConfirm = {
            showConfirm = false
            reportViewModel.submitReport(
                type = selectedType,
                storeId = selectedStore?.storeId,
                storeName = storeName,
                newStoreType = newStoreType,
                content = content,
                targetMenuName = selectedMenuName,
                previousPrice = previousPrice,
                newPrice = newPrice.ifBlank { null },
            )
            content = ""
            selectedStore = null
            newStoreName = ""
            newStoreType = null
            selectedMenuName = null
            previousPrice = null
            newPrice = ""
            isFormExpanded = false
        },
        onDismiss = { showConfirm = false },
    )
}

@Composable
private fun ReportFeedItem(
    report: ReportData,
    onUpvote: () -> Unit,
    onDownvote: () -> Unit,
) {
    val isUnderReview = !report.applied &&
        report.upvotes >= 10 &&
        report.type in setOf(ReportType.MENU_ADD, ReportType.MENU_REMOVE, ReportType.OTHER)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 14.dp),
    ) {
        // Header: type chip + store name + date
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // Type badge
            Text(
                text = report.type.label,
                style = MaterialTheme.typography.labelSmall,
                color = Color.White,
                modifier = Modifier
                    .background(
                        color = when (report.type) {
                            ReportType.MENU_ADD -> MaterialTheme.colorScheme.primary
                            ReportType.MENU_REMOVE -> SystemRed.copy(alpha = 0.8f)
                            ReportType.PRICE_CHANGE -> Color(0xFFFF9500)
                            ReportType.CLOSED -> SystemRed
                            ReportType.NEW_STORE -> SystemGreen
                            ReportType.OTHER -> MaterialTheme.colorScheme.onSurfaceVariant
                        },
                        shape = RoundedCornerShape(6.dp),
                    )
                    .padding(horizontal = 8.dp, vertical = 3.dp),
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                report.storeName,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.weight(1f),
            )

            // Applied badge
            if (report.applied || isUnderReview) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .background(
                            if (report.applied) SystemGreen.copy(alpha = 0.12f)
                            else Color(0xFFFF9500).copy(alpha = 0.15f),
                            RoundedCornerShape(6.dp),
                        )
                        .padding(horizontal = 8.dp, vertical = 3.dp),
                ) {
                    Icon(
                        Icons.Rounded.CheckCircle,
                        contentDescription = null,
                        modifier = Modifier.size(12.dp),
                        tint = if (report.applied) SystemGreen else Color(0xFFFF9500),
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        if (report.applied) "반영됨" else "검토중",
                        style = MaterialTheme.typography.labelSmall,
                        color = if (report.applied) SystemGreen else Color(0xFFFF9500),
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Content
        Text(
            report.content,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Footer: date + votes
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                report.date,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Spacer(modifier = Modifier.weight(1f))

            // Upvote
            IconButton(
                onClick = onUpvote,
                modifier = Modifier.size(32.dp),
            ) {
                Icon(
                    imageVector = Icons.Rounded.ThumbUp,
                    contentDescription = "추천",
                    modifier = Modifier.size(16.dp),
                    tint = if (report.upvotes > 0) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            Text(
                "${report.upvotes}",
                style = MaterialTheme.typography.labelMedium,
                color = if (report.upvotes > 0) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.onSurfaceVariant,
            )

            Spacer(modifier = Modifier.width(12.dp))

            // Downvote
            IconButton(
                onClick = onDownvote,
                modifier = Modifier.size(32.dp),
            ) {
                Icon(
                    imageVector = Icons.Rounded.ThumbDown,
                    contentDescription = "비추천",
                    modifier = Modifier.size(16.dp),
                    tint = if (report.downvotes > 0) SystemRed
                    else MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            Text(
                "${report.downvotes}",
                style = MaterialTheme.typography.labelMedium,
                color = if (report.downvotes > 0) SystemRed
                else MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
private fun transparentTextFieldColors() = TextFieldDefaults.colors(
    focusedContainerColor = Color.Transparent,
    unfocusedContainerColor = Color.Transparent,
    focusedIndicatorColor = Color.Transparent,
    unfocusedIndicatorColor = Color.Transparent,
    cursorColor = MaterialTheme.colorScheme.primary,
)
