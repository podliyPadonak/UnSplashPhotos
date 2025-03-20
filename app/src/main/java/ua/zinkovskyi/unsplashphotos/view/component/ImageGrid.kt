package ua.zinkovskyi.unsplashphotos.view.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ua.zinkovskyi.unsplashphotos.data.Image

@Composable
fun ImageGrid(cols: Int, itemList: List<Image>, onPhotoClick: (Image) -> Unit) {
    val lazyGridState = rememberLazyGridState()
    LazyVerticalGrid(
        columns = GridCells.Fixed(cols),
        state = lazyGridState
    ) {
        items(itemList, span = { photo ->
            val span = 1
            GridItemSpan(span)
        }) { photo ->
            Box(
                modifier = Modifier
                    .clickable(
                        onClick = {
                            onPhotoClick(photo)
                        },
                    )
                    .fillMaxSize() // Дополнительно задаём размер, если нужно
            ) {
                PhotoItem(photo)
            }
        }
    }
}