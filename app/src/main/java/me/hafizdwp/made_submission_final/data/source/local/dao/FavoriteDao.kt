package me.hafizdwp.made_submission_final.data.source.local.dao

import android.database.Cursor
import androidx.room.Dao
import androidx.room.Query
import me.hafizdwp.made_submission_final.base.BaseDao
import me.hafizdwp.made_submission_final.data.source.local.entity.FavoriteTable

/**
 * @author hafizdwp
 * 24/07/2019
 **/

@Dao
abstract class FavoriteDao : BaseDao<FavoriteTable> {

    @Query("SELECT * FROM table_favorite WHERE movie_id = :movieId")
    abstract fun getFavoritedMovieById(movieId: Int): FavoriteTable?

    @Query("SELECT * FROM table_favorite WHERE movie_id IS NOT 0")
    abstract fun getAllFavoritedMovies(): List<FavoriteTable>

    @Query("SELECT * FROM table_favorite WHERE tvshow_id = :tvShowId")
    abstract fun getFavoritedTvShowById(tvShowId: Int): FavoriteTable?

    @Query("SELECT * FROM table_favorite WHERE tvshow_id IS NOT 0")
    abstract fun getAllFavoritedTvShow(): List<FavoriteTable>


    /// Provider exclusive DAO

    @Query("DELETE FROM table_favorite WHERE _id = :id")
    abstract fun deleteById(id: Long): Int

    @Query("SELECT * FROM table_favorite")
    abstract fun getAllFavoritedProvider(): Cursor

    @Query("SELECT * FROM table_favorite WHERE _id = :id")
    abstract fun getAllFavoritedByIdProvider(id: Long): Cursor
}