package com.example.qrcraft

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.qrcraft.ui.BottomNavigation
import com.example.qrcraft.ui.CreateContactQRScreen
import com.example.qrcraft.ui.CreateGeolocationQRScreen
import com.example.qrcraft.ui.CreateLinkQRScreen
import com.example.qrcraft.ui.CreatePhoneQRScreen
import com.example.qrcraft.ui.CreateQRScreen
import com.example.qrcraft.ui.CreateTextQRScreen
import com.example.qrcraft.ui.CreateWiFiQRScreen
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
    var scannedQrData by rememberSaveable { mutableStateOf("") }
    var createdQrContent by rememberSaveable { mutableStateOf("") }
    var createdQrType by rememberSaveable { mutableStateOf("") }
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route ?: "splash"

    // Routes that should show bottom navigation
    val routesWithBottomNav = setOf("scanner", "create")
    val shouldShowBottomNav = currentRoute in routesWithBottomNav

    Box(modifier = Modifier.fillMaxSize()) {
        // Main content - takes full screen
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

            composable("create") {
                CreateQRScreen(
                    onQRTypeSelected = { qrType ->
                        when (qrType) {
                            "Text" -> navController.navigate("create_text")
                            "Link" -> navController.navigate("create_link")
                            "Contact" -> navController.navigate("create_contact")
                            "Phone Number" -> navController.navigate("create_phone")
                            "Geolocation" -> navController.navigate("create_geolocation")
                            "Wi-Fi" -> navController.navigate("create_wifi")
                        }
                    }
                )
            }

            composable("create_text") {
                CreateTextQRScreen(
                    onBackClick = { navController.navigateUp() },
                    onQRGenerated = { content, type ->
                        createdQrContent = content
                        createdQrType = type
                        navController.navigate("qr_preview")
                    }
                )
            }

            composable("create_link") {
                CreateLinkQRScreen(
                    onBackClick = { navController.navigateUp() },
                    onQRGenerated = { content, type ->
                        createdQrContent = content
                        createdQrType = type
                        navController.navigate("qr_preview")
                    }
                )
            }

            composable("create_contact") {
                CreateContactQRScreen(
                    onBackClick = { navController.navigateUp() },
                    onQRGenerated = { content, type ->
                        createdQrContent = content
                        createdQrType = type
                        navController.navigate("qr_preview")
                    }
                )
            }

            composable("create_phone") {
                CreatePhoneQRScreen(
                    onBackClick = { navController.navigateUp() },
                    onQRGenerated = { content, type ->
                        createdQrContent = content
                        createdQrType = type
                        navController.navigate("qr_preview")
                    }
                )
            }

            composable("create_geolocation") {
                CreateGeolocationQRScreen(
                    onBackClick = { navController.navigateUp() },
                    onQRGenerated = { content, type ->
                        createdQrContent = content
                        createdQrType = type
                        navController.navigate("qr_preview")
                    }
                )
            }

            composable("create_wifi") {
                CreateWiFiQRScreen(
                    onBackClick = { navController.navigateUp() },
                    onQRGenerated = { content, type ->
                        createdQrContent = content
                        createdQrType = type
                        navController.navigate("qr_preview")
                    }
                )
            }

            composable("qr_preview") {
                ScanResultScreen(
                    qrCodeData = createdQrContent,
                    title = "Preview",
                    onBackClick = { navController.navigateUp() }
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

        if (shouldShowBottomNav) {
            BottomNavigation(
                currentRoute = currentRoute,
                onNavigate = { route ->
                    navController.navigate(route) {
                        launchSingleTop = true
                        // Clear back stack when navigating between main screens
                        if (route in routesWithBottomNav) {
                            popUpTo("scanner") { inclusive = false }
                        }
                    }
                },
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }
    }
}