package ua.zinkovskyi.unsplashphotos.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent.getKoin
import ua.zinkovskyi.unsplashphotos.data.Image
import ua.zinkovskyi.unsplashphotos.repository.PhotoRepository

class PhotosScreenViewModel : ViewModel() {
    private val photosRepository: PhotoRepository = getKoin().get()

    var photos = mutableStateOf<List<Image>>(emptyList())
        private set

    fun loadPhotos(page: Int, perPage: Int) {
        viewModelScope.launch {
            try {
                val loadedPhotos = photosRepository.getPhotos(page, perPage)
                photos.value = loadedPhotos
            } catch (e: Exception) {

            }
        }
    }
}