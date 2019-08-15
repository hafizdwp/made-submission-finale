package me.hafizdwp.made_submission_final.data.source.remote.api

import io.reactivex.Observable
import kotlinx.coroutines.Deferred
import me.hafizdwp.made_submission_final.BuildConfig
import me.hafizdwp.made_submission_final.base.BaseApiModel
import me.hafizdwp.made_submission_final.data.Constant
import me.hafizdwp.made_submission_final.data.source.remote.model.GenreResponse
import me.hafizdwp.made_submission_final.data.source.remote.model.MovieDetailResponse
import me.hafizdwp.made_submission_final.data.source.remote.model.MovieResponse
import me.hafizdwp.made_submission_final.util.SharedPreferencesFactory.get
import me.hafizdwp.made_submission_final.util.ext.prefs
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
            @Query("api_key") apiKey: String = BuildConfig.BASE_API_KEY,
            @Query("language") language: String = prefs[Constant.PREF_LANGUAGE_API_QUERY] ?: "en-US"
    ): Observable<BaseApiModel<List<MovieResponse>>>

    @GET("genre/movie/list")
    fun getMoviesGenre(
            @Query("api_key") apiKey: String = BuildConfig.BASE_API_KEY,
            @Query("language") language: String = prefs[Constant.PREF_LANGUAGE_API_QUERY] ?: "en-US"
    ): Observable<BaseApiModel<List<GenreResponse>>>

    @GET("movie/now_playing")
    fun getNowPlayingMovies(
            @Query("api_key") apiKey: String = BuildConfig.BASE_API_KEY,
            @Query("language") language: String = prefs[Constant.PREF_LANGUAGE_API_QUERY] ?: "en-US"
    ): Observable<BaseApiModel<List<MovieResponse>>>

    @GET("movie/{movie_id}")
    fun getMovieDetail(
            @Path("movie_id") movieId: Int,
            @Query("api_key") apiKey: String = BuildConfig.BASE_API_KEY,
            @Query("language") language: String = prefs[Constant.PREF_LANGUAGE_API_QUERY] ?: "en-US"
    ): Observable<MovieDetailResponse>

//    @GET("search/movie?&query={MOVIE NAME}")
//    suspend fun getMovieBySearch(
//        @Query("api_key") apiKey: String = BuildConfig.BASE_API_KEY,
//        @Query("language") language: String = prefs[Constant.PREF_LANGUAGE_API_QUERY] ?: "en-US",
//        @Query("query") query: String
//    ): Observable<BaseApiModel<List<MovieResponse>>>

    @GET("search/movie?&query={MOVIE NAME}")
    fun getMovieBySearch(
            @Query("api_key") apiKey: String = BuildConfig.BASE_API_KEY,
            @Query("language") language: String = prefs[Constant.PREF_LANGUAGE_API_QUERY] ?: "en-US",
            @Query("query") query: String
    ): Deferred<BaseApiModel<List<MovieResponse>>>
}