package ua.zinkovskyi.unsplashphotos.view.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import coil.compose.rememberAsyncImagePainter
import ua.zinkovskyi.unsplashphotos.data.Image

@Composable
fun PhotoScreen(photo: Image) {
    val imageUrl = photo.urls.full
    val imagePainter = rememberAsyncImagePainter(imageUrl)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        Box {
            Image(
                painter = imagePainter,
                contentDescription = "Photo",
                modifier = Modifier
                    .align(Alignment.Center)
            )
        }

        Text(
            text = photo.user.name,
            color = Color.White,
            style = MaterialTheme.typography.bodyLarge
        )

    }
}