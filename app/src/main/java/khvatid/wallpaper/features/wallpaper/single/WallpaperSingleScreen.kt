package khvatid.wallpaper.features.wallpaper.single

import android.app.WallpaperManager
import android.graphics.Bitmap
import android.graphics.Rect
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Favorite
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.roundToInt


@Composable
fun WallpaperSingleScreen(id: String, viewModel: WallpaperSingleViewModel = hiltViewModel()) {
    val state by viewModel.uiState.collectAsState()
    LaunchedEffect(viewModel) {
        viewModel.obtainEvent(WallpaperSingleContract.Event.OpenScreen(id))
    }

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        WallpaperSingleScreenUi(state = state, event = viewModel::obtainEvent)
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun WallpaperSingleScreenUi(
    state: WallpaperSingleContract.State,
    event: (WallpaperSingleContract.Event) -> Unit
) {
    val context = LocalContext.current
    val wallpaperManager = WallpaperManager.getInstance(context)
    val density = LocalDensity.current
    val screenSize = IntSize(
        width = LocalConfiguration.current.screenWidthDp * density.density.roundToInt(),
        height = LocalConfiguration.current.screenHeightDp * density.density.roundToInt()
    )
    var ratioScreenToDrawable by remember { mutableStateOf(Size.Zero) }
    var ratioImageToDrawable by remember { mutableStateOf(Size.Zero) }

    val scrollState = rememberScrollState()
    var drawableSize by remember {
        mutableStateOf(IntSize.Zero)
    }
    var imageSize by remember { mutableStateOf(IntSize.Zero) }
    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data(state.image.urls.full)
            .crossfade(true)
            .allowHardware(false)
            .build(),
        contentScale = ContentScale.Fit,
        onSuccess = {
            drawableSize = IntSize(
                it.result.drawable.toBitmap().width,
                it.result.drawable.toBitmap().height,
            )
            ratioScreenToDrawable = Size(
                screenSize.width / it.result.drawable.intrinsicWidth.toFloat(),
                screenSize.height / it.result.drawable.intrinsicHeight.toFloat()
            )

        }
    )
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.BottomEnd
    ) {
        Image(
            modifier = Modifier
                .fillMaxSize()
                .horizontalScroll(state = scrollState)
                //.aspectRatio(ratio, true)
                .onGloballyPositioned {
                    imageSize = it.size
                    ratioImageToDrawable = Size(
                        it.size.width.toFloat() / drawableSize.width,
                        it.size.height.toFloat() / drawableSize.height
                    )
                },
            painter = painter,
            contentScale = ContentScale.FillHeight,
            contentDescription = null
        )



                Text(
                    text =
                    "screen size: $screenSize  -> ${(screenSize.width / ratioImageToDrawable.width)*ratioScreenToDrawable.width}x${screenSize.height / ratioImageToDrawable.height}\n" +

                            "image size: $imageSize\n" +
                            "drawable size: $drawableSize\n" +
                            "ratio screen / drawable: $ratioScreenToDrawable\n" +
                            "ratio image / drawable: $ratioImageToDrawable \n" +
                            "scroll value: ${scrollState.value}\n" +
                            "scroll value / rationScreenX: ${(scrollState.value / ratioScreenToDrawable.width)}\n" +
                            "scroll value / rationScreenX: ${(scrollState.value / ratioImageToDrawable.width)}\n",
                    modifier = Modifier
                        .padding(100.dp)
                        .fillMaxSize()
                )























        BottomAppBar(
            modifier = Modifier
                .padding(horizontal = 32.dp, vertical = 24.dp)
                .clip(MaterialTheme.shapes.extraLarge),
            windowInsets = WindowInsets(0, 0, 0, 0),
            actions = {
                IconButton(onClick = { /*TODO*/ }) {

                }
                IconButton(onClick = { event(WallpaperSingleContract.Event.AddToFavorite) }) {
                    Icon(imageVector = Icons.Rounded.Favorite, contentDescription = null)
                }
            }, floatingActionButton = {

                ExtendedFloatingActionButton(onClick = {
                    CoroutineScope(Dispatchers.IO).launch(Dispatchers.IO) {
                        try {
                            val ps = painter.state
                            when (ps) {
                                is AsyncImagePainter.State.Success -> {
                                    val srcRect = Rect(
                                        (scrollState.value / ratioScreenToDrawable.width / ratioImageToDrawable.width).roundToInt(),
                                        0,
                                        (screenSize.width) / ratioImageToDrawable.width.roundToInt(),
                                        ps.result.drawable.intrinsicHeight
                                    )
                                    val bitmap = ps.result.drawable.toBitmap()

                                    Log.i("BITMAP","RECT: ${srcRect.toString()} \n ${bitmap.width}x${bitmap.height}")
                                    var nBitmap = Bitmap.createBitmap(
                                        bitmap,
                                        srcRect.left,
                                        srcRect.top,
                                        srcRect.right - 1,
                                        srcRect.bottom
                                    )


                                    wallpaperManager.setBitmap(nBitmap)

                                }

                                else -> throw NullPointerException("Bitmap not found")


                            }

                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                }) {
                    Text(text = "Set as wallpaper", modifier = Modifier.padding(horizontal = 8.dp))
                }
            })
    }
}

private val imageModifier = Modifier
private val rowBoxModifier = Modifier.height(56.dp)

