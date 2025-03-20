package ua.zinkovskyi.unsplashphotos.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import kotlinx.coroutines.launch
import ua.zinkovskyi.unsplashphotos.data.Image
import ua.zinkovskyi.unsplashphotos.data.UnsplashApiService

class PhotosScreenViewModel : ViewModel() {
    val client = HttpClient(CIO)

    private val apiService =
        UnsplashApiService(client, "afdBxz_67xorIqGGA-f1JC2SiOWKc7mCH1gONcyy8_Q")

    // Состояние для хранения списка фотографий
    var photos = mutableStateOf<List<Image>>(emptyList())
        private set

    // Загрузка фотографий в фоновом потоке
    fun loadPhotos(page: Int, perPage: Int) {
        viewModelScope.launch {
            try {
                val loadedPhotos = apiService.getPhotos(page, perPage)
                photos.value = loadedPhotos
            } catch (e: Exception) {
                // Обработка ошибки
            }
        }
    }
}