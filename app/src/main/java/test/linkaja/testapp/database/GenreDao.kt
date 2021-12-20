package test.linkaja.testapp.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import test.linkaja.testapp.homescreen.model.genres.Genre

@Dao
interface GenreDao {
    @Insert(onConflict = REPLACE)
    fun insertAll(genres: List<Genre>)

    @Query("SELECT * FROM genre")
    fun getAllGenre(): List<Genre>
}