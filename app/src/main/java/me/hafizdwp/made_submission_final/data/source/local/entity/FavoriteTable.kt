package me.hafizdwp.made_submission_final.data.source.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import me.hafizdwp.made_submission_final.data.source.remote.model.MovieResponse
import me.hafizdwp.made_submission_final.data.source.remote.model.TvShowResponse
import me.hafizdwp.made_submission_final.util.ext.toJson

/**
 * @author hafizdwp
 * 24/07/2019
 **/

@Entity(tableName = "table_favorite")
data class FavoriteTable(

        @PrimaryKey(autoGenerate = true)
        var id: Int = 0,

        var movie_id: Int? = 0,
        var tvshow_id: Int? = 0,
        var title: String?,
        var poster_path: String?,
        var backdrop_path: String?,

        // self-made variable
        var listGenre: String? //List<String>?
) {
    companion object {
        fun from(movieResponse: MovieResponse): FavoriteTable {
            with(movieResponse) {
                return FavoriteTable(
                        movie_id = id,
                        title = title,
                        poster_path = poster_path,
                        backdrop_path = backdrop_path,
                        listGenre = listGenre.toJson()
                )
            }
        }

        fun from(tvShowResponse: TvShowResponse): FavoriteTable {
            with(tvShowResponse) {
                return FavoriteTable(
                        tvshow_id = id,
                        title = name,
                        poster_path = poster_path,
                        backdrop_path = backdrop_path,
                        listGenre = listGenre.toJson()
                )
            }
        }
    }
}