package test.linkaja.testapp.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import test.linkaja.testapp.BuildConfig
import test.linkaja.testapp.api.ApiServices
import test.linkaja.testapp.database.GenreDao
import test.linkaja.testapp.database.MovieDao
import test.linkaja.testapp.detailmovie.repository.DetailMovieRepository
import test.linkaja.testapp.favourite.repository.FavoriteRepository
import test.linkaja.testapp.homescreen.repository.MovieRepository
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {

    @Singleton
    @Provides
    fun provideApplicationScope() = CoroutineScope(SupervisorJob())

    @Singleton
    @Provides
    fun provideHttpLogging(): HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    @Singleton
    @Provides
    fun provideHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(httpLoggingInterceptor)
        .connectTimeout(30, TimeUnit.SECONDS)
        .retryOnConnectionFailure(false)
        .build()

    @Singleton
    @Provides
    fun provideApi(httpClient: OkHttpClient): ApiServices = Retrofit.Builder()
        .baseUrl(BuildConfig.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(httpClient)
        .build()
        .create(ApiServices::class.java)

    @Singleton
    @Provides
    fun provideGenresRepository(apiServices: ApiServices, genreDao: GenreDao, movieDao: MovieDao):
            MovieRepository = MovieRepository(apiServices, genreDao, movieDao)

    @Singleton
    @Provides
    fun getDetailMovieRepository(apiServices: ApiServices): DetailMovieRepository = DetailMovieRepository(apiServices)

    @Singleton
    @Provides
    fun getFavouriteRepository(apiServices: ApiServices): FavoriteRepository = FavoriteRepository(apiServices)
}