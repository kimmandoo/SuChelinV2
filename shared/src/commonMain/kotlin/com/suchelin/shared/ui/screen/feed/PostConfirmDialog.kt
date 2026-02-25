package com.suchelin.shared.ui.screen.feed

import androidx.compose.runtime.Composable
import com.suchelin.shared.ui.component.AppDialog

@Composable
fun PostConfirmDialog(
    visible: Boolean,
    onConfirm: () -> Unit,
    onCancel: () -> Unit,
) {
    AppDialog(
        visible = visible,
        title = "게시글 등록",
        message = "하루 한 번만 글을 등록할 수 있고, 부적절한 내용은 삭제될 수 있습니다.",
        confirmText = "등록",
        dismissText = "취소",
        onConfirm = onConfirm,
        onDismiss = onCancel,
    )
}
