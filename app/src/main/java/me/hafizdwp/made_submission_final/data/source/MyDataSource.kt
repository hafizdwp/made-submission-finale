package me.hafizdwp.made_submission_final.data.source

import kotlinx.coroutines.Deferred
import me.hafizdwp.made_submission_final.base.BaseApiModel
import me.hafizdwp.made_submission_final.data.source.local.entity.FavoriteTable
import me.hafizdwp.made_submission_final.data.source.remote.MyResponseCallback
import me.hafizdwp.made_submission_final.data.source.remote.model.GenreResponse
import me.hafizdwp.made_submission_final.data.source.remote.model.MovieDetailResponse
import me.hafizdwp.made_submission_final.data.source.remote.model.MovieResponse
import me.hafizdwp.made_submission_final.data.source.remote.model.TvShowDetailResponse
import me.hafizdwp.made_submission_final.data.source.remote.model.TvShowResponse
import me.hafizdwp.made_submission_final.mvvm.setting.AlarmType
import me.hafizdwp.made_submission_final.util.ext.async

/**
 * @author hafizdwp
 * 10/07/19
 **/
interface MyDataSource {

    ///
    /// Remote
    ///

    fun getPopularMovies(callback: MyResponseCallback<List<MovieResponse>>) {}
    fun getMoviesGenre(callback: MyResponseCallback<List<GenreResponse>>) {}
    fun getNowPlayingMovies(callback: MyResponseCallback<List<MovieResponse>>) {}
    fun getMovieDetails(movieId: Int, callback: MyResponseCallback<MovieDetailResponse>) {}

    fun getPopularTvShows(callback: MyResponseCallback<List<TvShowResponse>>) {}
    fun getOnAirTvShows(callback: MyResponseCallback<List<TvShowResponse>>) {}
    fun getTvShowsGenre(callback: MyResponseCallback<List<GenreResponse>>) {}
    fun getTvShowDetails(tvShowId: Int, callback: MyResponseCallback<TvShowDetailResponse>) {}

    suspend fun getMoviesBySearch(query: String): Deferred<BaseApiModel<List<MovieResponse>>> {
        throw IllegalAccessException("please override and don't use super")
    }

    suspend fun getTvShowBySearch(query: String): Deferred<BaseApiModel<List<TvShowResponse>>> {
        throw IllegalAccessException("please override and don't use super")
    }


    ///
    /// Local
    ///

    suspend fun getAllFavorited(): Deferred<List<FavoriteTable>> {
        throw IllegalAccessException("please override and don't use super")
    }

    fun saveDataToFavorite(favoriteTable: FavoriteTable) {}
    fun deleteDataFromFavorite(favoriteTable: FavoriteTable) {}
    fun getMovieFromFavorite(movieId: Int, callback: MyResponseCallback<FavoriteTable>) {}
    fun getTvShowFromFavorite(tvShowId: Int, callback: MyResponseCallback<FavoriteTable>) {}

    fun saveAlarmStatus(alarmType: AlarmType, status: Boolean) {}
    fun getAlarmStatus(alarmType: AlarmType): Boolean {
        return false
    }
}