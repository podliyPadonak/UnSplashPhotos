package ua.zinkovskyi.unsplashphotos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import org.koin.androidx.compose.getKoin
import ua.zinkovskyi.unsplashphotos.ui.theme.UnSplashPhotosTheme
import ua.zinkovskyi.unsplashphotos.view.component.AppBar
import ua.zinkovskyi.unsplashphotos.view.screen.PhotoListScreen
import ua.zinkovskyi.unsplashphotos.view.screen.PhotoScreen
import ua.zinkovskyi.unsplashphotos.viewmodel.PhotosScreenViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            UnSplashPhotosTheme {
                MainActivityContent()
            }
        }
    }
}

@Composable
fun MainActivityContent() {
    val navController = rememberNavController()
    val viewModel = getKoin().get<PhotosScreenViewModel>()

    Scaffold(
        topBar = { AppBar(navController) },
        content = { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = "photo_list",
                modifier = Modifier.padding(innerPadding)
            ) {
                composable("photo_list") {
                    PhotoListScreen(
                        viewModel = viewModel,
                        onPhotoClick = { photo ->
                            navController.navigate("photo_detail/${photo.id}")
                        }
                    )
                }
                composable(
                    "photo_detail/{photoId}",
                    arguments = listOf(navArgument("photoId") { type = NavType.StringType })
                ) { backStackEntry ->
                    val photoId =
                        backStackEntry.arguments?.getString("photoId") ?: return@composable

                    val photo = viewModel.photos.value.filter { it.id == photoId }.first()

                    PhotoScreen(
                        photo = photo,
                    )
                }
            }
        }
    )

    val context = LocalContext.current
    BackHandler {
        if (navController.currentBackStackEntry == null || navController.previousBackStackEntry == null) {
            (context as? ComponentActivity)?.finish()
        } else {
            navController.popBackStack()
        }
    }
}