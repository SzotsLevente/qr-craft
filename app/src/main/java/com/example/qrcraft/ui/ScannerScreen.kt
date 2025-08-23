package com.example.qrcraft.ui

import android.Manifest
import android.R
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.google.accompanist.permissions.*
import com.example.qrcraft.ui.theme.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.system.exitProcess
import com.google.mlkit.vision.barcode.common.Barcode

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ScannerScreen(
    onQrCodeScanned: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val cameraPermissionState = rememberPermissionState(Manifest.permission.CAMERA)
    var isLoading by remember { mutableStateOf(false) }
    var showPermissionDialog by remember { mutableStateOf(false) }
    var showPermissionGrantedSnackbar by remember { mutableStateOf(false) }
    var pendingQrCode by remember { mutableStateOf<String?>(null) }
    var previousPermissionStatus by remember { mutableStateOf<PermissionStatus?>(null) }
    val snackbarHostState = remember { SnackbarHostState() }

    // Handle permission result
    LaunchedEffect(cameraPermissionState.status) {
        val currentStatus = cameraPermissionState.status

        // Show snackbar when permission changes from denied to granted
        if (previousPermissionStatus != null &&
            previousPermissionStatus != PermissionStatus.Granted &&
            currentStatus == PermissionStatus.Granted
        ) {
            showPermissionGrantedSnackbar = true
        }

        // Handle pending QR code when permission is granted
        if (currentStatus == PermissionStatus.Granted && pendingQrCode != null) {
            showPermissionDialog = false
            // Process the pending QR code with loading
            val qrCode = pendingQrCode!!
            pendingQrCode = null
            isLoading = true
            delay(800) // Stable delay
            onQrCodeScanned(qrCode)
            isLoading = false
        }

        previousPermissionStatus = currentStatus
    }

    // Show snackbar when permission is granted
    LaunchedEffect(showPermissionGrantedSnackbar) {
        if (showPermissionGrantedSnackbar) {
            snackbarHostState.showSnackbar(
                message = "Camera permission granted",
                duration = SnackbarDuration.Short
            )
            showPermissionGrantedSnackbar = false
        }
    }

    // Check permission on app launch
    LaunchedEffect(Unit) {
        delay(50)
        if (cameraPermissionState.status != PermissionStatus.Granted) {
            showPermissionDialog = true
        }
    }

    Box(modifier = modifier
        .fillMaxSize()
        .background(Color(0xFF283037))
    ) {
        // Overlay background for areas outside camera preview
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Overlay)
        )

        // Show camera preview or loading
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            // Hint text at the top
            Text(
                text = "Point your camera at a QR code",
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 100.dp),
                color = Color.White,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center
            )

            // Camera preview without overlay elements
            CameraPreview(
                onQrCodeDetected = { barcode: Barcode ->
                    // Prevent multiple detections while processing
                    if (!isLoading && barcode.rawValue != null) {
                        if (cameraPermissionState.status == PermissionStatus.Granted) {
                            // Permission granted, process QR code with loading
                            isLoading = true
                            CoroutineScope(Dispatchers.Main).launch {
                                delay(800) // Stable delay
                                onQrCodeScanned(barcode.rawValue!!)
                                isLoading = false
                            }
                        } else {
                            // Permission not granted, store QR code and request permission
                            pendingQrCode = barcode.rawValue
                            showPermissionDialog = true
                        }
                    }
                },
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(250.dp)
            )

            // Scanning frame overlay
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(250.dp),
                contentAlignment = Alignment.Center
            ) {
                ScanningFrame(
                    modifier = Modifier.size(250.dp),
                    frameSize = 250.dp
                )
            }

            // Loading overlay - positioned over the frame
            if (isLoading) {
                LoadingOverlay()
            }
        }

        // Position snackbar higher with more padding from bottom
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 48.dp, start = 16.dp, end = 16.dp)
        ) { data ->
            Snackbar(
                containerColor = Success,
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        tint = OnSurface,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = data.visuals.message,
                        color = OnSurface,
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }
        }
    }

    // Only show dialog if permission is not granted
    val shouldShowDialog =
        showPermissionDialog && cameraPermissionState.status != PermissionStatus.Granted
    // Show permission dialog when QR code is detected without permission
    if (shouldShowDialog) {
        PermissionDialog(
            onDismiss = {
                showPermissionDialog = false
                pendingQrCode = null
            },
            onGrantAccess = {
                showPermissionDialog = false
                cameraPermissionState.launchPermissionRequest()
            },
            onCloseApp = {
                exitProcess(0)
            }
        )
    }
}

@Composable
private fun LoadingOverlay() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.7f)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CircularProgressIndicator(
                color = Color.White,
                strokeWidth = 3.dp,
                modifier = Modifier.size(40.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Processing...",
                color = Color.White,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun PermissionDialog(
    onDismiss: () -> Unit,
    onGrantAccess: () -> Unit,
    onCloseApp: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = false,
            usePlatformDefaultWidth = false
        )
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            colors = CardDefaults.cardColors(containerColor = Surface),
            shape = RoundedCornerShape(32.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Camera Required",
                    style = MaterialTheme.typography.headlineSmall,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "This app cannot function without camera access. To scan QR codes, please grant permission.",
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(
                        onClick = onCloseApp,
                        colors = ButtonDefaults.buttonColors(containerColor = SurfaceHigher)
                    ) {
                        Text("Close App", color = Error, style = MaterialTheme.typography.labelLarge)
                    }

                    Button(
                        onClick = onGrantAccess,
                        colors = ButtonDefaults.buttonColors(containerColor = SurfaceHigher)
                    ) {
                        Text("Grant Access", color = OnSurface, style = MaterialTheme.typography.labelLarge)
                    }
                }
            }
        }
    }
}