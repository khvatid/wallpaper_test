package khvatid.wallpaper.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import dagger.hilt.android.AndroidEntryPoint
import khvatid.wallpaper.features.favorites.FavoriteImagesScreen
import khvatid.wallpaper.features.settings.SettingsScreen
import khvatid.wallpaper.features.wallpaper.WallpaperGraphRoutes
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
            AiTheme(
                dynamicColor = uiState.themeSettingModel.isDynamic,
                darkTheme = if (uiState.themeSettingModel.isSystem) isSystemInDarkTheme()
                else uiState.themeSettingModel.isDark
            ) {
                Scaffold(
                    topBar = {
                        if (uiState.currentRoute != null &&
                            uiState.currentRoute != WallpaperGraphRoutes.SingleImage("{id}")
                                .route
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .systemBarsPadding(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                IconButton(onClick = {
                                    viewModel.obtainEvent(AppContract.Event.NavigateToFavorite)
                                }) {
                                    Icon(
                                        imageVector = Icons.Filled.Favorite,
                                        contentDescription = null
                                    )
                                }
                                IconButton(onClick = {
                                    viewModel.obtainEvent(AppContract.Event.NavigateToSettings)
                                }) {
                                    Icon(
                                        imageVector = Icons.Rounded.Settings,
                                        contentDescription = null
                                    )
                                }
                            }
                        }
                    },
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
                            navigateToSlug = {
                                viewModel.obtainEvent(
                                    AppContract.Event.NavigateToSlug(
                                        it
                                    )
                                )
                            },
                            navigateToImage = {
                                viewModel.obtainEvent(
                                    AppContract.Event.NavigateToImage(
                                        it
                                    )
                                )
                            }
                        )
                        composable(route = "favorite") {
                            FavoriteImagesScreen(navigateToImage = {
                                viewModel.obtainEvent(
                                    AppContract.Event.NavigateToImage(it)
                                )
                            })
                        }
                        composable(route = "settings") {
                            SettingsScreen()
                        }
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
