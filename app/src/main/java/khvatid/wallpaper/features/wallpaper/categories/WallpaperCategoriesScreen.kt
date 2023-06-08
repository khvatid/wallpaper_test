package khvatid.wallpaper.features.wallpaper.categories

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import khvatid.wallpaper.ui.components.ContentCard
import khvatid.wallpaper.ui.components.LoadingShimmerEffect

@Composable
fun WallpaperCategoriesScreen(
    navigateToSlug: (String) -> Unit,
    viewModel: WallpaperCategoriesViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()
    WallpaperCategoriesScreenUi(
        state = state,
        events = viewModel::obtainEvent,
        navigateToSlug = navigateToSlug
    )
}

@Composable
private fun WallpaperCategoriesScreenUi(
    state: WallpaperCategoriesContract.State,
    events: (WallpaperCategoriesContract.Event) -> Unit,
    navigateToSlug: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier.systemBarsPadding(),
        contentPadding = PaddingValues(bottom = 128.dp),
        content = {
            items(state.categoriesList) {
                ContentCard(
                    modifier = cardModifier.clickable { navigateToSlug(it.slug) },
                    image = {
                        AsyncImage(
                            modifier = imageModifier,
                            model = it.imageUrl,
                            contentScale = ContentScale.Crop,
                            contentDescription = null
                        )
                    },
                    label = {
                        Text(
                            modifier = labelModifier,
                            text = it.label,
                            maxLines = 1,
                            style = MaterialTheme.typography.headlineSmall
                        )
                    },
                    description = {
                        Text(
                            modifier = descriptionModifier,
                            text = it.description,
                            maxLines = 3,
                            style = MaterialTheme.typography.bodyMedium,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                )
            }
            if (state.isLoading) {
                items(4) {
                    ContentCard(
                        modifier = cardModifier,
                        image = {
                            LoadingShimmerEffect(
                                modifier = imageModifier,
                                color = MaterialTheme.colorScheme.surfaceVariant
                            )
                        },
                        label = {
                            LoadingShimmerEffect(
                                modifier = labelModifier.clip(MaterialTheme.shapes.large),
                                color = MaterialTheme.colorScheme.surfaceVariant
                            )
                        },
                        description = {
                            LoadingShimmerEffect(
                                modifier = descriptionModifier.clip(MaterialTheme.shapes.large),
                                color = MaterialTheme.colorScheme.surfaceVariant
                            )
                        }
                    )
                }
            } else if (!state.isEmpty) {
                item {
                    LaunchedEffect(true) {
                        events(WallpaperCategoriesContract.Event.ScrollToEnd)
                    }
                }
            }
        }
    )
}

private val cardModifier = Modifier
    .padding(horizontal = 4.dp, vertical = 2.dp)
    .fillMaxWidth()
private val imageModifier = Modifier
    .height(128.dp)
    .width(88.dp)
private val labelModifier = Modifier
    .fillMaxWidth()
    .height(32.dp)
private val descriptionModifier = Modifier
    .fillMaxWidth()
    .height(64.dp)