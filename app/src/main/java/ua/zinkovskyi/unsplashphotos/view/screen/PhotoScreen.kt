package ua.zinkovskyi.unsplashphotos.view.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import ua.zinkovskyi.unsplashphotos.data.Image
import ua.zinkovskyi.unsplashphotos.view.component.appBarText

@Composable
fun PhotoScreen(photo: Image) {
    val imageUrl = photo.urls.full
    val imagePainter = rememberAsyncImagePainter(imageUrl)
    val state = imagePainter.state

    appBarText.value = photo.user.name

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        when (state) {
            is AsyncImagePainter.State.Loading -> {
                CircularProgressIndicator(Modifier.align(Alignment.CenterHorizontally))
            }

            is AsyncImagePainter.State.Success -> {

            }

            is AsyncImagePainter.State.Error -> {
                Icon(
                    Icons.Default.Close,
                    contentDescription = "Back"
                )
            }

            else -> {}
        }
        Box {
            Image(
                painter = imagePainter,
                contentDescription = "Photo",
                modifier = Modifier
                    .align(Alignment.Center)
            )
        }

        Column(
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