package me.hafizdwp.made_submission_final.data.source.remote.api

import io.reactivex.Observable
import me.hafizdwp.made_submission_final.BuildConfig
import me.hafizdwp.made_submission_final.base.BaseApiModel
import me.hafizdwp.made_submission_final.data.Constant
import me.hafizdwp.made_submission_final.data.source.remote.model.GenreResponse
import me.hafizdwp.made_submission_final.data.source.remote.model.TvShowDetailResponse
import me.hafizdwp.made_submission_final.data.source.remote.model.TvShowResponse
import me.hafizdwp.made_submission_final.util.SharedPreferencesFactory.get
import me.hafizdwp.made_submission_final.util.ext.prefs
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * @author hafizdwp
 * 10/07/19
 **/
interface TvShowApi {

    @GET("discover/tv?sort_by=popularity.desc")
    fun getPopularTvShows(
            @Query("api_key") apiKey: String = BuildConfig.BASE_API_KEY,
            @Query("language") language: String = prefs[Constant.PREF_LANGUAGE_API_QUERY] ?: "en-US"
    ): Observable<BaseApiModel<List<TvShowResponse>>>

    @GET("genre/tv/list")
    fun getTvShowsGenre(
            @Query("api_key") apiKey: String = BuildConfig.BASE_API_KEY,
            @Query("language") language: String = prefs[Constant.PREF_LANGUAGE_API_QUERY] ?: "en-US"
    ): Observable<BaseApiModel<List<GenreResponse>>>

    @GET("tv/on_the_air")
    fun getOnAirTvShows(
            @Query("api_key") apiKey: String = BuildConfig.BASE_API_KEY,
            @Query("language") language: String = prefs[Constant.PREF_LANGUAGE_API_QUERY] ?: "en-US"
    ): Observable<BaseApiModel<List<TvShowResponse>>>

    @GET("tv/{tv_id}")
    fun getTvShowDetail(
            @Path("tv_id") tvShowId: Int,
            @Query("api_key") apiKey: String = BuildConfig.BASE_API_KEY,
            @Query("language") language: String = prefs[Constant.PREF_LANGUAGE_API_QUERY] ?: "en-US"
    ): Observable<TvShowDetailResponse>
}