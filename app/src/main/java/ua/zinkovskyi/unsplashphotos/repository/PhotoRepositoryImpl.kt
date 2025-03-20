package ua.zinkovskyi.unsplashphotos.repository

import org.koin.java.KoinJavaComponent.getKoin
import ua.zinkovskyi.unsplashphotos.data.Image
import ua.zinkovskyi.unsplashphotos.data.UnsplashApiService

class PhotoRepositoryImpl() : PhotoRepository {
    val apiService: UnsplashApiService = getKoin().get()
    var photos: List<Image> = emptyList()

    override suspend fun getPhotos(page: Int, perPage: Int): List<Image> {
        if(photos.isNotEmpty()) return photos
        photos = apiService.getPhotos(page, perPage)
        return photos
    }

    override suspend fun getPhotoById(id: String): Image? {
        val photo = photos.filter { it.id == id }.firstOrNull()
        return photo
    }
}