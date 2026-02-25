package com.suchelin.shared.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import androidx.compose.ui.viewinterop.UIKitView
import com.suchelin.shared.model.StoreData
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned
import org.jetbrains.skia.Image as SkiaImage
import platform.CoreGraphics.CGRectMake
import platform.CoreGraphics.CGSizeMake
import platform.CoreLocation.CLLocationCoordinate2DMake
import platform.Foundation.NSData
import platform.Foundation.NSURL
import platform.Foundation.NSURLRequest
import platform.MapKit.MKMapView
import platform.MapKit.MKPointAnnotation
import platform.UIKit.UIApplication
import platform.UIKit.UIGraphicsBeginImageContextWithOptions
import platform.UIKit.UIGraphicsEndImageContext
import platform.UIKit.UIGraphicsGetImageFromCurrentImageContext
import platform.UIKit.UIImage
import platform.UIKit.UIImagePNGRepresentation
import platform.WebKit.WKWebView
import platform.posix.memcpy

@OptIn(ExperimentalForeignApi::class)
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
                    val annotation = MKPointAnnotation()
                    annotation.setCoordinate(
                        CLLocationCoordinate2DMake(
                            store.storeDetailData.latitude,
                            store.storeDetailData.longitude,
                        ),
                    )
                    annotation.setTitle(store.storeDetailData.name)
                    addAnnotation(annotation)
                }
            }
        },
        update = { mapView ->
            mapView.removeAnnotations(mapView.annotations)
            stores.forEach { store ->
                val annotation = MKPointAnnotation()
                annotation.setCoordinate(
                    CLLocationCoordinate2DMake(
                        store.storeDetailData.latitude,
                        store.storeDetailData.longitude,
                    ),
                )
                annotation.setTitle(store.storeDetailData.name)
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

private fun mapDrawableToSFSymbol(drawableName: String): String {
    return when (drawableName) {
        "bxs_food_menu" -> "fork.knife"
        "bx_question" -> "questionmark.circle"
        else -> "questionmark.circle"
    }
}

@OptIn(ExperimentalForeignApi::class)
private fun uiImageToImageBitmap(uiImage: UIImage): ImageBitmap? {
    val size = CGSizeMake(24.0, 24.0)
    UIGraphicsBeginImageContextWithOptions(size, false, 0.0)
    uiImage.drawInRect(CGRectMake(0.0, 0.0, 24.0, 24.0))
    val renderedImage = UIGraphicsGetImageFromCurrentImageContext()
    UIGraphicsEndImageContext()

    val pngData: NSData = renderedImage?.let { UIImagePNGRepresentation(it) } ?: return null
    val bytes = ByteArray(pngData.length.toInt())
    bytes.usePinned { pinned ->
        memcpy(pinned.addressOf(0), pngData.bytes, pngData.length)
    }
    return SkiaImage.makeFromEncoded(bytes).toComposeImageBitmap()
}

@Composable
actual fun PlatformDrawableIcon(
    drawableName: String,
    contentDescription: String,
    modifier: Modifier,
    tint: Color?,
) {
    val sfSymbolName = mapDrawableToSFSymbol(drawableName)
    val bitmap = remember(sfSymbolName) {
        val uiImage = UIImage.systemImageNamed(sfSymbolName)
        uiImage?.let { uiImageToImageBitmap(it) }
    }
    if (bitmap != null) {
        Image(
            bitmap = bitmap,
            contentDescription = contentDescription,
            modifier = modifier,
            colorFilter = tint?.let { ColorFilter.tint(it) },
        )
    }
}

actual fun makePhoneCall(tel: String) {
    NSURL(string = "tel:$tel")?.let { url ->
        if (UIApplication.sharedApplication.canOpenURL(url)) {
            UIApplication.sharedApplication.openURL(url)
        }
    }
}
