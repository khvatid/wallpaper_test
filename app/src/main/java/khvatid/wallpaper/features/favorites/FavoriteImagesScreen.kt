package khvatid.wallpaper.features.favorites

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.SubcomposeAsyncImage
import khvatid.wallpaper.ui.components.LoadingShimmerEffect
import kotlin.reflect.KFunction1

@Composable
fun FavoriteImagesScreen(
    navigateToImage: (String) -> Unit,
    viewModel: FavoriteImagesViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()
    LaunchedEffect(key1 = viewModel, block = {
        viewModel.obtainEvent(FavoriteImagesContract.Event.OpenScreen)
    })
    FavoriteImagesScreenUi(
        state = state,
        event = viewModel::obtainEvent,
        navigateToImage = navigateToImage
    )
}

@Composable
private fun FavoriteImagesScreenUi(
    event: KFunction1<FavoriteImagesContract.Event, Unit>,
    state: FavoriteImagesContract.State,
    navigateToImage: (String) -> Unit,
) {
    LazyVerticalGrid(
        modifier = Modifier.systemBarsPadding(),
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(horizontal = 4.dp),
        content = {
            items(state.listImage) { image ->
                SubcomposeAsyncImage(
                    model = image.urls.small,
                    loading = {
                        LoadingShimmerEffect(
                            modifier = imageModifier,
                            color = MaterialTheme.colorScheme.surfaceVariant
                        )
                    },
                    contentDescription = null,
                    modifier = imageModifier.clickable { navigateToImage(image.id) },
                    contentScale = ContentScale.Crop
                )
            }
        }
    )
}

private val imageModifier = Modifier
    .height(256.dp)
    .width(128.dp)
    .padding(2.dp)