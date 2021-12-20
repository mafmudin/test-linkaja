package test.linkaja.testapp.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import test.linkaja.testapp.homescreen.model.genres.Genre
import test.linkaja.testapp.homescreen.model.movie.Movie

@Database(
    version = 1,
    entities = [Genre::class, Movie::class]
)
@TypeConverters(ListIntConverter::class)
abstract class AppDatabase :RoomDatabase(){

    abstract fun genreDao(): GenreDao
    abstract fun movieDao(): MovieDao
}