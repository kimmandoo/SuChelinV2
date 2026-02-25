package com.suchelin.shared.ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.viewinterop.UIKitView
import com.suchelin.shared.model.StoreData
import platform.CoreLocation.CLLocationCoordinate2DMake
import platform.Foundation.NSURL
import platform.Foundation.NSURLRequest
import platform.MapKit.MKMapView
import platform.MapKit.MKPointAnnotation
import platform.UIKit.UIApplication
import platform.WebKit.WKWebView

@Composable
actual fun NativeMapView(
    stores: List<StoreData>,
    onStoreClick: (StoreData) -> Unit,
    modifier: Modifier,
) {
    UIKitView(
        modifier = modifier,
        factory = {
            MKMapView().apply {
                stores.forEach { store ->
                    val annotation = MKPointAnnotation().apply {
                        setCoordinate(
                            CLLocationCoordinate2DMake(
                                store.storeDetailData.latitude,
                                store.storeDetailData.longitude,
                            ),
                        )
                        title = store.storeDetailData.name
                    }
                    addAnnotation(annotation)
                }
            }
        },
        update = { mapView ->
            mapView.removeAnnotations(mapView.annotations)
            stores.forEach { store ->
                val annotation = MKPointAnnotation().apply {
                    setCoordinate(
                        CLLocationCoordinate2DMake(
                            store.storeDetailData.latitude,
                            store.storeDetailData.longitude,
                        ),
                    )
                    title = store.storeDetailData.name
                }
                mapView.addAnnotation(annotation)
            }
        },
    )
}

@Composable
actual fun WebViewScreen(url: String, modifier: Modifier) {
    UIKitView(
        modifier = modifier,
        factory = {
            WKWebView().apply {
                NSURL(string = url)?.let { loadRequest(NSURLRequest(it)) }
            }
        },
        update = { webView ->
            NSURL(string = url)?.let { webView.loadRequest(NSURLRequest(it)) }
        },
    )
}

@Composable
actual fun BannerAd(modifier: Modifier) {
    Box(modifier = modifier) { Text("Ad") }
}

@Composable
actual fun LoadingIndicator(modifier: Modifier) {
    CircularProgressIndicator(modifier = modifier)
}

@Composable
actual fun PlatformRemoteImage(
    imageUrl: String?,
    contentDescription: String,
    modifier: Modifier,
    placeholderDrawableName: String?,
) {
    Box(modifier = modifier) {
        Text("image")
    }
}

@Composable
actual fun PlatformDrawableIcon(
    drawableName: String,
    contentDescription: String,
    modifier: Modifier,
    tint: Color?,
) {
    Box(modifier = modifier) {
        Text("icon")
    }
}

actual fun makePhoneCall(tel: String) {
    NSURL(string = "tel:$tel")?.let { url ->
        if (UIApplication.sharedApplication.canOpenURL(url)) {
            UIApplication.sharedApplication.openURL(url)
        }
    }
}
