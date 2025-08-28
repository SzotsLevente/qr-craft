package com.example.qrcraft.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.qrcraft.ui.theme.*
import com.example.qrcraft.R

data class QRType(
    val name: String,
    val icon: Int,
    val backgroundColor: Color,
    val iconColor: Color
)

@Composable
fun CreateQRScreen(
    modifier: Modifier = Modifier,
    onQRTypeSelected: (String) -> Unit = {}
) {
    val qrTypes = listOf(
        QRType("Text", R.drawable.type_01, TextBG, Text),
        QRType("Link", R.drawable.link_01, LinkBG, Link),
        QRType("Contact", R.drawable.user_03, ContactBG, Contact),
        QRType("Phone Number", R.drawable.phone, PhoneBG, Phone),
        QRType("Geolocation", R.drawable.marker_pin_06, GeoBG, Geo),
        QRType("Wi-Fi", R.drawable.icon__3_, WiFiBG, WiFi)
    )

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val columnCount = if (screenWidth < 600.dp) 2 else 3

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Surface)
            .padding(horizontal = 24.dp)
    ) {
        Spacer(modifier = Modifier.height(60.dp))

        Text(
            text = "Create QR",
            style = MaterialTheme.typography.titleMedium,
            color = OnSurface,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(32.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(columnCount),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(qrTypes) { qrType ->
                QRTypeCard(
                    qrType = qrType,
                    onClick = { onQRTypeSelected(qrType.name) }
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))
    }
}

@Composable
fun QRTypeCard(
    qrType: QRType,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceHigher),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
                .padding(vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .background(
                        color = qrType.backgroundColor,
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource( qrType.icon),
                    contentDescription = null,
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = qrType.name,
                style = MaterialTheme.typography.titleSmall,
                color = OnSurface
            )
        }
    }
}