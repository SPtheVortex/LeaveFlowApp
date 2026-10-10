package dev.h4ze.leaveflow

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

object LeaveColors {
    // Sampled from the captured original H5 page and matched to its stylesheet.
    val BrandBlue = Color(0xFF6DB0FA)
    val ActionBlue = Color(0xFF52AEFF)
    val IconBlue = Color(0xFF52AEFF)
    val PageBackground = Color(0xFFF5F6FA)
    val SectionBackground = Color(0xFFF5F6FA)
    val TextPrimary = Color(0xFF333333)
    val TextSecondary = Color(0xFF999999)
    val Divider = Color(0xFFEEEEEE)
    val Approved = Color(0xFF87D068)
    val Ended = Color(0xFF9A9A9A)
    val Progress = Color(0xFF87D068)
}

@Immutable
data class LeaveSpacing(
    val xs: Dp = 4.dp,
    val sm: Dp = 8.dp,
    val md: Dp = 16.dp,
    val lg: Dp = 24.dp,
    val xl: Dp = 32.dp,
)

val LocalLeaveSpacing = staticCompositionLocalOf { LeaveSpacing() }

private val LeaveColorScheme = lightColorScheme(
    primary = LeaveColors.ActionBlue,
    onPrimary = Color.White,
    secondary = LeaveColors.Approved,
    background = LeaveColors.PageBackground,
    onBackground = LeaveColors.TextPrimary,
    surface = Color.White,
    onSurface = LeaveColors.TextPrimary,
    surfaceVariant = LeaveColors.SectionBackground,
    onSurfaceVariant = LeaveColors.TextSecondary,
    outline = LeaveColors.Divider,
)

@Composable
fun LeaveFlowTheme(content: @Composable () -> Unit) {
    CompositionLocalProvider(LocalLeaveSpacing provides LeaveSpacing()) {
        MaterialTheme(
            colorScheme = LeaveColorScheme,
            typography = Typography(),
            content = content,
        )
    }
}
