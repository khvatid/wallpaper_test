package khvatid.wallpaper.features.wallpaper.single

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import khvatid.wallpaper.features.wallpaper.single.WallpaperSingleContract.Event.SetAs
import khvatid.wallpaper.features.wallpaper.single.WallpaperSingleContract.Event.ShareImage
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
    state: WallpaperSingleContract.State, event: (WallpaperSingleContract.Event) -> Unit
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current).data(state.image.urls.full)
            .crossfade(true).build(), contentScale = ContentScale.Fit
    )
    Box(
        modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomEnd
    ) {
        Image(
            modifier = Modifier
                .fillMaxSize()
                .horizontalScroll(state = scrollState),
            painter = painter,
            contentScale = ContentScale.FillHeight,
            contentDescription = null,

            )
        if (state.isLoading || painter.state !is AsyncImagePainter.State.Success) {
            LoadingShimmerEffect(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.surfaceVariant
            )
        }
        AnimatedVisibility(visible = !state.isLoading, enter = fadeIn()) {
            BottomMenu(
                isFavorite = state.isFavorite,
                onShareClick = {
                    when (val painter = painter.state) {
                        is AsyncImagePainter.State.Success -> event(
                            ShareImage(
                                context = context, bitmap = painter.result.drawable.toBitmap()
                            )
                        )

                        else -> {}
                    }
                },
                onFavoriteClick = { event(WallpaperSingleContract.Event.AddToFavorite) },
                onSetAsClick = {
                    when (val painterState = painter.state) {
                        is AsyncImagePainter.State.Success -> event(
                            SetAs(
                                context = context, bitmap = painterState.result.drawable.toBitmap()
                            )
                        )

                        else -> {}
                    }
                }

            )
        }
    }
}


@Composable
fun BottomMenu(
    isFavorite: Boolean,
    onShareClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    onSetAsClick: () -> Unit
) {
    BottomAppBar(
        modifier = Modifier
            .padding(horizontal = 32.dp, vertical = 24.dp)
            .clip(MaterialTheme.shapes.extraLarge),
        windowInsets = WindowInsets(0, 0, 0, 0),
        actions = {
            IconButton(onClick = onShareClick) {
                Icon(
                    imageVector = Icons.Rounded.Share, contentDescription = null
                )
            }
            IconButton(onClick = onFavoriteClick) {
                Icon(
                    imageVector = if (isFavorite) Icons.Rounded.Favorite else Icons.Rounded.FavoriteBorder,
                    contentDescription = null
                )
            }
        },
        floatingActionButton = {

            ExtendedFloatingActionButton(onClick = onSetAsClick) {
                Text(
                    text = "Set as wallpaper",
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
            }
        }
    )
}