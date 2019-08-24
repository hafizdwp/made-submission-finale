package me.hafizdwp.made_submission_final.mvvm.favorite

import android.app.Application
import androidx.lifecycle.MutableLiveData
import me.hafizdwp.made_submission_final.R
import me.hafizdwp.made_submission_final.base.BaseViewModel
import me.hafizdwp.made_submission_final.data.MyRepository
import me.hafizdwp.made_submission_final.data.source.local.entity.FavoriteTable
import me.hafizdwp.made_submission_final.util.ext.await
import me.hafizdwp.made_submission_final.util.ext.call
import me.hafizdwp.made_submission_final.util.ext.launch

/**
 * @author hafizdwp
 * 10/07/19
 **/
class FavoriteViewModel(
    application: Application,
    val mRepository: MyRepository
) : BaseViewModel(application) {

    val startProgress = MutableLiveData<Void>()
    val requestSuccess = MutableLiveData<Void>()
    val requestEmpty = MutableLiveData<Int>()
    val requestFailed = MutableLiveData<String>()
    val listMovieFavoritedLive = MutableLiveData<List<FavoriteTable>>()
    val listTvShowFavoritedLive = MutableLiveData<List<FavoriteTable>>()


    fun getAllFavorited() {
        launch {
            try {
                startProgress.call()

                val listFavorited: List<FavoriteTable> =
                    await { mRepository.getAllFavorited() }
                val listMovieModel = ArrayList<FavoriteTable>()
                val listTvModel = ArrayList<FavoriteTable>()

                if (listFavorited.isNotEmpty()) {
                    listFavorited.forEach { model ->
                        when {
                            model.movie_id != 0 ->
                                listMovieModel.add(model)
                            model.tvshow_id != 0 ->
                                listTvModel.add(model)
                        }
                    }

                    listMovieFavoritedLive.value = listMovieModel
                    listTvShowFavoritedLive.value = listTvModel

                    requestSuccess.call()
                } else {
                    requestEmpty.value = R.string.no_favorite
                }

            } catch (t: Throwable) {
                requestFailed.value = t.getErrorMessage<FavoriteTable>()
            }
        }
    }
}
