package com.suchelin.shared.ui.screen.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.suchelin.shared.model.StoreData
import com.suchelin.shared.ui.component.PlatformRemoteImage

@Composable
fun RandomDialog(
    visible: Boolean,
    store: StoreData?,
    onDismiss: () -> Unit,
    onOpenDetail: (StoreData) -> Unit,
) {
    if (!visible || store == null) return

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(20.dp),
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "오늘의 랜덤 추천",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.primary,
                )
                Spacer(modifier = Modifier.height(10.dp))

                PlatformRemoteImage(
                    imageUrl = store.storeDetailData.imageUrl,
                    contentDescription = store.storeDetailData.name,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(146.dp)
                        .clip(RoundedCornerShape(14.dp)),
                    placeholderDrawableName = "ic_launcher_foreground",
                )

                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = store.storeDetailData.name,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = store.storeDetailData.detail,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        },
        confirmButton = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                OutlinedButton(
                    onClick = onDismiss,
                    modifier = Modifier.weight(1f),
                ) {
                    Text("다음에")
                }
                Button(
                    onClick = { onOpenDetail(store) },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                    ),
                ) {
                    Text("상세 보기")
                }
            }
        },
        dismissButton = {},
    )
}
