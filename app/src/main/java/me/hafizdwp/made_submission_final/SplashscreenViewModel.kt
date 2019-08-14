package me.hafizdwp.made_submission_final

import android.app.Application
import androidx.lifecycle.MutableLiveData
import me.hafizdwp.made_submission_final.base.BaseViewModel
import me.hafizdwp.made_submission_final.data.MyRepository
import me.hafizdwp.made_submission_final.data.MyResponseCallback
import me.hafizdwp.made_submission_final.data.source.remote.model.GenreResponse
import me.hafizdwp.made_submission_final.util.ext.call

/**
 * @author hafizdwp
 * 10/07/19
 **/
class SplashscreenViewModel(application: Application,
                            val mRepository: MyRepository) : BaseViewModel(application) {

    val startProgress = MutableLiveData<Void>()
    val requestSuccess = MutableLiveData<Void>()

    val listMoviesGenre = MutableLiveData<List<GenreResponse>>()
    val listTvShowsGenre = MutableLiveData<List<GenreResponse>>()

    fun start() {
        startProgress.call()
        getMoviesGenre()
    }

    private fun getMoviesGenre() {
        mRepository.getMoviesGenre(object : MyResponseCallback<List<GenreResponse>> {
            override fun onDataAvailable(data: List<GenreResponse>?) {
                listMoviesGenre.postValue(data)
                getTvShowsGenre()
            }

            override fun onDataNotAvailable() {}

            override fun onError(code: Int?, errorMessage: String?) {
                requestSuccess.call()
            }
        })
    }

    private fun getTvShowsGenre() {

        mRepository.getTvShowsGenre(object : MyResponseCallback<List<GenreResponse>> {
            override fun onDataAvailable(data: List<GenreResponse>?) {
                listTvShowsGenre.postValue(data)
                requestSuccess.call()
            }

            override fun onDataNotAvailable() {}

            override fun onError(code: Int?, errorMessage: String?) {
                requestSuccess.call()
            }
        })
    }
}