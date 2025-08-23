package com.example.qrcraft.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.unit.sp
import com.example.qrcraft.R

// Google Font provider
val provider = GoogleFont.Provider(
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage = "com.google.android.gms",
    certificates = R.array.com_google_android_gms_fonts_certs
)

// SUSE Google Font
val suseFont = GoogleFont("SUSE")

// SUSE Font Family using Google Fonts
val SuseFamily = FontFamily(
    Font(googleFont = suseFont, fontProvider = provider, weight = FontWeight.Normal),
    Font(googleFont = suseFont, fontProvider = provider, weight = FontWeight.Medium),
    Font(googleFont = suseFont, fontProvider = provider, weight = FontWeight.SemiBold)
)

// Custom Typography based on SUSE specifications
val Typography = Typography(
    // Title Medium - SUSE SemiBold 28/32
    headlineMedium = TextStyle(
        fontFamily = SuseFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 28.sp,
        lineHeight = 32.sp
    ),

    // Title Small - SUSE SemiBold 19/24  
    headlineSmall = TextStyle(
        fontFamily = SuseFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 19.sp,
        lineHeight = 24.sp
    ),

    // Label Large - SUSE Medium 16/20
    labelLarge = TextStyle(
        fontFamily = SuseFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = 20.sp
    ),

    // Body Large - SUSE Regular 16/20
    bodyLarge = TextStyle(
        fontFamily = SuseFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 20.sp
    ),

    // Additional common styles using SUSE
    bodyMedium = TextStyle(
        fontFamily = SuseFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 18.sp
    ),

    labelMedium = TextStyle(
        fontFamily = SuseFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 18.sp
    )
)