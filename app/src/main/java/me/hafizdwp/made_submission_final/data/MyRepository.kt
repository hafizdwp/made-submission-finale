package me.hafizdwp.made_submission_final.data

import kotlinx.coroutines.Deferred
import me.hafizdwp.made_submission_final.base.BaseApiModel
import me.hafizdwp.made_submission_final.data.source.MyDataSource
import me.hafizdwp.made_submission_final.data.source.local.MyLocalDataSource
import me.hafizdwp.made_submission_final.data.source.local.entity.FavoriteTable
import me.hafizdwp.made_submission_final.data.source.remote.MyRemoteDataSource
import me.hafizdwp.made_submission_final.data.source.remote.MyResponseCallback
import me.hafizdwp.made_submission_final.data.source.remote.model.GenreResponse
import me.hafizdwp.made_submission_final.data.source.remote.model.MovieDetailResponse
import me.hafizdwp.made_submission_final.data.source.remote.model.MovieResponse
import me.hafizdwp.made_submission_final.data.source.remote.model.TvShowDetailResponse
import me.hafizdwp.made_submission_final.data.source.remote.model.TvShowResponse
import me.hafizdwp.made_submission_final.mvvm.setting.AlarmType
import me.hafizdwp.made_submission_final.util.ext.asyncAwait

/**
 * @author hafizdwp
 * 10/07/19
 **/
open class MyRepository(
        val remoteDataSource: MyRemoteDataSource,
        val localDataSource: MyLocalDataSource) : MyDataSource {


    override fun getAlarmStatus(alarmType: AlarmType): Boolean {
        return localDataSource.getAlarmStatus(alarmType)
    }

    override fun saveAlarmStatus(alarmType: AlarmType, status: Boolean) {
        localDataSource.saveAlarmStatus(alarmType, status)
    }

    override suspend fun getAllFavorited(): Deferred<List<FavoriteTable>> {
        return asyncAwait { localDataSource.getAllFavorited() }
    }

    override suspend fun getTvShowBySearch(query: String): Deferred<BaseApiModel<List<TvShowResponse>>> {
        return asyncAwait { remoteDataSource.getTvShowBySearch(query = query) }
    }

    override suspend fun getMoviesBySearch(query: String): Deferred<BaseApiModel<List<MovieResponse>>> {
        return asyncAwait { remoteDataSource.getMoviesBySearch(query = query) }
    }

    override fun getPopularMovies(callback: MyResponseCallback<List<MovieResponse>>) {
        remoteDataSource.getPopularMovies(callback)
    }

    override fun getPopularTvShows(callback: MyResponseCallback<List<TvShowResponse>>) {
        remoteDataSource.getPopularTvShows(callback)
    }

    override fun getOnAirTvShows(callback: MyResponseCallback<List<TvShowResponse>>) {
        remoteDataSource.getOnAirTvShows(callback)
    }

    override fun getTvShowDetails(tvShowId: Int, callback: MyResponseCallback<TvShowDetailResponse>) {
        remoteDataSource.getTvShowDetails(tvShowId, callback)
    }

    override fun getMoviesGenre(callback: MyResponseCallback<List<GenreResponse>>) {
        remoteDataSource.getMoviesGenre(callback)
    }

    override fun getNowPlayingMovies(callback: MyResponseCallback<List<MovieResponse>>) {
        remoteDataSource.getNowPlayingMovies(callback)
    }

    override fun getMovieDetails(movieId: Int, callback: MyResponseCallback<MovieDetailResponse>) {
        remoteDataSource.getMovieDetails(movieId, callback)
    }

    override fun getTvShowsGenre(callback: MyResponseCallback<List<GenreResponse>>) {
        remoteDataSource.getTvShowsGenre(callback)
    }

    override fun saveDataToFavorite(favoriteTable: FavoriteTable) {
        localDataSource.saveDataToFavorite(favoriteTable)
    }

    override fun deleteDataFromFavorite(favoriteTable: FavoriteTable) {
        localDataSource.deleteDataFromFavorite(favoriteTable)
    }

    override fun getMovieFromFavorite(movieId: Int, callback: MyResponseCallback<FavoriteTable>) {
        localDataSource.getMovieFromFavorite(movieId, callback)
    }

    override fun getTvShowFromFavorite(tvShowId: Int, callback: MyResponseCallback<FavoriteTable>) {
        localDataSource.getTvShowFromFavorite(tvShowId, callback)
    }

    companion object {
        private var INSTANCE: MyRepository? = null

        /**
         * Returns the single instance of this class, creating it if necessary.
         */
        @JvmStatic
        fun getInstance(
                remoteDataSource: MyRemoteDataSource,
                localDataSource: MyLocalDataSource
        ) =
                INSTANCE
                        ?: synchronized(MyRepository::class.java) {
                            INSTANCE
                                    ?: MyRepository(remoteDataSource, localDataSource)
                                            .also { INSTANCE = it }
                        }
    }
}