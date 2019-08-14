package me.hafizdwp.made_submission_final.data.source

import me.hafizdwp.made_submission_final.data.source.remote.MyResponseCallback
import me.hafizdwp.made_submission_final.data.source.remote.model.GenreResponse
import me.hafizdwp.made_submission_final.data.source.remote.model.MovieDetailResponse
import me.hafizdwp.made_submission_final.data.source.remote.model.MovieResponse
import me.hafizdwp.made_submission_final.data.source.remote.model.TvShowDetailResponse
import me.hafizdwp.made_submission_final.data.source.remote.model.TvShowResponse
import me.hafizdwp.made_submission_final.data.source.local.entity.FavoriteTable

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


    ///
    /// Local
    ///

    fun saveDataToFavorite(favoriteTable: FavoriteTable) {}
    fun deleteDataFromFavorite(favoriteTable: FavoriteTable) {}
    fun getMovieFromFavorite(movieId: Int, callback: MyResponseCallback<FavoriteTable>) {}
    fun getMoviesFromFavorite(callback: MyResponseCallback<List<FavoriteTable>>) {}
    fun getTvShowFromFavorite(tvShowId: Int, callback: MyResponseCallback<FavoriteTable>) {}
    fun getTvShowsFromFavorite(callback: MyResponseCallback<List<FavoriteTable>>) {}
}