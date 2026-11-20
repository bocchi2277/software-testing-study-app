package com.example.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BrightnessAuto
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.ui.theme.LocalQaColors
import com.example.ui.theme.LocalQaIsDarkTheme

@Composable
fun DynamicThemeToggleButton(
    onToggle: (isCurrentlyDark: Boolean) -> Unit,
    modifier: Modifier = Modifier,
    compact: Boolean = false,
    testTag: String = "dynamic_theme_toggle"
) {
    val isDark = LocalQaIsDarkTheme.current
    val qa = LocalQaColors.current

    val iconRotation by animateFloatAsState(
        targetValue = if (isDark) 0f else 180f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMediumLow
        ),
        label = "theme_icon_rotation"
    )

    val a11yLabel = if (isDark) {
        "الوضع الداكن مفعّل، اضغط للتبديل إلى الوضع الفاتح"
    } else {
        "الوضع الفاتح مفعّل، اضغط للتبديل إلى الوضع الداكن"
    }

    val containerColor = if (isDark) {
        MaterialTheme.colorScheme.primaryContainer
    } else {
        MaterialTheme.colorScheme.surface
    }
    val borderColor = if (isDark) {
        MaterialTheme.colorScheme.primary.copy(alpha = 0.55f)
    } else {
        qa.border
    }
    val iconColor = if (isDark) {
        qa.warn
    } else {
        MaterialTheme.colorScheme.primary
    }

    Surface(
        modifier = modifier
            .minimumInteractiveComponentSize()
            .testTag(testTag)
            .semantics { contentDescription = a11yLabel }
            .clip(RoundedCornerShape(999.dp))
            .clickable(role = Role.Switch) { onToggle(isDark) },
        shape = RoundedCornerShape(999.dp),
        color = containerColor,
        border = BorderStroke(1.dp, borderColor),
        tonalElevation = if (isDark) 2.dp else 1.dp
    ) {
        Row(
            modifier = Modifier.padding(
                horizontal = if (compact) 10.dp else 12.dp,
                vertical = 8.dp
            ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            AnimatedContent(
                targetState = isDark,
                transitionSpec = { fadeIn() togetherWith fadeOut() },
                label = "theme_toggle_icon"
            ) { dark ->
                Icon(
                    imageVector = if (dark) Icons.Filled.DarkMode else Icons.Filled.LightMode,
                    contentDescription = null,
                    tint = iconColor,
                    modifier = Modifier
                        .size(18.dp)
                        .rotate(iconRotation)
                )
            }
            if (!compact) {
                Text(
                    text = if (isDark) "داكن" else "فاتح",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    color = if (isDark) {
                        MaterialTheme.colorScheme.onPrimaryContainer
                    } else {
                        MaterialTheme.colorScheme.onSurface
                    },
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

private data class ThemeModeOption(
    val key: String,
    val label: String,
    val icon: ImageVector
)

private val THEME_OPTIONS = listOf(
    ThemeModeOption("auto", "تلقائي", Icons.Filled.BrightnessAuto),
    ThemeModeOption("light", "فاتح", Icons.Filled.LightMode),
    ThemeModeOption("dark", "داكن", Icons.Filled.DarkMode)
)

@Composable
fun ThemeModeSelectorCard(
    selectedTheme: String,
    onSelectTheme: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val qa = LocalQaColors.current
    val isDark = LocalQaIsDarkTheme.current

    SectionCard(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "المظهر والقراءة",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = when (selectedTheme) {
                        "dark" -> "الوضع الداكن مفعّل لتقليل إجهاد العين"
                        "light" -> "الوضع الفاتح مفعّل لوضوح النصوص نهاراً"
                        else -> if (isDark) "تلقائي (داكن حالياً حسب النظام)" else "تلقائي (فاتح حالياً حسب النظام)"
                    },
                    style = MaterialTheme.typography.labelSmall,
                    color = qa.muted
                )
            }
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (isDark) Icons.Filled.DarkMode else Icons.Filled.LightMode,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.size(18.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            THEME_OPTIONS.forEach { option ->
                val selected = selectedTheme == option.key
                Surface(
                    modifier = Modifier
                        .weight(1f)
                        .minimumInteractiveComponentSize()
                        .testTag("theme_${option.key}")
                        .clip(RoundedCornerShape(12.dp))
                        .clickable { onSelectTheme(option.key) },
                    shape = RoundedCornerShape(12.dp),
                    color = if (selected) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.background
                    },
                    border = BorderStroke(
                        width = 1.dp,
                        color = if (selected) MaterialTheme.colorScheme.primary else qa.border
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = option.icon,
                            contentDescription = option.label,
                            tint = if (selected) {
                                MaterialTheme.colorScheme.onPrimary
                            } else {
                                MaterialTheme.colorScheme.onSurface
                            },
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            text = option.label,
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                            color = if (selected) {
                                MaterialTheme.colorScheme.onPrimary
                            } else {
                                MaterialTheme.colorScheme.onSurface
                            },
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
    }
}
