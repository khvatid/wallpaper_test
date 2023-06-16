package khvatid.wallpaper.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ListSwitchButton(label: String, isActive: Boolean, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(modifier = Modifier.padding(horizontal = 18.dp).weight(0.7f), contentAlignment = Alignment.CenterStart) {
            Text(text = label)
        }
        Box(modifier = Modifier.weight(0.3f), contentAlignment = Alignment.Center) {
            Switch(checked = isActive, onCheckedChange = { onClick() })
        }
    }
}