package ua.zinkovskyi.unsplashphotos.view.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import ua.zinkovskyi.unsplashphotos.data.Image
import ua.zinkovskyi.unsplashphotos.view.component.appBarText

@Composable
fun PhotoScreen(photo: Image) {
    val imageUrl = photo.urls.full
    val imagePainter = rememberAsyncImagePainter(imageUrl)

    appBarText.value = photo.user.name

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

        Column (
            Modifier.padding(8.dp)
        ) {
            Text(
                text = photo.alt_description ?: "",
                style = MaterialTheme.typography.titleLarge,
            )
            val likesLabel = "${photo.likes} likes"
            Text(
                text = likesLabel,
                style = MaterialTheme.typography.titleMedium,
            )
            Text(
                text = photo.description ?: "",
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }
}