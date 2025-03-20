package ua.zinkovskyi.unsplashphotos.view.screen

import android.provider.ContactsContract.Contacts.Photo
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import kotlinx.coroutines.delay
import ua.zinkovskyi.unsplashphotos.data.Image
import ua.zinkovskyi.unsplashphotos.view.component.ImageGrid
import ua.zinkovskyi.unsplashphotos.view.component.appBarText
import ua.zinkovskyi.unsplashphotos.viewmodel.PhotosScreenViewModel

@Composable
fun PhotoListScreen(
    modifier: Modifier = Modifier,
    viewModel: PhotosScreenViewModel,
    onPhotoClick: (Image) -> Unit
) {
    appBarText.value = "UnSplash"

    val photos = viewModel.photos

    val isOnScreen = remember { mutableStateOf(true) }
    LaunchedEffect(isOnScreen.value) {
        while (viewModel.photos.value.isEmpty()) {
            viewModel.loadPhotos(page = 1, perPage = 30)
            delay(1000)
        }
    }
    Box(
        Modifier
            .fillMaxSize()
            .then(modifier)
    ) {
        if (photos.value.isEmpty()) {
            CircularProgressIndicator(Modifier.align(Alignment.Center))
        }
        ImageGrid(2, photos.value, onPhotoClick)
    }
}