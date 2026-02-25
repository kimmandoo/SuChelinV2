package com.suchelin.shared.ui.component

import android.content.Intent
import android.net.Uri
import android.webkit.WebView
import androidx.compose.foundation.Image
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.suchelin.shared.model.StoreData
import com.suchelin.shared.di.getPlatformContext

@Composable
actual fun NativeMapView(
    stores: List<StoreData>,
    onStoreClick: (StoreData) -> Unit,
    modifier: Modifier,
) {
    AndroidView(
        modifier = modifier,
        factory = { context ->
            MapView(context).apply {
                onCreate(null)
                getMapAsync { map ->
                    stores.forEach { store ->
                        val position = LatLng(store.storeDetailData.latitude, store.storeDetailData.longitude)
                        map.addMarker(MarkerOptions().position(position).title(store.storeDetailData.name))
                    }
                    val first = stores.firstOrNull()
                    if (first != null) {
                        map.moveCamera(
                            com.google.android.gms.maps.CameraUpdateFactory.newCameraPosition(
                                CameraPosition.fromLatLngZoom(
                                    LatLng(first.storeDetailData.latitude, first.storeDetailData.longitude),
                                    15f,
                                ),
                            ),
                        )
                    }
                    map.setOnMarkerClickListener { marker ->
                        val match = stores.firstOrNull { it.storeDetailData.name == marker.title }
                        if (match != null) onStoreClick(match)
                        false
                    }
                }
            }
        },
        update = { mapView ->
            mapView.getMapAsync { map ->
                map.clear()
                stores.forEach { store ->
                    val position = LatLng(store.storeDetailData.latitude, store.storeDetailData.longitude)
                    map.addMarker(MarkerOptions().position(position).title(store.storeDetailData.name))
                }
            }
        },
    )
}

@Composable
actual fun WebViewScreen(url: String, modifier: Modifier) {
    AndroidView(
        modifier = modifier,
        factory = { context ->
            WebView(context).apply {
                settings.javaScriptEnabled = true
                loadUrl(url)
            }
        },
        update = { webView -> webView.loadUrl(url) },
    )
}

@Composable
actual fun BannerAd(modifier: Modifier) {
    AndroidView(
        modifier = modifier,
        factory = { context ->
            AdView(context).apply {
                setAdSize(AdSize.BANNER)
                setAdUnitId("ca-app-pub-3940256099942544/6300978111")
                loadAd(AdRequest.Builder().build())
            }
        },
    )
}

@Composable
actual fun LoadingIndicator(modifier: Modifier) {
    CircularProgressIndicator(modifier = modifier)
}

@Composable
actual fun PlatformDrawableIcon(
    drawableName: String,
    contentDescription: String,
    modifier: Modifier,
    tint: Color?,
) {
    val context = LocalContext.current
    val drawableRes = context.resources.getIdentifier(drawableName, "drawable", context.packageName)
    if (drawableRes == 0) return

    Image(
        painter = painterResource(drawableRes),
        contentDescription = contentDescription,
        modifier = modifier,
        colorFilter = tint?.let { ColorFilter.tint(it) },
    )
}

actual fun makePhoneCall(tel: String) {
    val androidContext = getPlatformContext()
    val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$tel"))
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    androidContext.startActivity(intent)
}
