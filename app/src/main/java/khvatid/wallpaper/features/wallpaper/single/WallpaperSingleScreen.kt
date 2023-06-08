package khvatid.wallpaper.features.wallpaper.single

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import khvatid.wallpaper.ui.components.LoadingShimmerEffect

@Composable
fun WallpaperSingleScreen(id: String, viewModel: WallpaperSingleViewModel = hiltViewModel()) {
    val state by viewModel.uiState.collectAsState()
    LaunchedEffect(viewModel) {
        viewModel.obtainEvent(WallpaperSingleContract.Event.OpenScreen(id))
    }
    WallpaperSingleScreenUi(state = state, event = viewModel::obtainEvent)
}

@Composable
private fun WallpaperSingleScreenUi(
    state: WallpaperSingleContract.State,
    event: (WallpaperSingleContract.Event) -> Unit
) {
    Box(contentAlignment = Alignment.BottomCenter) {
        if (state.isLoading) {
            LoadingShimmerEffect(
                modifier = imageModifier,
                color = MaterialTheme.colorScheme.surfaceVariant
            )
        } else {
            AsyncImage(
                model = state.image.url,
                contentDescription = null,
                imageModifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }
        Row(
            modifier = Modifier
                .padding(16.dp)
                .background(
                    MaterialTheme.colorScheme.primary,
                    shape = MaterialTheme.shapes.extraLarge
                )
        ) {
            Box(modifier = rowBoxModifier.clickable { }, contentAlignment = Alignment.Center) {
                Text(text = "Установить на главный экран")
            }
            Box(modifier = rowBoxModifier.clickable { }, contentAlignment = Alignment.Center) {
                Icon(imageVector = Icons.Rounded.Share, contentDescription = null)
            }
            Box(modifier = rowBoxModifier.clickable { }, contentAlignment = Alignment.Center) {
                Icon(imageVector = Icons.Rounded.Star, contentDescription = null)
            }
        }
    }
}

private val imageModifier = Modifier.fillMaxSize()
private val rowBoxModifier = Modifier.height(64.dp)
