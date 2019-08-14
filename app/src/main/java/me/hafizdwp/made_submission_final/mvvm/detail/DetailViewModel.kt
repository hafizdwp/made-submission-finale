package me.hafizdwp.made_submission_final.mvvm.detail

import android.app.Application
import androidx.lifecycle.MutableLiveData
import me.hafizdwp.made_submission_final.R
import me.hafizdwp.made_submission_final.base.BaseViewModel
import me.hafizdwp.made_submission_final.data.MyRepository
import me.hafizdwp.made_submission_final.data.MyResponseCallback
import me.hafizdwp.made_submission_final.data.source.remote.model.MovieDetailResponse
import me.hafizdwp.made_submission_final.data.source.remote.model.MovieResponse
import me.hafizdwp.made_submission_final.data.source.remote.model.TvShowDetailResponse
import me.hafizdwp.made_submission_final.data.source.remote.model.TvShowResponse
import me.hafizdwp.made_submission_final.data.source.local.entity.FavoriteTable
import me.hafizdwp.made_submission_final.util.ext.call

/**
 * @author hafizdwp
 * 11/07/19
 **/
class DetailViewModel(
        application: Application,
        val mRepository: MyRepository
) : BaseViewModel(application) {

    val startProgress = MutableLiveData<Void>()
    val requestSuccess = MutableLiveData<Void>()
    val requestFailed = MutableLiveData<String>()
    val detailMovieLive = MutableLiveData<MovieDetailResponse>()
    val detailTvShowLive = MutableLiveData<TvShowDetailResponse>()
    val favoriteTableLive = MutableLiveData<FavoriteTable?>()

    val toast = MutableLiveData<Int>()


    fun getMovieDetail(movieId: Int) {

        startProgress.call()
        mRepository.getMovieDetails(movieId, object : MyResponseCallback<MovieDetailResponse> {
            override fun onDataAvailable(data: MovieDetailResponse?) {
                requestSuccess.call()
                detailMovieLive.value = data
            }

            override fun onDataNotAvailable() {}

            override fun onError(code: Int?, errorMessage: String?) {
                requestFailed.value = errorMessage
            }
        })
    }

    fun getTvShowDetail(tvShowId: Int) {

        startProgress.call()
        mRepository.getTvShowDetails(tvShowId, object : MyResponseCallback<TvShowDetailResponse> {
            override fun onDataAvailable(data: TvShowDetailResponse?) {
                requestSuccess.call()
                detailTvShowLive.value = data
            }

            override fun onDataNotAvailable() {}

            override fun onError(code: Int?, errorMessage: String?) {
                requestFailed.value = errorMessage
            }
        })
    }


    /**
     * Movie locals
     * ---------------------------------------------------------------------------------------------
     * */

    fun saveMovieToFavorite(movieResponse: MovieResponse) {
        val favoriteTable = FavoriteTable.from(movieResponse)
        mRepository.saveDataToFavorite(favoriteTable)

        toast.value = R.string.movie_favorite_added_msg

        // Call to refresh the favorite state
        getMovieFromFavorite(movieResponse.id!!)
    }

    fun deleteMovieFromFavorite(favoriteTable: FavoriteTable) {
        mRepository.deleteDataFromFavorite(favoriteTable)

        toast.value = R.string.movie_favorite_removed_msg

        // Call to refresh the favorite state
        getMovieFromFavorite(favoriteTable.movie_id!!)
    }

    fun getMovieFromFavorite(movieId: Int) {

        mRepository.getMovieFromFavorite(movieId, object : MyResponseCallback<FavoriteTable> {
            override fun onDataAvailable(data: FavoriteTable?) {
                favoriteTableLive.value = data
            }

            override fun onDataNotAvailable() {
                favoriteTableLive.value = null
            }

            override fun onError(code: Int?, errorMessage: String?) {}
        })
    }

    /**
     * TvShow locals
     * ---------------------------------------------------------------------------------------------
     * */

    fun saveTvShowToFavorite(tvShowResponse: TvShowResponse) {
        val favoriteTable = FavoriteTable.from(tvShowResponse)
        mRepository.saveDataToFavorite(favoriteTable)

        toast.value = R.string.tvshow_favorite_added_msg

        // Call to refresh the favorite state
        getTvShowFromFavorite(tvShowResponse.id!!)
    }

    fun deleteTvShowFromFavorite(favoriteTable: FavoriteTable) {
        mRepository.deleteDataFromFavorite(favoriteTable)

        toast.value = R.string.tvshow_favorite_removed_msg

        // Call to refresh the favorite state
        getTvShowFromFavorite(favoriteTable.tvshow_id!!)
    }

    fun getTvShowFromFavorite(tvShowId: Int) {

        mRepository.getTvShowFromFavorite(tvShowId, object : MyResponseCallback<FavoriteTable> {
            override fun onDataAvailable(data: FavoriteTable?) {
                favoriteTableLive.value = data
            }

            override fun onDataNotAvailable() {
                favoriteTableLive.value = null
            }

            override fun onError(code: Int?, errorMessage: String?) {}
        })
    }
}