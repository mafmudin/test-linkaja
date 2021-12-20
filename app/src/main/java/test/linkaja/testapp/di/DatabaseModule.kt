package test.linkaja.testapp.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import test.linkaja.testapp.database.AppDatabase
import test.linkaja.testapp.database.GenreDao
import test.linkaja.testapp.database.MovieDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase{
        return Room.databaseBuilder(
            context, AppDatabase::class.java, "test_link_aja.db"
        ).build()
    }

    @Singleton
    @Provides
    fun provideGenreDao(appDatabase: AppDatabase): GenreDao = appDatabase.genreDao()

    @Singleton
    @Provides
    fun provideMovieDao(appDatabase: AppDatabase): MovieDao = appDatabase.movieDao()
}