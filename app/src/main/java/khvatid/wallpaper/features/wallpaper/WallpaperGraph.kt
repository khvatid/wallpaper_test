package khvatid.wallpaper.features.wallpaper

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import khvatid.wallpaper.features.wallpaper.categories.WallpaperCategoriesScreen
import khvatid.wallpaper.features.wallpaper.images.WallpaperImagesScreen
import khvatid.wallpaper.features.wallpaper.single.WallpaperSingleScreen

sealed class WallpaperGraphRoutes(val route: String) {
    object Categories : WallpaperGraphRoutes("categories")
    data class CategoryImages(private val slug: String) :
        WallpaperGraphRoutes(route = "categories/$slug")

    data class SingleImage(private val id: String) : WallpaperGraphRoutes(route = "image/$id")
}


fun NavGraphBuilder.wallpaperGraph(
    route: String,
    navigateToSlug: (String) -> Unit,
    navigateToImage: (String) -> Unit,
) {
    navigation(startDestination = "categories", route = route) {
        composable(route = WallpaperGraphRoutes.Categories.route) {
            WallpaperCategoriesScreen(navigateToSlug = navigateToSlug)
        }
        composable(
            route = WallpaperGraphRoutes.CategoryImages("{slug}").route,
            arguments = listOf(navArgument(name = "slug") { type = NavType.StringType })
        ) {
            WallpaperImagesScreen(
                it.arguments?.getString("slug")!!,
                navigateToImage = navigateToImage
            )
        }
        composable(
            route = WallpaperGraphRoutes.SingleImage("{id}").route,
            arguments = listOf(navArgument("id") { type = NavType.StringType })
        ) {
            WallpaperSingleScreen(it.arguments?.getString("id")!!)
        }

    }
}
