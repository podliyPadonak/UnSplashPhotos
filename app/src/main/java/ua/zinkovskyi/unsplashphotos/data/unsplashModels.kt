package ua.zinkovskyi.unsplashphotos.data

data class Photo(
    val id: String,
    val createdAt: String,
    val updatedAt: String,
    val width: Int,
    val height: Int,
    val color: String,
    val description: String?,
    val altDescription: String?,
    val urls: PhotoUrls,
    val user: User
)

data class PhotoUrls(
    val raw: String,
    val full: String,
    val regular: String,
    val small: String,
    val thumb: String
)

data class User(
    val id: String,
    val username: String,
    val name: String,
    val portfolioUrl: String?,
    val profileImage: ProfileImage
)

data class ProfileImage(
    val small: String,
    val medium: String,
    val large: String
)