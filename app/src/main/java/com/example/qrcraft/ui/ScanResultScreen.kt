package com.example.qrcraft.ui

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color.BLACK
import android.graphics.Color.WHITE
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.qrcraft.data.QrContentDetector
import com.example.qrcraft.data.QrContentType
import com.example.qrcraft.ui.theme.OnOverlay
import com.example.qrcraft.ui.theme.OnSurface
import com.example.qrcraft.ui.theme.OnSurfaceAlt
import com.example.qrcraft.ui.theme.Surface
import com.example.qrcraft.ui.theme.SurfaceHigher
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.qrcode.QRCodeWriter
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScanResultScreen(
    qrCodeData: String,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current

    // Handle hardware back button
    BackHandler {
        onBackClick()
    }

    val qrContent = remember(qrCodeData) {
        QrContentDetector.detectContentTypeFromString(qrCodeData)
    }

    // Generate QR code bitmap
    val qrCodeBitmap = remember(qrCodeData) {
        generateQrCodeBitmap(qrCodeData)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF283037))
    ) {
        // Top Bar
        TopAppBar(
            title = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Scan Result",
                        style = MaterialTheme.typography.headlineMedium,
                        color = OnOverlay,
                        textAlign = TextAlign.Center
                    )
                }
            },
            navigationIcon = {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = OnOverlay
                    )
                }
            },
            actions = {
                // Empty spacer to balance the navigation icon and center the title
                Spacer(modifier = Modifier.width(48.dp)) // Same width as IconButton
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = OnSurface
            )
        )

        // Scrollable content
        val scrollState = rememberScrollState()

        Box(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(bottom = 32.dp)
        ) {
            // Content Card
            Card(
                modifier = Modifier
                    .width(480.dp)
                    .align(Alignment.TopCenter)
                    .padding(horizontal = 16.dp)
                    .padding(top = 140.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                colors = CardDefaults.cardColors(containerColor = Surface),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(
                        top = 80.dp,
                        start = 24.dp,
                        end = 24.dp,
                        bottom = 24.dp
                    ),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = qrContent.displayName,
                        style = MaterialTheme.typography.headlineMedium,
                        color = OnSurface,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    if (qrContent.formattedData.isNotEmpty()) {
                        FormattedContentDisplay(qrContent.formattedData, qrContent.type)
                    } else {
                        ExpandableText(
                            text = qrCodeData,
                            maxLines = 6,
                            style = MaterialTheme.typography.bodyLarge,
                            color = OnSurface,
                            textAlign = if (qrContent.type == QrContentType.Text) TextAlign.Start else TextAlign.Center
                        )
                    }

                    Spacer(modifier = Modifier.height(40.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Button(
                            onClick = { shareContent(context, qrCodeData) },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = SurfaceHigher)
                        ) {
                            Icon(Icons.Default.Share, null, Modifier.size(16.dp))
                            Spacer(Modifier.width(8.dp))
                            Text(
                                "Share",
                                color = OnSurface,
                                style = MaterialTheme.typography.labelLarge
                            )
                        }

                        Button(
                            onClick = {
                                clipboardManager.setText(AnnotatedString(qrCodeData))
                                Toast.makeText(
                                    context,
                                    "Copied to clipboard",
                                    Toast.LENGTH_SHORT
                                ).show()
                            },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = SurfaceHigher)
                        ) {
                            Text(
                                "Copy",
                                color = OnSurface,
                                style = MaterialTheme.typography.labelLarge
                            )
                        }
                    }
                }
            }

            // QR Code positioned above card
            qrCodeBitmap?.let { bitmap ->
                Box(
                    modifier = Modifier
                        .size(160.dp)
                        .align(Alignment.TopCenter)
                        .padding(top = 100.dp)
                        .aspectRatio(1f)
                        .background(
                            color = Surface,
                            shape = RoundedCornerShape(16.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        bitmap = bitmap.asImageBitmap(),
                        contentDescription = "QR Code",
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(16.dp))
                    )
                }
            }
        }
    }
}

@Composable
private fun FormattedContentDisplay(data: Map<String, String>, contentType: QrContentType) {
    val context = LocalContext.current
    val textAlign = if (contentType == QrContentType.Text) TextAlign.Start else TextAlign.Center
    val horizontalAlignment =
        if (contentType == QrContentType.Text) Alignment.Start else Alignment.CenterHorizontally

    Column(
        horizontalAlignment = horizontalAlignment,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        data.forEach { (key, value) ->
            if (value.isNotEmpty()) {
                if (contentType == QrContentType.Link) {
                    Text(
                        text = "$value",
                        style = MaterialTheme.typography.bodyLarge,
                        color = OnSurface,
                        textAlign = textAlign,
                        modifier = Modifier.clickable {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(value.trim()))
                            context.startActivity(intent)
                        }
                    )
                } else {
                    Text(
                        text = if (key.isEmpty()) value else "$key: $value",
                        style = MaterialTheme.typography.bodyLarge,
                        color = OnSurface,
                        textAlign = textAlign
                    )
                }
            }
        }
    }
}

@Composable
private fun ExpandableText(
    text: String,
    maxLines: Int,
    style: androidx.compose.ui.text.TextStyle,
    color: Color,
    textAlign: TextAlign
) {
    var isExpanded by remember { mutableStateOf(false) }
    var showReadMoreButton by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .animateContentSize(animationSpec = tween(durationMillis = 300))
    ) {
        Text(
            text = text,
            style = style,
            color = color,
            textAlign = textAlign,
            maxLines = if (isExpanded) Int.MAX_VALUE else maxLines,
            onTextLayout = { textLayoutResult ->
                if (textLayoutResult.hasVisualOverflow) {
                    showReadMoreButton = true
                }
            }
        )

        if (showReadMoreButton) {
            Text(
                text = if (isExpanded) "Show less" else "Show more",
                style = MaterialTheme.typography.labelLarge,
                color = OnSurfaceAlt,
                textAlign = textAlign,
                modifier = Modifier
                    .padding(top = 8.dp)
                    .clickable { isExpanded = !isExpanded }
            )
        }
    }
}

private fun generateQrCodeBitmap(data: String, size: Int = 400): Bitmap? {
    return try {
        val writer = QRCodeWriter()
        val hints = hashMapOf<EncodeHintType, Any>().apply {
            put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M)
            put(EncodeHintType.MARGIN, 2) // Increase margin for better appearance
        }

        val bitMatrix = writer.encode(data, BarcodeFormat.QR_CODE, size, size, hints)
        val width = bitMatrix.width
        val height = bitMatrix.height
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888) // Better quality

        for (x in 0 until width) {
            for (y in 0 until height) {
                bitmap.setPixel(x, y, if (bitMatrix[x, y]) BLACK else WHITE)
            }
        }
        bitmap
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

private fun shareContent(context: Context, content: String) {
    val shareIntent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, content)
        type = "text/plain"
    }

    val chooser = Intent.createChooser(shareIntent, "Share QR Code Content")
    context.startActivity(chooser)
}