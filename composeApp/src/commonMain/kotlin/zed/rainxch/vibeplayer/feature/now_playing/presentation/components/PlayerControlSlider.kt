package zed.rainxch.vibeplayer.feature.now_playing.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import zed.rainxch.vibeplayer.core.presentation.utils.formatMilliseconds
import zed.rainxch.vibeplayer.feature.now_playing.presentation.MusicPlaybackState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerControlSlider(
    state: MusicPlaybackState,
    modifier: Modifier,
    onSeek: (Long) -> Unit
) {

    val progressFraction =
        if (state.duration > 0) state.currentProgress.toFloat() / state.duration else 0f


    val sliderColors = SliderDefaults.colors(
        thumbColor = Color.Transparent,
        activeTickColor = Color.Transparent,
        inactiveTickColor = Color.Transparent,
        disabledActiveTickColor = Color.Transparent,
        disabledInactiveTickColor = Color.Transparent,
        activeTrackColor = MaterialTheme.colorScheme.onSurface,
        inactiveTrackColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)
    )
    BoxWithConstraints(
        modifier = modifier
    ) {

        val fullWidth = maxWidth // This is the width of the blue parent

        Slider(
            value = progressFraction,
            onValueChange = { fraction ->
                onSeek((fraction * state.duration).toLong())
            },
            modifier = Modifier.fillMaxWidth(),
            colors = SliderDefaults.colors(
                thumbColor = Color.Transparent, // High visibility thumb
                activeTickColor = Color.Transparent,
                inactiveTickColor = Color.Transparent,
                disabledActiveTickColor = Color.Transparent,
                disabledInactiveTickColor = Color.Transparent,
                activeTrackColor = MaterialTheme.colorScheme.onSurface, // Played part
                inactiveTrackColor = MaterialTheme.colorScheme.outline // Unplayed part
            ),
            track = { sliderState ->


                Box(
                    modifier = Modifier
                        .requiredWidth(fullWidth)
                        .height(12.dp)
                        .background(MaterialTheme.colorScheme.outline, RoundedCornerShape(100)),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(progressFraction)
                            .fillMaxHeight()
                            .background(
                                MaterialTheme.colorScheme.onSurface,
                                RoundedCornerShape(100)
                            )
                    )
                }

                /*    SliderDefaults.Track(
                        colors = sliderColors,
                        enabled = true,
                        sliderState = sliderState,
                        modifier = Modifier.fillMaxWidth().height(12.dp).padding((-20).dp),
                        thumbTrackGapSize = 0.dp,
                        drawStopIndicator = null
                    )*/
            },
            thumb = {

                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.wrapContentSize().background(
                        MaterialTheme.colorScheme.onSurface,
                        RoundedCornerShape(16.dp)
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .padding(horizontal = 8.dp, vertical = 2.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {

                        Text(
                            text = "${formatMilliseconds(state.currentProgress)} / ${
                                formatMilliseconds(
                                    state.duration
                                )
                            }",
                            style = MaterialTheme.typography.bodySmall,
                            fontSize = 10.sp,
                            color = MaterialTheme.colorScheme.onSecondary
                        )

                    }
                }
            }
        )
    }
}

