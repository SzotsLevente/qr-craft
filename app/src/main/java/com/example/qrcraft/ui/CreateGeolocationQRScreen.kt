package com.example.qrcraft.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.qrcraft.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateGeolocationQRScreen(
    onBackClick: () -> Unit,
    onQRGenerated: (String, String) -> Unit, // (content, type)
    modifier: Modifier = Modifier
) {
    var latitudeInput by rememberSaveable { mutableStateOf("") }
    var longitudeInput by rememberSaveable { mutableStateOf("") }

    val isGenerateEnabled = latitudeInput.trim().isNotEmpty() &&
            longitudeInput.trim().isNotEmpty()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Geolocation",
                            style = MaterialTheme.typography.titleMedium,
                            color = OnSurface
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = OnSurface
                        )
                    }
                },
                actions = {
                    Spacer(modifier = Modifier.width(48.dp))
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Surface
                )
            )
        },
        modifier = modifier.fillMaxSize()
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Surface)
                .padding(paddingValues)
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Content Card
            Card(
                modifier = Modifier
                    .width(480.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceHigher),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Latitude Input Field
                    TextField(
                        value = latitudeInput,
                        onValueChange = { latitudeInput = it },
                        label = {
                            Text(
                                "Latitude",
                                color = OnSurfaceAlt.copy(alpha = 0.6f),
                                style = MaterialTheme.typography.bodyLarge
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Surface,
                            unfocusedContainerColor = Surface,
                            focusedIndicatorColor = androidx.compose.ui.graphics.Color.Transparent,
                            unfocusedIndicatorColor = androidx.compose.ui.graphics.Color.Transparent,
                            focusedLabelColor = OnSurfaceAlt.copy(alpha = 0.6f),
                            unfocusedLabelColor = OnSurfaceAlt.copy(alpha = 0.6f),
                            cursorColor = OnSurface,
                            focusedTextColor = OnSurface,
                            unfocusedTextColor = OnSurface
                        ),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        placeholder = {
                            Text(
                                "37.7749",
                                color = OnSurfaceAlt.copy(alpha = 0.4f),
                                style = MaterialTheme.typography.bodyLarge
                            )
                        },
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true
                    )

                    // Longitude Input Field
                    TextField(
                        value = longitudeInput,
                        onValueChange = { longitudeInput = it },
                        label = {
                            Text(
                                "Longitude",
                                color = OnSurfaceAlt.copy(alpha = 0.6f),
                                style = MaterialTheme.typography.bodyLarge
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Surface,
                            unfocusedContainerColor = Surface,
                            focusedIndicatorColor = androidx.compose.ui.graphics.Color.Transparent,
                            unfocusedIndicatorColor = androidx.compose.ui.graphics.Color.Transparent,
                            focusedLabelColor = OnSurfaceAlt.copy(alpha = 0.6f),
                            unfocusedLabelColor = OnSurfaceAlt.copy(alpha = 0.6f),
                            cursorColor = OnSurface,
                            focusedTextColor = OnSurface,
                            unfocusedTextColor = OnSurface
                        ),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        placeholder = {
                            Text(
                                "-122.4194",
                                color = OnSurfaceAlt.copy(alpha = 0.4f),
                                style = MaterialTheme.typography.bodyLarge
                            )
                        },
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    // Generate Button
                    Button(
                        onClick = {
                            if (isGenerateEnabled) {
                                val geoData = "geo:${latitudeInput.trim()},${longitudeInput.trim()}"
                                onQRGenerated(geoData, "Geolocation")
                            }
                        },
                        enabled = isGenerateEnabled,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isGenerateEnabled) Primary else OnSurfaceAlt.copy(
                                alpha = 0.2f
                            ),
                            contentColor = if (isGenerateEnabled) OnSurface else OnSurfaceAlt.copy(
                                alpha = 0.6f
                            ),
                            disabledContainerColor = OnSurfaceAlt.copy(alpha = 0.2f),
                            disabledContentColor = OnSurfaceAlt.copy(alpha = 0.6f)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = "Generate QR-Code",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))
        }
    }
}