package khvatid.wallpaper.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ContentCard(
    modifier: Modifier = Modifier,
    image: @Composable() (BoxScope.() -> Unit),
    label: @Composable() (BoxScope.() -> Unit),
    description: @Composable() (BoxScope.() -> Unit)
) {
    Row(modifier = modifier) {
        Box(
            modifier = Modifier.weight(0.3f),
            content = image,
            contentAlignment = Alignment.Center
        )
        Column(modifier = Modifier.weight(0.7f).padding(end = 4.dp)) {
            Box(
                content = label,
                contentAlignment = Alignment.TopStart
            )
            Spacer(modifier = Modifier.height(4.dp))
            Box(

                content = description,
                contentAlignment = Alignment.TopStart
            )
        }
    }
}