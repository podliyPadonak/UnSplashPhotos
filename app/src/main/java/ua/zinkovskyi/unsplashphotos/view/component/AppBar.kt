package ua.zinkovskyi.unsplashphotos.view.component

import androidx.activity.ComponentActivity
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(navController: NavController) {
    val context = LocalContext.current
    TopAppBar(
        title = {
            Text(
                text = if (navController.currentBackStackEntry?.destination?.route == "photo_list") "Галерея" else "Фото",
                style = MaterialTheme.typography.titleLarge
            )
        },
        navigationIcon = {
            if (navController.currentBackStackEntry?.destination?.route != "photo_list") {
                IconButton(onClick = {
                    if (navController.previousBackStackEntry != null) {
                        navController.popBackStack()
                    } else {
                        (context as? ComponentActivity)?.finish()
                    }
                }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
            }
        }
    )
}