package ua.zinkovskyi.unsplashphotos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import coil.compose.rememberAsyncImagePainter
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
                MainActivityContent()
            }
        }
    }
}

class PhotoViewModel : ViewModel() {
    val client = HttpClient(CIO)

    private val apiService =
        UnsplashApiService(client, "afdBxz_67xorIqGGA-f1JC2SiOWKc7mCH1gONcyy8_Q")

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

val vm = PhotoViewModel()

@Composable
fun MainActivityContent() {
    val navController = rememberNavController()

    Scaffold(
        topBar = { AppBar(navController) },
        content = { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = "photo_list",
                modifier = Modifier.padding(innerPadding)
            ) {
                composable("photo_list") {
                    PhotoListScreen(
                        viewModel = vm,
                        onPhotoClick = { photo ->
                            navController.navigate("photo_detail/${photo.id}")
                        }
                    )
                }
                composable(
                    "photo_detail/{photoId}",
                    arguments = listOf(navArgument("photoId") { type = NavType.StringType })
                ) { backStackEntry ->
                    val photoId =
                        backStackEntry.arguments?.getString("photoId") ?: return@composable

                    val photo = vm.photos.value.filter { it.id == photoId }.first()

                    PhotoScreen(
                        photo = photo,
                    )
                }
            }
        }
    )

    val context = LocalContext.current
    BackHandler {
        if (navController.currentBackStackEntry == null || navController.previousBackStackEntry == null) {
            // Закрываем приложение, если бэкстек пуст
            (context as? ComponentActivity)?.finish()
        } else {
            navController.popBackStack() // Иначе стандартное поведение
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(navController: NavController) {
    val context = LocalContext.current
    TopAppBar(
        title = {
            Text(
                text = if (navController.currentBackStackEntry?.destination?.route == "photo_list") "Галерея" else "Фото",
                style = MaterialTheme.typography.titleLarge
            )
        },
        navigationIcon = {
            if (navController.currentBackStackEntry?.destination?.route != "photo_list") {
                IconButton(onClick = {
                    if (navController.previousBackStackEntry != null) {
                        navController.popBackStack()
                    } else {
                        (context as? ComponentActivity)?.finish()
                    }
                }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
            }
        }
    )
}


@Composable
fun PhotoListScreen(
    modifier: Modifier = Modifier,
    viewModel: PhotoViewModel,
    onPhotoClick: (Image) -> Unit
) {
    // Состояние для отслеживания загрузки данных
    val photos = viewModel.photos

    viewModel.loadPhotos(page = 1, perPage = 30)

    Box(
        Modifier
            .fillMaxSize()
            .then(modifier)
    ) {
        if (photos.value.isEmpty()) {
            CircularProgressIndicator(Modifier.align(Alignment.Center))
        }
        SpanLazyVerticalGrid(2, photos.value, onPhotoClick)
    }
}

@Composable
fun SpanLazyVerticalGrid(cols: Int, itemList: List<Image>, onPhotoClick: (Image) -> Unit) {
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

@Composable
fun PhotoItem(photo: Image) {
    val imageUrl = photo.urls.small
    val imagePainter = rememberAsyncImagePainter(imageUrl)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
    ) {
        Image(
            painter = imagePainter,
            contentDescription = "Photo",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.Center)
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .background(Color.Black.copy(alpha = 0.6f))
                .padding(8.dp)
        ) {
            Text(
                text = photo.user.name,
                color = Color.White,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

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