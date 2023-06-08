package khvatid.wallpaper.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import dagger.hilt.android.AndroidEntryPoint
import khvatid.wallpaper.features.wallpaper.categories.WallpaperCategoriesScreen
import khvatid.wallpaper.features.wallpaper.images.WallpaperImagesScreen
import khvatid.wallpaper.features.wallpaper.single.WallpaperSingleScreen
import khvatid.wallpaper.features.wallpaper.wallpaperGraph
import khvatid.wallpaper.ui.theme.AiTheme


@AndroidEntryPoint
class AppActivity : ComponentActivity() {

    private val viewModel: AppViewModel by viewModels()

    @OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val uiState by viewModel.uiState.collectAsState()
            AiTheme(dynamicColor = uiState.isDynamicTheme) {
                Scaffold(
                    contentWindowInsets = WindowInsets(0.dp, 0.dp, 0.dp, 0.dp)
                ) { paddingValues ->
                    NavHost(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                            .consumeWindowInsets(paddingValues),
                        navController = uiState.navController,
                        startDestination = "wallpaper",
                    ) {
                        wallpaperGraph(
                            route = "wallpaper",
                            navigateToSlug = { viewModel.obtainEvent(AppContract.Event.NavigateToSlug(it)) },
                            navigateToImage ={viewModel.obtainEvent(AppContract.Event.NavigateToImage(it))}
                        )
                    }
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onRestart() {
        super.onRestart()
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}