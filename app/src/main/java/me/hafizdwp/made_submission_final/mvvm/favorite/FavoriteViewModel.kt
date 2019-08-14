package me.hafizdwp.made_submission_final.mvvm.favorite

import android.app.Application
import androidx.lifecycle.MutableLiveData
import me.hafizdwp.made_submission_final.R
import me.hafizdwp.made_submission_final.base.BaseViewModel
import me.hafizdwp.made_submission_final.data.MyRepository
import me.hafizdwp.made_submission_final.data.MyResponseCallback
import me.hafizdwp.made_submission_final.data.source.local.entity.FavoriteTable
import me.hafizdwp.made_submission_final.util.ext.call

/**
 * @author hafizdwp
 * 10/07/19
 **/
class FavoriteViewModel(
    application: Application, val mRepository: MyRepository
) : BaseViewModel(application) {

    val startProgress = MutableLiveData<Void>()
    val requestSuccess = MutableLiveData<Void>()
    val requestEmpty = MutableLiveData<Int>()
    val listMovieFavoritedLive = MutableLiveData<List<FavoriteTable>>()
    val listTvShowFavoritedLive = MutableLiveData<List<FavoriteTable>>()
    var isBothEmpty = 0


    fun getFavoritedMovies() {
        startProgress.call()
        mRepository.getMoviesFromFavorite(object : MyResponseCallback<List<FavoriteTable>> {
            override fun onDataAvailable(data: List<FavoriteTable>?) {
                requestSuccess.call()
                listMovieFavoritedLive.value = data
            }

            override fun onDataNotAvailable() {
                isBothEmpty++
            }

            override fun onError(code: Int?, errorMessage: String?) {}
        })
    }

    fun getFavoritedTvShows() {
        mRepository.getTvShowsFromFavorite(object : MyResponseCallback<List<FavoriteTable>> {
            override fun onDataAvailable(data: List<FavoriteTable>?) {
                requestSuccess.call()
                listTvShowFavoritedLive.value = data
            }

            override fun onDataNotAvailable() {
                isBothEmpty++

                if (isBothEmpty == 2) {
                    isBothEmpty = 0
                    requestEmpty.value = R.string.no_favorite
                }
            }

            override fun onError(code: Int?, errorMessage: String?) {}
        })
    }
}
