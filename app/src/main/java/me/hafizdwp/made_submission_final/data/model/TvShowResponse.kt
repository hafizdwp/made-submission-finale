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
data class TvShowResponse(
    var original_name: String? = null,
    var genre_ids: List<Int?>? = null,
    var name: String? = null,
    var popularity: Double? = null,
    var origin_country: List<String?>? = null,
    var vote_count: Int? = null,
    var first_air_date: String? = null,
    var backdrop_path: String? = null,
    var original_language: String? = null,
    var id: Int? = null,
    var vote_average: Double? = null,
    var overview: String? = null,
    var poster_path: String? = null,

    // self-made variable
    var listGenre: List<String>?
) : Parcelable {
    companion object {
        fun from(favoriteTable: FavoriteTable): TvShowResponse {
            with(favoriteTable) {
                return TvShowResponse(
                    id = movie_id,
                    name = title,
                    poster_path = poster_path,
                    backdrop_path = backdrop_path,
                    listGenre = listGenre?.fromJson()
                )
            }
        }
    }
}