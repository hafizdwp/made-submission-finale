package me.hafizdwp.made_submission_final.data.source.remote

import me.hafizdwp.made_submission_final.base.BaseApiModel
import me.hafizdwp.made_submission_final.data.ApiServiceFactory
import me.hafizdwp.made_submission_final.data.MyApiCallback
import me.hafizdwp.made_submission_final.data.MyResponseCallback
import me.hafizdwp.made_submission_final.data.source.remote.api.MovieApi
import me.hafizdwp.made_submission_final.data.source.remote.api.TvShowApi
import me.hafizdwp.made_submission_final.data.source.remote.model.GenreResponse
import me.hafizdwp.made_submission_final.data.source.remote.model.MovieDetailResponse
import me.hafizdwp.made_submission_final.data.source.remote.model.MovieResponse
import me.hafizdwp.made_submission_final.data.source.remote.model.TvShowDetailResponse
import me.hafizdwp.made_submission_final.data.source.remote.model.TvShowResponse
import me.hafizdwp.made_submission_final.data.source.MyDataSource
import me.hafizdwp.made_submission_final.util.ext.onCallback

/**
 * @author hafizdwp
 * 10/07/19
 **/
object MyRemoteDataSource : MyDataSource {

    val movieApi by lazy { ApiServiceFactory.builder<MovieApi>() }
    val tvShowApi by lazy { ApiServiceFactory.builder<TvShowApi>() }

    override fun getPopularMovies(callback: MyResponseCallback<List<MovieResponse>>) {

        movieApi.getPopularMovies().onCallback(
                object : MyApiCallback<BaseApiModel<List<MovieResponse>>>() {
                    override fun onSuccess(data: BaseApiModel<List<MovieResponse>>) {
                        callback.onDataAvailable(data.results)
                    }

                    override fun onFailure(code: Int, errorMessage: String) {
                        callback.onError(code, errorMessage)
                    }
                })
    }

    override fun getPopularTvShows(callback: MyResponseCallback<List<TvShowResponse>>) {

        tvShowApi.getPopularTvShows().onCallback(
                object : MyApiCallback<BaseApiModel<List<TvShowResponse>>>() {
                    override fun onSuccess(data: BaseApiModel<List<TvShowResponse>>) {
                        callback.onDataAvailable(data.results)
                    }

                    override fun onFailure(code: Int, errorMessage: String) {
                        callback.onError(code, errorMessage)
                    }
                }
        )
    }

    override fun getTvShowDetails(tvShowId: Int, callback: MyResponseCallback<TvShowDetailResponse>) {

        tvShowApi.getTvShowDetail(tvShowId = tvShowId).onCallback(
                object: MyApiCallback<TvShowDetailResponse>() {
                    override fun onSuccess(data: TvShowDetailResponse) {
                        callback.onDataAvailable(data)
                    }

                    override fun onFailure(code: Int, errorMessage: String) {
                        callback.onError(code, errorMessage)
                    }
                }
        )
    }

    override fun getMoviesGenre(callback: MyResponseCallback<List<GenreResponse>>) {

        movieApi.getMoviesGenre().onCallback(
                object : MyApiCallback<BaseApiModel<List<GenreResponse>>>() {
                    override fun onSuccess(data: BaseApiModel<List<GenreResponse>>) {
                        callback.onDataAvailable(data.genres)
                    }

                    override fun onFailure(code: Int, errorMessage: String) {
                        callback.onError(code, errorMessage)
                    }
                }
        )
    }

    override fun getNowPlayingMovies(callback: MyResponseCallback<List<MovieResponse>>) {

        movieApi.getNowPlayingMovies().onCallback(
                object : MyApiCallback<BaseApiModel<List<MovieResponse>>>() {
                    override fun onSuccess(data: BaseApiModel<List<MovieResponse>>) {
                        callback.onDataAvailable(data.results)
                    }

                    override fun onFailure(code: Int, errorMessage: String) {
                        callback.onError(code, errorMessage)
                    }
                }
        )
    }

    override fun getMovieDetails(movieId: Int, callback: MyResponseCallback<MovieDetailResponse>) {

        movieApi.getMovieDetail(movieId = movieId).onCallback(
                object : MyApiCallback<MovieDetailResponse>() {
                    override fun onSuccess(data: MovieDetailResponse) {
                        callback.onDataAvailable(data)
                    }

                    override fun onFailure(code: Int, errorMessage: String) {
                        callback.onError(code, errorMessage)
                    }
                }
        )
    }

    override fun getOnAirTvShows(callback: MyResponseCallback<List<TvShowResponse>>) {

        tvShowApi.getOnAirTvShows().onCallback(
                object : MyApiCallback<BaseApiModel<List<TvShowResponse>>>() {
                    override fun onSuccess(data: BaseApiModel<List<TvShowResponse>>) {
                        callback.onDataAvailable(data.results)
                    }

                    override fun onFailure(code: Int, errorMessage: String) {
                        callback.onError(code, errorMessage)
                    }
                }
        )
    }

    override fun getTvShowsGenre(callback: MyResponseCallback<List<GenreResponse>>) {

        tvShowApi.getTvShowsGenre().onCallback(
                object : MyApiCallback<BaseApiModel<List<GenreResponse>>>() {
                    override fun onSuccess(data: BaseApiModel<List<GenreResponse>>) {
                        callback.onDataAvailable(data.genres)
                    }

                    override fun onFailure(code: Int, errorMessage: String) {
                        callback.onError(code, errorMessage)
                    }
                }
        )
    }
}