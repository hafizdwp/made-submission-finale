package me.hafizdwp.made_submission_final.data.source.remote.api

import io.reactivex.Observable
import kotlinx.coroutines.Deferred
import me.hafizdwp.made_submission_final.base.BaseApiModel
import me.hafizdwp.made_submission_final.data.Const.getApiKey
import me.hafizdwp.made_submission_final.data.Const.getLanguagePreference
import me.hafizdwp.made_submission_final.data.source.remote.model.GenreResponse
import me.hafizdwp.made_submission_final.data.source.remote.model.MovieDetailResponse
import me.hafizdwp.made_submission_final.data.source.remote.model.MovieResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * @author hafizdwp
 * 10/07/19
 **/
interface MovieApi {

    @GET("discover/movie?sort_by=popularity.desc")
    fun getPopularMovies(
            @Query("api_key") apiKey: String = getApiKey(),
            @Query("language") language: String = getLanguagePreference()
    ): Observable<BaseApiModel<List<MovieResponse>>>

    @GET("genre/movie/list")
    fun getMoviesGenre(
            @Query("api_key") apiKey: String = getApiKey(),
            @Query("language") language: String = getLanguagePreference()
    ): Observable<BaseApiModel<List<GenreResponse>>>

    @GET("movie/now_playing")
    fun getNowPlayingMovies(
            @Query("api_key") apiKey: String = getApiKey(),
            @Query("language") language: String = getLanguagePreference()
    ): Observable<BaseApiModel<List<MovieResponse>>>

    @GET("movie/{movie_id}")
    fun getMovieDetail(
            @Path("movie_id") movieId: Int,
            @Query("api_key") apiKey: String = getApiKey(),
            @Query("language") language: String = getLanguagePreference()
    ): Observable<MovieDetailResponse>

    @GET("search/movie")
    fun getMovieBySearch(
            @Query("api_key") apiKey: String = getApiKey(),
            @Query("language") language: String = getLanguagePreference(),
            @Query("query") query: String
    ): Deferred<BaseApiModel<List<MovieResponse>>>
}