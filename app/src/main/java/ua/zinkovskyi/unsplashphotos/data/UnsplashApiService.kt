package ua.zinkovskyi.unsplashphotos.data

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.http.HttpHeaders
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.client.call.*
import kotlinx.coroutines.withContext

class UnsplashApiService(private val client: HttpClient, private val authorizationHeader: String) {

    private val baseUrl = "https://api.unsplash.com"

    // Получаем список фотографий
    suspend fun getPhotos(page: Int, perPage: Int): List<Photo> {
        return withContext(Dispatchers.IO) {
            try {
                // Запрос к API для получения списка фотографий
                val response = client.get("$baseUrl/photos") {
                    // Устанавливаем параметры запроса
                    parameter("page", page)
                    parameter("per_page", perPage)
                    header(HttpHeaders.Authorization, authorizationHeader)
                }
                response.body() // Возвращаем список фотографий
            } catch (e: Exception) {
                throw Exception("Error fetching photos: ${e.message}")
            }
        }
    }

    // Получаем детальную информацию о фотографии
    suspend fun getPhotoDetail(id: String): Photo {
        return withContext(Dispatchers.IO) {
            try {
                // Запрос к API для получения информации о фотографии по ID
                val response = client.get("$baseUrl/photos/$id") {
                    header(HttpHeaders.Authorization, authorizationHeader)
                }
                response.body() // Возвращаем объект фотографии
            } catch (e: Exception) {
                throw Exception("Error fetching photo details: ${e.message}")
            }
        }
    }
}