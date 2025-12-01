package zed.rainxch.vibeplayer.core.presentation.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.Font
import vibeplayer.composeapp.generated.resources.HostGrotesk_Bold
import vibeplayer.composeapp.generated.resources.HostGrotesk_ExtraBold
import vibeplayer.composeapp.generated.resources.HostGrotesk_Light
import vibeplayer.composeapp.generated.resources.HostGrotesk_Medium
import vibeplayer.composeapp.generated.resources.HostGrotesk_Regular
import vibeplayer.composeapp.generated.resources.HostGrotesk_SemiBold
import vibeplayer.composeapp.generated.resources.Res

val HostGroteskFontFamily
    @Composable get() =
        FontFamily(
            Font(
                resource = Res.font.HostGrotesk_Bold,
                weight = FontWeight.Bold
            ),
            Font(
                resource = Res.font.HostGrotesk_ExtraBold,
                weight = FontWeight.ExtraBold
            ),
            Font(
                resource = Res.font.HostGrotesk_Light,
                weight = FontWeight.Light
            ),
            Font(
                resource = Res.font.HostGrotesk_Medium,
                weight = FontWeight.Medium
            ),
            Font(
                resource = Res.font.HostGrotesk_Regular,
                weight = FontWeight.Normal
            ),
            Font(
                resource = Res.font.HostGrotesk_SemiBold,
                weight = FontWeight.SemiBold
            )
        )

val baseline = Typography()

val VibePlayerTypography
    @Composable get() = Typography(
        titleLarge = baseline.titleLarge.copy(
            fontFamily = HostGroteskFontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 24.sp,
            lineHeight = 28.sp
        ),
        titleMedium = baseline.titleMedium.copy(
            fontFamily = HostGroteskFontFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 20.sp,
            lineHeight = 24.sp
        ),

        bodyLarge = baseline.bodyLarge.copy(
            fontFamily = HostGroteskFontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
            lineHeight = 22.sp
        ),
        bodyMedium = baseline.bodyMedium.copy(
            fontFamily = HostGroteskFontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
            lineHeight = 18.sp
        )
    )
