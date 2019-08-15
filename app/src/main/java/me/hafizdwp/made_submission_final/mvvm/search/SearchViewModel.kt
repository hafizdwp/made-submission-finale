package me.hafizdwp.made_submission_final.mvvm.search

import android.app.Application
import androidx.lifecycle.MutableLiveData
import me.hafizdwp.made_submission_final.base.BaseApiModel
import me.hafizdwp.made_submission_final.base.BaseViewModel
import me.hafizdwp.made_submission_final.data.MyRepository
import me.hafizdwp.made_submission_final.data.source.remote.model.MovieResponse
import me.hafizdwp.made_submission_final.data.source.remote.model.TvShowResponse
import me.hafizdwp.made_submission_final.util.ext.await
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

    fun getMovieBySearch(query: String) = doIfQueryValid(query) {
        launch {
            try {
                val response = await { mRepository.getMoviesBySearch(query) }
                listMoviesLive.value = response.results

            } catch (e: Throwable) {
                toast.value = e.getErrorMessage<BaseApiModel<List<MovieResponse>>>()
            }
        }
    }

    fun getTvShowBySearch(query: String) = doIfQueryValid(query) {
        launch {
            try {
                val response = await { mRepository.getTvShowBySearch(query) }
                listTvShowsLive.value = response.results

            } catch (e: Throwable) {
                toast.value = e.getErrorMessage<BaseApiModel<List<TvShowResponse>>>()
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
