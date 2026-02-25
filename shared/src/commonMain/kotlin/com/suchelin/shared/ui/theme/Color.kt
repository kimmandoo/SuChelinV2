package com.suchelin.shared.ui.theme

import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

// Apple iOS 26 inspired color palette
val SystemBlue = Color(0xFF007AFF)
val SystemBlueLight = Color(0xFF5AC8FA)
val SystemIndigo = Color(0xFF5856D6)

val LabelPrimary = Color(0xFF1C1C1E)
val LabelSecondary = Color(0xFF3C3C43).copy(alpha = 0.6f)
val LabelTertiary = Color(0xFF3C3C43).copy(alpha = 0.3f)

val SystemBackground = Color(0xFFFAFAFC)
val GroupedBackground = Color(0xFFFAFAFC)
val ElevatedSurface = Color(0xFFFFFFFF)

val Separator = Color(0xFF3C3C43).copy(alpha = 0.08f)
val SeparatorOpaque = Color(0xFFD1D1D6)

val SystemGray = Color(0xFF8E8E93)
val SystemGray2 = Color(0xFFAEAEB2)
val SystemGray3 = Color(0xFFC7C7CC)
val SystemGray4 = Color(0xFFD1D1D6)
val SystemGray5 = Color(0xFFE5E5EA)
val SystemGray6 = Color(0xFFF2F2F7)

val SystemRed = Color(0xFFFF3B30)
val SystemOrange = Color(0xFFFF9500)
val SystemGreen = Color(0xFF34C759)

val SuChelinLightColorScheme = lightColorScheme(
    primary = SystemBlue,
    onPrimary = Color.White,
    primaryContainer = SystemBlue.copy(alpha = 0.1f),
    onPrimaryContainer = SystemBlue,
    secondary = SystemGray,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFF0F0F5),
    onSecondaryContainer = LabelPrimary,
    tertiary = SystemIndigo,
    onTertiary = Color.White,
    background = GroupedBackground,
    onBackground = LabelPrimary,
    surface = ElevatedSurface,
    onSurface = LabelPrimary,
    surfaceVariant = Color(0xFFF0F0F5),
    onSurfaceVariant = Color(0xFF3C3C43).copy(alpha = 0.6f),
    outline = SeparatorOpaque,
    outlineVariant = Separator,
)
