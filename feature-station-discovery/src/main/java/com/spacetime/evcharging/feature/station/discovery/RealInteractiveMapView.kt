package com.spacetime.evcharging.feature.station.discovery

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.webkit.JavascriptInterface
import android.webkit.WebChromeClient
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.spacetime.evcharging.core.common.model.ChargerStatus
import com.spacetime.evcharging.data.model.Station
import org.json.JSONArray
import org.json.JSONObject
import timber.log.Timber

/**
 * JavaScript interface bridge for bidirectional communication between the Leaflet map and Compose.
 */
class MapWebBridge(
    private val onStationSelected: (String) -> Unit
) {
    @JavascriptInterface
    fun onStationClicked(stationId: String) {
        onStationSelected(stationId)
    }
}

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun RealInteractiveMapView(
    stations: List<Station>,
    selectedStation: Station?,
    userLatitude: Double,
    userLongitude: Double,
    onSelectStation: (Station) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val webView = remember {
        WebView(context).apply {
            settings.apply {
                javaScriptEnabled = true
                domStorageEnabled = true
                databaseEnabled = true
                allowFileAccess = true
                allowContentAccess = true
                cacheMode = WebSettings.LOAD_DEFAULT
                loadWithOverviewMode = true
                useWideViewPort = true
                displayZoomControls = false
                builtInZoomControls = true
                setSupportZoom(true)
            }
            webChromeClient = WebChromeClient()
            webViewClient = object : WebViewClient() {
                override fun onReceivedError(view: WebView?, request: WebResourceRequest?, error: WebResourceError?) {
                    Timber.w("WebView map resource error: ${error?.description}")
                }
            }
        }
    }

    DisposableEffect(webView) {
        val bridge = MapWebBridge { stationId ->
            val found = stations.find { it.id == stationId }
            if (found != null) {
                webView.post { onSelectStation(found) }
            }
        }
        webView.addJavascriptInterface(bridge, "AndroidBridge")
        onDispose {
            webView.removeJavascriptInterface("AndroidBridge")
        }
    }

    // Load or update HTML content with null base URL so CORS doesn't block local scripts
    LaunchedEffect(stations, userLatitude, userLongitude) {
        val html = buildMapHtml(stations, userLatitude, userLongitude)
        webView.loadDataWithBaseURL(null, html, "text/html", "UTF-8", null)
    }

    // Fly to selected station when changed
    LaunchedEffect(selectedStation) {
        selectedStation?.let { s ->
            val js = "javascript:if(window.focusStation){window.focusStation(${s.latitude}, ${s.longitude}, '${s.id}');}"
            webView.evaluateJavascript(js, null)
        }
    }

    AndroidView(
        factory = { webView },
        modifier = modifier.fillMaxSize()
    )
}

/**
 * Generates an embedded Leaflet HTML with Carto tiles and an automatic fallback
 * vector map so the user NEVER sees a blank screen if external CDNs are unavailable.
 */
private fun buildMapHtml(
    stations: List<Station>,
    userLat: Double,
    userLng: Double
): String {
    val stationsArray = JSONArray().apply {
        stations.forEach { s ->
            put(JSONObject().apply {
                put("id", s.id)
                put("name", s.name)
                put("address", s.address)
                put("lat", s.latitude)
                put("lng", s.longitude)
                put("avail", s.availableConnectors)
                put("total", s.totalConnectors)
                put("price", s.minimumTariff.displayAmount)
                put("status", if (s.availableConnectors > 0 && s.availability == ChargerStatus.AVAILABLE) "AVAILABLE" else "BUSY")
            })
        }
    }

    return """
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no" />
    <link rel="stylesheet" href="https://unpkg.com/leaflet@1.9.4/dist/leaflet.css" crossorigin="" />
    <script src="https://unpkg.com/leaflet@1.9.4/dist/leaflet.js" crossorigin=""></script>
    <style>
        * { margin: 0; padding: 0; box-sizing: border-box; }
        body, html, #map { width: 100%; height: 100%; overflow: hidden; font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif; background: #eef2f5; }
        
        .station-pin {
            position: relative;
            cursor: pointer;
            transition: transform 0.2s cubic-bezier(0.175, 0.885, 0.32, 1.275);
        }
        .station-pin:active, .station-pin.selected {
            transform: scale(1.15);
        }
        .pin-pill {
            display: inline-flex;
            align-items: center;
            background: #FFFFFF;
            padding: 5px 9px;
            border-radius: 18px;
            box-shadow: 0 4px 14px rgba(16, 42, 67, 0.22);
            border: 2px solid #087F5B;
            font-size: 11px;
            font-weight: 700;
            color: #102A43;
            white-space: nowrap;
        }
        .pin-pill.busy {
            border-color: #F59E0B;
        }
        .pin-pill.selected {
            background: #102A43;
            color: #FFFFFF;
            border-color: #087F5B;
        }
        .pin-dot {
            width: 8px;
            height: 8px;
            border-radius: 50%;
            background: #16A34A;
            margin-right: 5px;
            display: inline-block;
        }
        .pin-dot.busy { background: #F59E0B; }
        .user-dot {
            width: 20px;
            height: 20px;
            border-radius: 50%;
            background: #2563EB;
            border: 3px solid #FFFFFF;
            box-shadow: 0 0 0 4px rgba(37, 99, 235, 0.35);
            animation: pulse 2s infinite;
        }
        @keyframes pulse {
            0% { box-shadow: 0 0 0 0 rgba(37, 99, 235, 0.5); }
            70% { box-shadow: 0 0 0 14px rgba(37, 99, 235, 0); }
            100% { box-shadow: 0 0 0 0 rgba(37, 99, 235, 0); }
        }
        #fallback-canvas {
            display: none;
            width: 100%;
            height: 100%;
        }
    </style>
</head>
<body>
    <div id="map"></div>
    <canvas id="fallback-canvas"></canvas>

    <script>
        var userLat = $userLat;
        var userLng = $userLng;
        var stations = $stationsArray;

        // Check if Leaflet successfully loaded
        if (typeof L !== 'undefined') {
            try {
                var map = L.map('map', {
                    zoomControl: false,
                    attributionControl: false
                }).setView([userLat, userLng], 13);

                L.tileLayer('https://{s}.basemaps.cartocdn.com/rastertiles/voyager/{z}/{x}/{y}{r}.png', {
                    maxZoom: 19,
                    subdomains: 'abcd',
                    fallback: 'https://tile.openstreetmap.org/{z}/{x}/{y}.png'
                }).addTo(map);

                // User Location
                var userIcon = L.divIcon({
                    className: 'user-pin-wrapper',
                    html: '<div class="user-dot"></div>',
                    iconSize: [20, 20],
                    iconAnchor: [10, 10]
                });
                L.marker([userLat, userLng], { icon: userIcon, zIndexOffset: 1000 }).addTo(map);

                // Stations
                stations.forEach(function(s) {
                    var isAvail = s.status === 'AVAILABLE';
                    var pillClass = isAvail ? 'pin-pill' : 'pin-pill busy';
                    var dotClass = isAvail ? 'pin-dot' : 'pin-dot busy';
                    var badgeText = s.price + ' • ' + s.avail + '/' + s.total;

                    var pinHtml = '<div class="station-pin" id="pin_' + s.id + '">' +
                        '<div class="' + pillClass + '">' +
                            '<span class="' + dotClass + '"></span>' +
                            '<span>' + badgeText + '</span>' +
                        '</div>' +
                    '</div>';

                    var icon = L.divIcon({
                        className: 'station-marker-div',
                        html: pinHtml,
                        iconSize: [120, 36],
                        iconAnchor: [60, 18]
                    });

                    var marker = L.marker([s.lat, s.lng], { icon: icon }).addTo(map);
                    marker.on('click', function() {
                        if (window.AndroidBridge) {
                            window.AndroidBridge.onStationClicked(s.id);
                        }
                    });
                });

                window.focusStation = function(lat, lng, id) {
                    map.flyTo([lat, lng], 15, { animate: true, duration: 0.8 });
                };
            } catch(e) {
                renderFallbackCanvas();
            }
        } else {
            renderFallbackCanvas();
        }

        function renderFallbackCanvas() {
            document.getElementById('map').style.display = 'none';
            var canvas = document.getElementById('fallback-canvas');
            canvas.style.display = 'block';
            var ctx = canvas.getContext('2d');
            canvas.width = window.innerWidth;
            canvas.height = window.innerHeight;

            ctx.fillStyle = '#f0f4f8';
            ctx.fillRect(0, 0, canvas.width, canvas.height);

            // Draw grid & rings
            ctx.strokeStyle = '#d6dfe6';
            ctx.lineWidth = 1.5;
            var cx = canvas.width / 2;
            var cy = canvas.height / 2;
            [60, 140, 220].forEach(function(r) {
                ctx.beginPath();
                ctx.arc(cx, cy, r, 0, 2 * Math.PI);
                ctx.stroke();
            });

            // User pin
            ctx.fillStyle = '#2563eb';
            ctx.beginPath();
            ctx.arc(cx, cy, 8, 0, 2 * Math.PI);
            ctx.fill();

            // Station pins
            stations.forEach(function(s, idx) {
                var dx = (s.lng - userLng) * 1800;
                var dy = -(s.lat - userLat) * 1800;
                var sx = cx + dx;
                var sy = cy + dy;

                ctx.fillStyle = s.status === 'AVAILABLE' ? '#087F5B' : '#F59E0B';
                ctx.beginPath();
                ctx.roundRect(sx - 45, sy - 15, 90, 30, 15);
                ctx.fill();

                ctx.fillStyle = '#ffffff';
                ctx.font = 'bold 11px sans-serif';
                ctx.textAlign = 'center';
                ctx.fillText(s.price + ' • ' + s.avail + '/' + s.total, sx, sy + 4);
            });
        }
    </script>
</body>
</html>
    """.trimIndent()
}
