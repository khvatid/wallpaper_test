package khvatid.wallpaper.features.wallpaper.single

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.core.content.FileProvider
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import khvatid.wallpaper.domain.models.ImageModel
import khvatid.wallpaper.domain.models.Resource
import khvatid.wallpaper.domain.usecase.GetFavoriteImageUseCase
import khvatid.wallpaper.domain.usecase.GetImageUseCase
import khvatid.wallpaper.domain.usecase.SaveToFavoriteImageUseCase
import khvatid.wallpaper.utils.ViewModelMVI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

@HiltViewModel
class WallpaperSingleViewModel @Inject constructor(
    private val getImageUseCase: GetImageUseCase,
    private val getFavoriteImageUseCase: GetFavoriteImageUseCase,
    private val saveToFavoriteImageUseCase: SaveToFavoriteImageUseCase,
) : ViewModelMVI<WallpaperSingleContract.State, WallpaperSingleContract.Event>() {
    override val state: MutableStateFlow<WallpaperSingleContract.State> =
        MutableStateFlow(WallpaperSingleContract.State())

    override fun obtainEvent(event: WallpaperSingleContract.Event) {
        when (event) {
            is WallpaperSingleContract.Event.OpenScreen -> {
                reduce(event)
            }

            is WallpaperSingleContract.Event.AddToFavorite -> {
                reduce(event)
            }

            is WallpaperSingleContract.Event.SetAs -> {
                reduce(event)
            }

            is WallpaperSingleContract.Event.ShareImage -> {
                reduce(event)
            }
        }
    }

    private fun reduce(event: WallpaperSingleContract.Event.ShareImage) {
        val cacheDir = event.context.cacheDir
        val file = File(cacheDir, "${state.value.id}.jpg")
        try {
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "image/*"
                putExtra(Intent.EXTRA_STREAM, getUri(file, event.context, event.bitmap))
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            event.context.startActivity(Intent.createChooser(intent, "Share Image"))
        } catch (e: Exception) {
            Log.i("VIEW_MODEL", "${e.message}")
        }
    }

    private fun reduce(event: WallpaperSingleContract.Event.SetAs) = viewModelScope.launch {
        val cacheDir = event.context.cacheDir
        val file = File(cacheDir, "${state.value.id}.jpg")
        try {
            val intent = Intent(Intent.ACTION_ATTACH_DATA).apply {
                addCategory(Intent.CATEGORY_DEFAULT)
                setDataAndType(getUri(file, event.context, event.bitmap), "image/*")
                putExtra("mimeType", "image/*")
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            event.context.startActivity(Intent.createChooser(intent, "Set as "))
        } catch (e: Exception) {
            Log.i("VIEW_MODEL", "${e.message}")
        }

    }

    private fun reduce(event: WallpaperSingleContract.Event.AddToFavorite) = viewModelScope.launch {
        val isFavorite = !state.value.isFavorite
        saveToFavoriteImageUseCase.execute(state.value.image, isFavorite){ it ->
            if (it == null){
                state.update { it.copy(isFavorite = isFavorite) }
            }
        }
    }


    private fun reduce(event: WallpaperSingleContract.Event.OpenScreen) =
        viewModelScope.launch(Dispatchers.IO) {
            getFavoriteImageUseCase.execute(id = event.id).first().handler(
                isFavorite = true,
                error = {
                    this.launch(Dispatchers.IO) {
                        getImageUseCase.execute(event.id).collect() { modelResource ->
                            modelResource.handler(
                                isFavorite = false,
                                error = { state.update { it.copy(isLoading = false) } })
                        }
                    }
                }
            )
        }

    private inline fun Resource<ImageModel>.handler(
        isFavorite: Boolean,
        noinline error: () -> Unit
    ) = when (this) {
        is Resource.Error -> error()
        is Resource.Loading -> state.update { it.copy(isLoading = true) }
        is Resource.Success -> state.update {
            it.copy(
                id = this.data.id,
                image = this.data,
                isLoading = false,
                isFavorite = isFavorite
            )
        }
    }


    private fun getUri(fileName: File, context: Context, bitmap: Bitmap): Uri {
        if (!fileName.exists()) {
            try {
                val outputStream = FileOutputStream(fileName)
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                outputStream.flush()
                outputStream.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return FileProvider.getUriForFile(
            context,
            "khvatid.wallpaper.fileprovider",
            fileName
        )
    }

}
