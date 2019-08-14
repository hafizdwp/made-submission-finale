package me.hafizdwp.made_submission_final.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import me.hafizdwp.made_submission_final.data.source.local.entity.FavoriteTable
import me.hafizdwp.made_submission_final.util.ext.fromJson

/**
 * @author hafizdwp
 * 10/07/19
 **/
@Parcelize
data class MovieResponse(
    var vote_count: Int? = null,
    var id: Int? = null,
    var video: Boolean? = null,
    var vote_average: Double? = null,
    var title: String? = null,
    var popularity: Double? = null,
    var poster_path: String? = null,
    var original_language: String? = null,
    var original_title: String? = null,
    var genre_ids: List<Int?>? = null,
    var backdrop_path: String? = null,
    var adult: Boolean? = null,
    var overview: String? = null,
    var release_date: String? = null,

    // self-made variable
    var listGenre: List<String>? = null
) : Parcelable {
    companion object {
        fun from(favoriteTable: FavoriteTable): MovieResponse {
            with(favoriteTable) {
                return MovieResponse(
                    id = movie_id,
                    title = title,
                    poster_path = poster_path,
                    backdrop_path = backdrop_path,
                    listGenre = listGenre?.fromJson()
                )
            }
        }
    }
}