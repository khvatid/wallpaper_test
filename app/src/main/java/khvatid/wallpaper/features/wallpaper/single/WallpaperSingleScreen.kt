package khvatid.wallpaper.features.wallpaper.single

import android.app.WallpaperManager
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.webkit.MimeTypeMap
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
import androidx.core.content.FileProvider
import androidx.core.graphics.drawable.toBitmap
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
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
                    CoroutineScope(Dispatchers.Main).launch(Dispatchers.IO) {
                        val ps = painter.state
                        when (ps) {
                            is AsyncImagePainter.State.Success -> {

                                val cacheDir = context.cacheDir
                                val file = File(cacheDir, "${state.id}.jpg")
                                try {
                                    val outputStream = FileOutputStream(file)
                                    ps.result.drawable.toBitmap().compress(Bitmap.CompressFormat.JPEG,100,outputStream)
                                    outputStream.flush()
                                    outputStream.close()
                                }catch (e : Exception){
                                    e.printStackTrace()
                                }
                                val fileUri: Uri = FileProvider.getUriForFile(context, "khvatid.wallpaper.fileprovider", file)
                                val intent = Intent(Intent.ACTION_ATTACH_DATA).apply {
                                    addCategory(Intent.CATEGORY_DEFAULT)
                                    setDataAndType(fileUri,"image/*")
                                    putExtra("mimeType","image/*")
                                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                                }
                                try {
                                    context.startActivity(Intent.createChooser(intent, "Set as Wallpaper"))
                                } catch (e: ActivityNotFoundException) {
                                    e.printStackTrace()
                                }

                            }

                            else -> throw NullPointerException("Bitmap not found")


                        }

                    }
                }) {
                    Text(text = "Set as wallpaper", modifier = Modifier.padding(horizontal = 8.dp))
                }
            })
    }
}

private fun getMimeType(url: String): String? {
    var type: String? = null
    val extension = MimeTypeMap.getFileExtensionFromUrl(url)
    if (extension != null) {
        type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
    }
    return type
}


private val imageModifier = Modifier
private val rowBoxModifier = Modifier.height(56.dp)


