package zed.rainxch.vibeplayer.core.presentation.components.progressbars

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import org.jetbrains.compose.ui.tooling.preview.Preview
import zed.rainxch.vibeplayer.core.presentation.theme.VibePlayerTheme

@Composable
fun ScanningProgressbar(
    laps: Int = 4,
    modifier: Modifier = Modifier
) {
    val rotationInfinityTransition = rememberInfiniteTransition()
    val stickRotation = rotationInfinityTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 2000,
                delayMillis = 0,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Restart
        )
    )

    Box(
        modifier = modifier
            .size(180.dp)
            .padding(12.dp)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.secondary.copy(alpha = .1f),
                shape = CircleShape
            )
            .padding(12.dp)
            .border(
                width = 2.dp,
                color = MaterialTheme.colorScheme.secondary,
                shape = CircleShape
            )
            .clip(CircleShape)
    ) {
        repeat(laps) { index ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding((index + 1) * 12.dp)
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.secondary.copy(alpha = .1f),
                        shape = CircleShape
                    )
            )
        }

        Box(
            modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.secondary)
                .align(Alignment.Center)
        )

        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .width(1.dp)
                .fillMaxHeight(fraction = 1f)
                .graphicsLayer {
                    rotationZ = stickRotation.value
                    transformOrigin = TransformOrigin.Center
                }
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xffEBFE6F),
                            Color(0xffF8FDD6),
                            Color.Transparent,
                            Color.Transparent,
                            Color.Transparent,
                            Color.Transparent,
                        ),
                        startY = 0f,
                        endY = Float.POSITIVE_INFINITY
                    )
                )
                .drawWithContent {
                    drawContent()

                    drawRect(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                Color(0xffEBFE6F),
                                Color(0xffEBFE6F).copy(alpha = .0f),
                                Color(0xffEBFE6F).copy(alpha = .0f)
                            )
                        ),
                        size = Size(
                            width = 300f,
                            height = size.height
                        )
                    )
                }
        )
    }
}

@Preview
@Composable
fun ScanningProgressbarPreview() {
    VibePlayerTheme {
        ScanningProgressbar()
    }
}