package me.hafizdwp.made_submission_final.mvvm.search

import android.app.Application
import androidx.lifecycle.MutableLiveData
import me.hafizdwp.made_submission_final.base.BaseApiModel
import me.hafizdwp.made_submission_final.base.BaseViewModel
import me.hafizdwp.made_submission_final.data.MyRepository
import me.hafizdwp.made_submission_final.data.source.remote.model.MovieResponse
import me.hafizdwp.made_submission_final.data.source.remote.model.TvShowResponse
import me.hafizdwp.made_submission_final.util.ext.await
import me.hafizdwp.made_submission_final.util.ext.call
import me.hafizdwp.made_submission_final.util.ext.launch

/**
 * @author hafizdwp
 * 15/08/2019
 **/
class SearchViewModel(application: Application,
                      private val mRepository: MyRepository) : BaseViewModel(application) {

    val toast = MutableLiveData<String>()
    val listMoviesLive = MutableLiveData<List<MovieResponse>>()
    val listTvShowsLive = MutableLiveData<List<TvShowResponse>>()

    val mvStartProgress = MutableLiveData<Void>()
    val mvRequestSuccess = MutableLiveData<Void>()
    val mvRequestEmpty = MutableLiveData<String>()
    val mvRequestFailed = MutableLiveData<String>()

    val tvStartProgress = MutableLiveData<Void>()
    val tvRequestSuccess = MutableLiveData<Void>()
    val tvRequestEmpty = MutableLiveData<String>()
    val tvRequestFailed = MutableLiveData<String>()

    var tempMovieQuery = ""
    var tempTvShowQuery = ""

    fun getMovieBySearch(query: String = "",
                         isCalledFromChild: Boolean = false) = doIfQueryValid(query) {

        tempMovieQuery = query

        launch {
            try {

                mvStartProgress.call()

                val response = await {
                    mRepository.getMoviesBySearch(
                            query = if (!isCalledFromChild) query
                            else tempMovieQuery
                    )
                }
                val listMovies = response.results
                if (listMovies.isNullOrEmpty())
                    mvRequestEmpty.value = "Belum ada konten"
                else {
                    mvRequestSuccess.call()
                    listMoviesLive.value = response.results
                }


            } catch (e: Throwable) {
                mvRequestFailed.value = e.getErrorMessage<BaseApiModel<List<MovieResponse>>>()
            }
        }
    }

    fun getTvShowBySearch(query: String = "",
                          isCalledFromChild: Boolean = false) = doIfQueryValid(query) {

        tempTvShowQuery = query

        launch {
            try {

                tvStartProgress.call()

                val response = await {
                    mRepository.getTvShowBySearch(
                            query = if (!isCalledFromChild) query
                            else tempTvShowQuery
                    )
                }
                val listTvShows = response.results
                if (listTvShows.isNullOrEmpty())
                    tvRequestEmpty.value = "Belum ada konten"
                else {
                    tvRequestSuccess.call()
                    listTvShowsLive.value = response.results
                }


            } catch (e: Throwable) {
                tvRequestFailed.value = e.getErrorMessage<BaseApiModel<List<TvShowResponse>>>()
            }
        }
    }

    private fun doIfQueryValid(
            query: String,
            todo: () -> Unit
    ) {
        if (query.isNotBlank() && query.length > 2)
            todo.invoke()
    }

}
