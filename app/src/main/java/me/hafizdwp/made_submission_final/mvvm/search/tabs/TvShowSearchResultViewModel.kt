package me.hafizdwp.made_submission_final.mvvm.search.tabs

import android.app.Application
import androidx.lifecycle.MutableLiveData
import me.hafizdwp.made_submission_final.base.BaseViewModel
import me.hafizdwp.made_submission_final.data.MyRepository
import me.hafizdwp.made_submission_final.data.source.remote.model.TvShowResponse

/**
 * @author hafizdwp
 * 15/08/2019
 **/
class TvShowSearchResultViewModel(application: Application,
                                  private val mRepository: MyRepository) : BaseViewModel(application) {

    val listTvShowsLive = MutableLiveData<List<TvShowResponse>>()
    val startProgress = MutableLiveData<Void>()
    val requestSuccess = MutableLiveData<Void>()
    val requestEmpty = MutableLiveData<String>()
    val requestFailed = MutableLiveData<String>()
}