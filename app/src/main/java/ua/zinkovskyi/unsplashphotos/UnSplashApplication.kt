package ua.zinkovskyi.unsplashphotos

import android.app.Application
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.module.Module
import org.koin.dsl.module
import ua.zinkovskyi.unsplashphotos.data.UnsplashApiService
import ua.zinkovskyi.unsplashphotos.viewmodel.PhotosScreenViewModel

class UnSplashApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@UnSplashApplication)
            modules(appModule) // Инициализация модулей
        }
    }
}

val httpClient = HttpClient(CIO)
val token = BuildConfig.API_KEY

val appModule: Module = module {
    viewModel { PhotosScreenViewModel() } // Регистрация ViewModel
    single { UnsplashApiService(httpClient, token) }  // Регистрация сервиса API
}