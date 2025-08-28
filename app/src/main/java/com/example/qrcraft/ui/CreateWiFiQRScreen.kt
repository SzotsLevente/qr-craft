package com.example.qrcraft.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.qrcraft.ui.theme.OnSurface
import com.example.qrcraft.ui.theme.OnSurfaceAlt
import com.example.qrcraft.ui.theme.Primary
import com.example.qrcraft.ui.theme.Surface
import com.example.qrcraft.ui.theme.SurfaceHigher

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateWiFiQRScreen(
    onBackClick: () -> Unit,
    onQRGenerated: (String, String) -> Unit, // (content, type)
    modifier: Modifier = Modifier
) {
    var ssidInput by rememberSaveable { mutableStateOf("") }
    var passwordInput by rememberSaveable { mutableStateOf("") }
    var encryptionType by rememberSaveable { mutableStateOf("") }

    val isGenerateEnabled = ssidInput.trim().isNotEmpty()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Wi-Fi",
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
                .padding(paddingValues),
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
                    // SSID Input Field
                    TextField(
                        value = ssidInput,
                        onValueChange = { ssidInput = it },
                        label = {
                            Text(
                                "SSID",
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
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true
                    )

                    // Password Input Field
                    TextField(
                        value = passwordInput,
                        onValueChange = { passwordInput = it },
                        label = {
                            Text(
                                "Password",
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
                        visualTransformation = PasswordVisualTransformation(),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true
                    )

                    TextField(
                        value = encryptionType,
                        onValueChange = { },
                        readOnly = true,
                        label = {
                            Text(
                                "Encryption Type",
                                color = OnSurfaceAlt.copy(alpha = 0.6f),
                                style = MaterialTheme.typography.bodyLarge
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth(),
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
                        shape = RoundedCornerShape(12.dp)
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Generate Button
                Button(
                    onClick = {
                        if (isGenerateEnabled) {
                            val wifiData =
                                "WIFI:T:$encryptionType;S:${ssidInput.trim()};P:${passwordInput.trim()};;"
                            onQRGenerated(wifiData, "Wi-Fi")
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

    }
}