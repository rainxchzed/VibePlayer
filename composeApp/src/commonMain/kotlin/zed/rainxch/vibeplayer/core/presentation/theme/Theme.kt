package zed.rainxch.vibeplayer.core.presentation.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val VibeDarkColorScheme = darkColorScheme(
    primary = ButtonPrimary,
    primaryContainer = ButtonPrimary30,
    primaryFixed = ButtonHover,

    onSurface = TextPrimary,
    onSurfaceVariant = TextSecondary,
    inverseOnSurface = TextDisabled,

    secondary = AccentYellow,
    onSecondary = SurfaceBG,

    outline = Outline
)

@Composable
fun VibePlayerTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        content = content,
        colorScheme = VibeDarkColorScheme,
        typography = VibePlayerTypography
    )
}