package com.suchelin.shared.ui.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import coil3.compose.AsyncImage
import com.suchelin.shared.model.StoreData

@Composable
expect fun NativeMapView(
    stores: List<StoreData>,
    onStoreClick: (StoreData) -> Unit,
    modifier: Modifier = Modifier,
)

@Composable
expect fun WebViewScreen(url: String, modifier: Modifier = Modifier)

@Composable
expect fun BannerAd(modifier: Modifier = Modifier)

@Composable
expect fun LoadingIndicator(modifier: Modifier = Modifier)

@Composable
fun PlatformRemoteImage(
    imageUrl: String?,
    contentDescription: String,
    modifier: Modifier = Modifier,
    placeholderDrawableName: String? = null,
) {
    AsyncImage(
        model = imageUrl,
        contentDescription = contentDescription,
        modifier = modifier,
        contentScale = ContentScale.Crop,
    )
}

@Composable
expect fun PlatformDrawableIcon(
    drawableName: String,
    contentDescription: String,
    modifier: Modifier = Modifier,
    tint: Color? = null,
)

expect fun makePhoneCall(tel: String)
