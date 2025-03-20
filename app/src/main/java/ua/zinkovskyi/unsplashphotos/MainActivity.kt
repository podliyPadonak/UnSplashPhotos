package ua.zinkovskyi.unsplashphotos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import kotlinx.coroutines.launch
import ua.zinkovskyi.unsplashphotos.data.Image
import ua.zinkovskyi.unsplashphotos.data.UnsplashApiService
import ua.zinkovskyi.unsplashphotos.ui.theme.UnSplashPhotosTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            UnSplashPhotosTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {

    PhotoListScreen(PhotoViewModel())

    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    UnSplashPhotosTheme {
        Greeting("Android")
    }
}

class PhotoViewModel : ViewModel() {
    val client = HttpClient(CIO)

    private val apiService = UnsplashApiService(client, "afdBxz_67xorIqGGA-f1JC2SiOWKc7mCH1gONcyy8_Q")

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


@Composable
fun PhotoListScreen(viewModel: PhotoViewModel) {
    // Состояние для отслеживания загрузки данных
    val photos = viewModel.photos

    LaunchedEffect(Unit) {
        viewModel.loadPhotos(page = 1, perPage = 30)
    }

    if (photos.value.isEmpty()) {
        // Показываем индикатор загрузки, если фотографии еще не загружены
        CircularProgressIndicator()
    }
    SpanLazyVerticalGrid(3,photos.value)

}

@Composable
fun SpanLazyVerticalGrid(cols: Int, itemList: List<Image>) {
    val lazyGridState = rememberLazyGridState()
    LazyVerticalGrid(
        columns = GridCells.Fixed(cols),
        state = lazyGridState
    ) {
        items(itemList, span = { photo ->
            val isHorizontal = (photo.width ?: 0) > (photo.height ?: 0)
            val span = if (isHorizontal) {
                2 // горизонтальное фото занимает 2 колонки
            } else {
                1 // вертикальное фото занимает 1 колонку
            }
            GridItemSpan(span)
        }) { photo ->
            val imageUrl = photo.urls.small
            //val imagePainter = rememberAsyncImagePainter(imageUrl)

            PhotoItem(photo)

//            Box(
//                modifier = Modifier
//                    .padding(4.dp)
//                    .fillMaxWidth()
//                    .height(200.dp)
//            ) {
//                Image(
//                    painter = imagePainter,
//                    contentDescription = "Photo",
//                    modifier = Modifier.fillMaxSize()
//                )
//            }
        }
    }
}

//@Composable
//fun PhotoGrid(photos: List<Image>) {
//    LazyVerticalGrid(
//        columns = GridCells.Fixed(3), // 3 колонки
//        contentPadding = PaddingValues(4.dp),
//        content = {
//            items(photos) { photo ->
//                val isHorizontal = (photo.width ?: 0) > (photo.height ?: 0)
//                val imageUrl = photo.urls.small
//                val imagePainter = rememberAsyncImagePainter(imageUrl)
//
//                // Используем spanStrategy, чтобы контролировать, сколько колонок будет занимать элемент
//                val span = if (isHorizontal) {
//                    GridItemSpan(2) // горизонтальное фото занимает 2 колонки
//                } else {
//                    GridItemSpan(1) // вертикальное фото занимает 1 колонку
//                }
//
//                Box(
//                    modifier = Modifier
//                        .padding(4.dp)
//                        .fillMaxWidth()
//                ) {
//                    Image(
//                        painter = imagePainter,
//                        contentDescription = "Photo",
//                        modifier = Modifier.fillMaxSize()
//                    )
//                }
//            }
//        }
//    )
//}
//
//
//@Composable
//fun PhotoCollage2(photos: List<Image>) {
//    LazyVerticalGrid(
//        columns = GridCells.Adaptive(minSize = 120.dp),
//        modifier = Modifier.fillMaxSize()
//    ) {
//        items(photos) { photo -> // Перебираем список photos
//            val imageUrl = photo.urls.small
//            val imagePainter = rememberAsyncImagePainter(imageUrl)
//
//            // Получаем пропорции изображения
//            val aspectRatio = remember(photo) { calculateAspectRatio(photo) }
//
//            // Отображаем изображение с нужными пропорциями
//            Box(
//                modifier = Modifier
//                    .padding(4.dp) // Отступы между изображениями
//                    .aspectRatio(aspectRatio) // Используем динамическое соотношение сторон
//            ) {
//                Image(
//                    painter = imagePainter,
//                    contentDescription = "Photo",
//                    modifier = Modifier.fillMaxSize()
//                )
//            }
//        }
//    }
//}

//// Эта функция имитирует расчет соотношения сторон изображения
//// Реальный код для вычисления соотношения сторон изображения будет зависеть от библиотеки для работы с изображениями
//fun calculateAspectRatio(photo: Image): Float {
//    val width = photo.width ?: 1
//    val height = photo.height ?: 1
//    return width.toFloat() / height.toFloat()
//}

//@Composable
//fun PhotoCollage(photos: List<Image>) {
//    LazyVerticalGrid(
//        columns = GridCells.Adaptive(minSize = 128.dp), // автоматически регулирует размер ячеек
//        modifier = Modifier.fillMaxSize()
//    ) {
//        items(photos.count()) { photo ->
//            val imageUrl = photos[photo].urls.small
//            val imagePainter = rememberAsyncImagePainter(imageUrl)
//
//            // Загружаем и показываем изображение в коллаже
//            Box(
//                modifier = Modifier
//                    .padding(4.dp) // отступы между изображениями
//                    .aspectRatio(1f) // соотношение сторон 1:1 (можно изменять)
//            ) {
//                Image(
//                    painter = imagePainter,
//                    contentDescription = "Photo",
//                    modifier = Modifier.fillMaxSize()
//                )
//            }
//        }
//    }
//}

@Composable
fun PhotoItem(photo: Image) {
    val imageUrl = photo.urls.small
    val imagePainter = rememberAsyncImagePainter(imageUrl)

    // Элемент списка, отображающий миниатюру фотографии
    Text(text = photo.user.name)
    Image(
        painter = imagePainter,
        contentDescription = "Photo",
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .padding(16.dp)
    )
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    // Здесь можно передать ViewModel в реальной ситуации
    PhotoListScreen(viewModel = PhotoViewModel())
}