package com.example.qrcraft.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathOperation
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun ScanningFrame(
    modifier: Modifier = Modifier,
    frameSize: Dp = 250.dp,
    cornerSize: Dp = 20.dp,
    cornerStrokeWidth: Dp = 4.dp,
    frameColor: Color = Color.Yellow
) {
    Box(modifier = modifier.size(frameSize)) {
        Canvas(
            modifier = Modifier.size(frameSize)
        ) {
            drawScanningFrame(
                frameSize = frameSize.toPx(),
                cornerSize = cornerSize.toPx(),
                cornerStrokeWidth = cornerStrokeWidth.toPx(),
                frameColor = frameColor
            )
        }
    }
}

private fun DrawScope.drawScanningFrame(
    frameSize: Float,
    cornerSize: Float,
    cornerStrokeWidth: Float,
    frameColor: Color
) {
    val strokeWidth = cornerStrokeWidth
    val halfStroke = strokeWidth / 2f

    // Draw corners
    // Top left corner
    drawLine(
        color = frameColor,
        start = Offset(halfStroke, cornerSize),
        end = Offset(halfStroke, halfStroke),
        strokeWidth = strokeWidth
    )
    drawLine(
        color = frameColor,
        start = Offset(halfStroke, halfStroke),
        end = Offset(cornerSize, halfStroke),
        strokeWidth = strokeWidth
    )

    // Top right corner
    drawLine(
        color = frameColor,
        start = Offset(frameSize - cornerSize, halfStroke),
        end = Offset(frameSize - halfStroke, halfStroke),
        strokeWidth = strokeWidth
    )
    drawLine(
        color = frameColor,
        start = Offset(frameSize - halfStroke, halfStroke),
        end = Offset(frameSize - halfStroke, cornerSize),
        strokeWidth = strokeWidth
    )

    // Bottom left corner
    drawLine(
        color = frameColor,
        start = Offset(halfStroke, frameSize - cornerSize),
        end = Offset(halfStroke, frameSize - halfStroke),
        strokeWidth = strokeWidth
    )
    drawLine(
        color = frameColor,
        start = Offset(halfStroke, frameSize - halfStroke),
        end = Offset(cornerSize, frameSize - halfStroke),
        strokeWidth = strokeWidth
    )

    // Bottom right corner
    drawLine(
        color = frameColor,
        start = Offset(frameSize - cornerSize, frameSize - halfStroke),
        end = Offset(frameSize - halfStroke, frameSize - halfStroke),
        strokeWidth = strokeWidth
    )
    drawLine(
        color = frameColor,
        start = Offset(frameSize - halfStroke, frameSize - halfStroke),
        end = Offset(frameSize - halfStroke, frameSize - cornerSize),
        strokeWidth = strokeWidth
    )
}