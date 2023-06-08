package khvatid.wallpaper.ui.components

import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush.Companion.linearGradient
import androidx.compose.ui.graphics.Color

@Composable
fun LoadingShimmerEffect(modifier: Modifier, color: Color) {


    val gradient = listOf(
        color.copy(alpha = 0.9f),
        color.copy(alpha = 0.3f),
        color.copy(alpha = 0.9f)
    )

    val transition = rememberInfiniteTransition(label = "")

    val translateAnimation: State<Float> = transition.animateFloat(
        initialValue = 0f,
        targetValue = 4000f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1500,
                easing = FastOutLinearInEasing
            )
        ), label = ""
    )
    val brush = linearGradient(
        colors = gradient,
        start = Offset(0f, 0f),
        end = Offset(
            x = translateAnimation.value,
            y = translateAnimation.value
        )
    )

    Box(modifier = modifier.background(brush))

}