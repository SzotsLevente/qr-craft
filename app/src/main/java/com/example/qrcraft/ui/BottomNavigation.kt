package com.example.qrcraft.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.AddCircle
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.qrcraft.R
import com.example.qrcraft.ui.theme.OnSurface
import com.example.qrcraft.ui.theme.OnSurfaceAlt
import com.example.qrcraft.ui.theme.Primary
import com.example.qrcraft.ui.theme.SurfaceHigher

@Composable
fun BottomNavigation(
    currentRoute: String,
    onNavigate: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    // Container with rounded background
    Box(
        modifier = modifier.padding(bottom = 24.dp),
        contentAlignment = Alignment.Center
    ) {
        // Rounded background container
        Box(
            modifier = Modifier
                .background(
                    color = SurfaceHigher,
                    shape = RoundedCornerShape(36.dp)
                )
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                // History button (left)
                CircularNavButton(
                    icon = R.drawable.clock_refresh,
                    isActive = false,
                    isLarge = false,
                    onClick = { /* Do nothing - inactive in this milestone */ }
                )

                // Spacer for the large scan button
                Box(modifier = Modifier.size(44.dp))

                // Create QR button (right)
                CircularNavButton(
                    icon = R.drawable.add_circle,
                    isActive = true,
                    isLarge = false,
                    onClick = { onNavigate("create") }
                )
            }
        }

        // Scan button (center) - positioned on top to extend beyond the background
        CircularNavButton(
            icon = R.drawable.scan,
            isActive = true,
            isLarge = true,
            onClick = { onNavigate("scanner") }
        )
    }
}

@Composable
fun CircularNavButton(
    icon: Int,
    isActive: Boolean,
    isLarge: Boolean,
    onClick: () -> Unit
) {
    val buttonSize = if (isLarge) 64.dp else 44.dp
    val backgroundColor = if (isLarge) Primary else SurfaceHigher
    val iconColor = if (isLarge) OnSurface else OnSurfaceAlt

    Box(
        modifier = Modifier
            .size(buttonSize)
            .background(backgroundColor, CircleShape)
            .clickable(enabled = isActive) { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = null,
            tint = iconColor,
        )
    }
}