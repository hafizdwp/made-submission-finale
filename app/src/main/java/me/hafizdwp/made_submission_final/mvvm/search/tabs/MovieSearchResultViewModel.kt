package me.hafizdwp.made_submission_final.mvvm.search.tabs

import android.app.Application
import androidx.lifecycle.MutableLiveData
import me.hafizdwp.made_submission_final.base.BaseViewModel
import me.hafizdwp.made_submission_final.data.MyRepository
import me.hafizdwp.made_submission_final.data.source.remote.model.MovieResponse

/**
 * @author hafizdwp
 * 15/08/2019
 **/
class MovieSearchResultViewModel(application: Application,
                                 private val mRepository: MyRepository) : BaseViewModel(application) {

    val listMoviesLive = MutableLiveData<List<MovieResponse>>()
}