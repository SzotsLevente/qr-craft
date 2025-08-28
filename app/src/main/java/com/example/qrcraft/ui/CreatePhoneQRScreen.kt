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
fun CreatePhoneQRScreen(
    onBackClick: () -> Unit,
    onQRGenerated: (String, String) -> Unit, // (content, type)
    modifier: Modifier = Modifier
) {
    var phoneInput by rememberSaveable { mutableStateOf("") }
    val isGenerateEnabled = phoneInput.trim().isNotEmpty()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Phone Number",
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
                        .padding(16.dp)
                ) {
                    // Phone Number Input Field
                    TextField(
                        value = phoneInput,
                        onValueChange = { phoneInput = it },
                        label = {
                            Text(
                                "Phone Number",
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
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Generate Button
                    Button(
                        onClick = {
                            if (isGenerateEnabled) {
                                val phoneData = "tel:${phoneInput.trim()}"
                                onQRGenerated(phoneData, "Phone Number")
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