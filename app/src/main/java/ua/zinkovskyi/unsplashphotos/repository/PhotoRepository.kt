package ua.zinkovskyi.unsplashphotos.repository

import ua.zinkovskyi.unsplashphotos.data.Image

interface PhotoRepository {
    suspend fun getPhotos(page: Int, perPage: Int): List<Image>
    fun getPhotoById(id: String): Image?
}
