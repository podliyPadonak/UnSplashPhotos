package ua.zinkovskyi.unsplashphotos.view.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import ua.zinkovskyi.unsplashphotos.data.Image
import ua.zinkovskyi.unsplashphotos.view.component.ImageGrid
import ua.zinkovskyi.unsplashphotos.viewmodel.PhotosScreenViewModel

@Composable
fun PhotoListScreen(
    modifier: Modifier = Modifier,
    viewModel: PhotosScreenViewModel,
    onPhotoClick: (Image) -> Unit
) {
    // Состояние для отслеживания загрузки данных
    val photos = viewModel.photos

    viewModel.loadPhotos(page = 1, perPage = 30)

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