package com.example.qrcraft

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.qrcraft.ui.ScanResultScreen
import com.example.qrcraft.ui.ScannerScreen
import com.example.qrcraft.ui.SplashScreen
import com.example.qrcraft.ui.theme.QrCraftTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            QrCraftTheme {
                QrCraftApp()
            }
        }
    }
}

@Composable
fun QrCraftApp() {
    val navController = rememberNavController()
    var scannedQrData by remember { mutableStateOf("") }

    NavHost(
        navController = navController,
        startDestination = "splash",
        modifier = Modifier.fillMaxSize()
    ) {
        composable("splash") {
            SplashScreen(
                onSplashFinished = {
                    navController.navigate("scanner") {
                        popUpTo("splash") { inclusive = true }
                    }
                }
            )
        }

        composable("scanner") {
            ScannerScreen(
                onQrCodeScanned = { qrCode ->
                    scannedQrData = qrCode
                    navController.navigate("result")
                }
            )
        }

        composable("result") {
            ScanResultScreen(
                qrCodeData = scannedQrData,
                onBackClick = {
                    navController.navigate("scanner") {
                        popUpTo("scanner") { inclusive = false }
                    }
                }
            )
        }
    }
}