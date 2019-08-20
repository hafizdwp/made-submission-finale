package me.hafizdwp.made_submission_final.data.source.local.entity

import android.content.ContentValues
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import me.hafizdwp.made_submission_final.data.source.remote.model.MovieResponse
import me.hafizdwp.made_submission_final.data.source.remote.model.TvShowResponse
import me.hafizdwp.made_submission_final.util.ext.toJson

/**
 * @author hafizdwp
 * 24/07/2019
 **/

@Entity(tableName = FavoriteTable.TABLE_NAME)
data class FavoriteTable(

        @PrimaryKey(autoGenerate = true)
        @ColumnInfo(name = COLUMN_ID)
        var id: Int = 0,

        @ColumnInfo(name = COLUMN_MOVIE_ID)
        var movie_id: Int? = 0,

        @ColumnInfo(name = COLUMN_TVSHOW_ID)
        var tvshow_id: Int? = 0,

        @ColumnInfo(name = COLUMN_TITLE)
        var title: String?,

        @ColumnInfo(name = COLUMN_POSTER_PATH)
        var poster_path: String?,

        @ColumnInfo(name = COLUMN_BACKDROP_PATH)
        var backdrop_path: String?,

        // self-made variable
        @ColumnInfo(name = COLUMN_LIST_GENRE)
        var listGenre: String? //List<String>?
) {
    companion object {
        const val TABLE_NAME = "table_favorite"
        const val COLUMN_ID = "_id"
        const val COLUMN_MOVIE_ID = "movie_id"
        const val COLUMN_TVSHOW_ID = "tvshow_id"
        const val COLUMN_TITLE = "title"
        const val COLUMN_POSTER_PATH = "poster_path"
        const val COLUMN_BACKDROP_PATH = "backdrop_path"
        const val COLUMN_LIST_GENRE = "list_genre"


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

        fun fromContentValues(values: ContentValues): FavoriteTable {
            with(values) {
                return FavoriteTable(
                        movie_id = getAsInteger(COLUMN_MOVIE_ID),
                        tvshow_id = getAsInteger(COLUMN_TVSHOW_ID),
                        title = getAsString(COLUMN_TITLE),
                        poster_path = getAsString(COLUMN_POSTER_PATH),
                        backdrop_path = getAsString(COLUMN_BACKDROP_PATH),
                        listGenre = getAsString(COLUMN_LIST_GENRE)
                )
            }
        }

        fun toContentValues(data: FavoriteTable): ContentValues {
            return ContentValues().apply {
                put(COLUMN_ID, data.id)
                put(COLUMN_MOVIE_ID, data.movie_id)
                put(COLUMN_TVSHOW_ID, data.tvshow_id)
                put(COLUMN_TITLE, data.title)
                put(COLUMN_POSTER_PATH, data.poster_path)
                put(COLUMN_BACKDROP_PATH, data.backdrop_path)
                put(COLUMN_LIST_GENRE, data.listGenre)
            }
        }
    }
}