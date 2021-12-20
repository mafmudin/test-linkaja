package test.linkaja.testapp.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import test.linkaja.testapp.homescreen.model.movie.Movie

@Dao
interface MovieDao {
    @Insert(onConflict = REPLACE)
    fun insertAll(movies: MutableList<Movie>)

    @Query("SELECT * FROM movie")
    fun getAllMovie(): MutableList<Movie>
}