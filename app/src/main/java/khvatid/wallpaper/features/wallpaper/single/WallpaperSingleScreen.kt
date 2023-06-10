package khvatid.wallpaper.features.wallpaper.single

import android.app.WallpaperManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Rect
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExtendedFloatingActionButton
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
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
    var loaded by remember {
        mutableStateOf(false)
    }
    val screenWidth = LocalConfiguration.current.screenWidthDp
    val screenHeight = LocalConfiguration.current.screenHeightDp
    val density = LocalDensity.current
    val scrollState = rememberScrollState()
    var offset by remember { mutableStateOf(IntOffset.Zero) }
    var imageSize by remember { mutableStateOf(IntSize.Zero) }
    var ratio by remember { mutableStateOf(1f) }
    var scale by remember { mutableStateOf(1f) }
    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data(state.image.urls.full)
            .crossfade(true)
            .allowHardware(false)
            .build(),
        contentScale = ContentScale.Fit,
        onSuccess = {
            /*bitmap = it.result.drawable.toBitmap(
                it.result.drawable.intrinsicWidth,
                it.result.drawable.intrinsicHeight,
                it.result.request.bitmapConfig
            )*/
            val options = BitmapFactory.Options()
            options.inJustDecodeBounds = false
            options.inSampleSize = 1


            scale = it.result.drawable.intrinsicWidth.toFloat()
            loaded = true
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
                },
            painter = painter,
            contentScale = ContentScale.FillHeight,
            contentDescription = null
        )





        Text(
            text = "scroll value: ${scrollState.value / ratio}\n" +
                    "image size: ${imageSize}\n" +
                    "screen size: ${screenWidth * density.density}x${screenHeight * density.density}\n" +
                    "\n\n wallpaper bitmap width: ${(screenWidth * density.density).roundToInt()}x ",
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
                Text(text = "ПОДЕЛИТЬСЯ")
                Text(text = "СКАЧАТЬ")
                IconButton(onClick = { /*TODO*/ }) {

                    //Icon(imageVector = Icons.Rounded.Share, contentDescription = null)
                }
                IconButton(onClick = { /*TODO*/ }) {


                    // Icon(imageVector = Icons.Rounded.Star, contentDescription = null)
                }
            }, floatingActionButton = {

                ExtendedFloatingActionButton(onClick = {
                    CoroutineScope(Dispatchers.IO).launch(Dispatchers.IO) {
                        try {

                            val ps = painter.state
                            when (ps) {
                                is AsyncImagePainter.State.Success -> {
                                    var nBitmap = Bitmap.createBitmap(
                                        (screenWidth * density.density).roundToInt(),
                                        (screenHeight * density.density).roundToInt(),
                                        Bitmap.Config.ARGB_8888
                                    )
                                    val canvas = Canvas(nBitmap)
                                    val srcRect = Rect(
                                        scrollState.value,
                                        0,
                                       scrollState.value + (screenWidth * density.density).roundToInt(),
                                        (screenHeight * density.density).roundToInt()
                                    )

                                    val destRect = Rect(
                                        0, 0, (screenWidth * density.density).roundToInt(),
                                        (screenHeight * density.density).roundToInt(),
                                    )
                                    val drawable = ps.result.drawable
                                    drawable.apply {
                                        this.bounds = srcRect
                                        this.draw(canvas)
                                    }
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

