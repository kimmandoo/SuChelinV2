package com.suchelin.shared.ui.screen.list

import androidx.compose.runtime.Composable
import com.suchelin.shared.model.StoreData
import com.suchelin.shared.ui.component.AppDialog

@Composable
fun RandomDialog(
    visible: Boolean,
    store: StoreData?,
    onDismiss: () -> Unit,
    onOpenDetail: (StoreData) -> Unit,
) {
    if (!visible || store == null) return

    AppDialog(
        visible = true,
        title = "오늘 뭐 먹지",
        message = "${store.storeDetailData.name}\n${store.storeDetailData.detail}",
        confirmText = "상세 보기",
        dismissText = "닫기",
        onConfirm = { onOpenDetail(store) },
        onDismiss = onDismiss,
    )
}
