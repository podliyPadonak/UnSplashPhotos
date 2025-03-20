package ua.zinkovskyi.unsplashphotos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
        viewModel.loadPhotos(page = 1, perPage = 10)
    }

    if (photos.value.isEmpty()) {
        // Показываем индикатор загрузки, если фотографии еще не загружены
        CircularProgressIndicator()
    }
    LazyColumn {
        items(photos.value) { photo ->
            PhotoItem(photo)
        }
    }
}

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