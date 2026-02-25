package com.suchelin.shared.ui.screen.mail

import androidx.compose.runtime.Composable
import com.suchelin.shared.ui.component.AppDialog

@Composable
fun SendMailDialog(
    visible: Boolean,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
) {
    AppDialog(
        visible = visible,
        title = "문의하기",
        message = "메일 앱을 열어 SuChelin 팀에 문의합니다.",
        confirmText = "열기",
        dismissText = "취소",
        onConfirm = onConfirm,
        onDismiss = onDismiss,
    )
}
