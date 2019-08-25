package me.hafizdwp.made_submission_final.data.source.remote.api

import io.reactivex.Observable
import kotlinx.coroutines.Deferred
import me.hafizdwp.made_submission_final.base.BaseApiModel
import me.hafizdwp.made_submission_final.data.Const.getApiKey
import me.hafizdwp.made_submission_final.data.Const.getLanguagePreference
import me.hafizdwp.made_submission_final.data.source.remote.model.GenreResponse
import me.hafizdwp.made_submission_final.data.source.remote.model.TvShowDetailResponse
import me.hafizdwp.made_submission_final.data.source.remote.model.TvShowResponse
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
            @Query("api_key") apiKey: String = getApiKey(),
            @Query("language") language: String = getLanguagePreference()
    ): Observable<BaseApiModel<List<TvShowResponse>>>

    @GET("genre/tv/list")
    fun getTvShowsGenre(
            @Query("api_key") apiKey: String = getApiKey(),
            @Query("language") language: String = getLanguagePreference()
    ): Observable<BaseApiModel<List<GenreResponse>>>

    @GET("tv/on_the_air")
    fun getOnAirTvShows(
            @Query("api_key") apiKey: String = getApiKey(),
            @Query("language") language: String = getLanguagePreference()
    ): Observable<BaseApiModel<List<TvShowResponse>>>

    @GET("tv/{tv_id}")
    fun getTvShowDetail(
            @Path("tv_id") tvShowId: Int,
            @Query("api_key") apiKey: String = getApiKey(),
            @Query("language") language: String = getLanguagePreference()
    ): Observable<TvShowDetailResponse>

    @GET("search/tv")
    fun getTvShowBySearch(
            @Query("api_key") apiKey: String = getApiKey(),
            @Query("language") language: String = getLanguagePreference(),
            @Query("query") query: String
    ): Deferred<BaseApiModel<List<TvShowResponse>>>
}