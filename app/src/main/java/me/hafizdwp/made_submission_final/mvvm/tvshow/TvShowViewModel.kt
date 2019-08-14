package me.hafizdwp.made_submission_final.mvvm.tvshow

import android.app.Application
import android.os.AsyncTask
import androidx.lifecycle.MutableLiveData
import me.hafizdwp.made_submission_final.base.BaseViewModel
import me.hafizdwp.made_submission_final.data.MyRepository
import me.hafizdwp.made_submission_final.data.MyResponseCallback
import me.hafizdwp.made_submission_final.data.source.remote.model.GenreResponse
import me.hafizdwp.made_submission_final.data.source.remote.model.TvShowResponse
import me.hafizdwp.made_submission_final.util.ext.call

/**
 * @author hafizdwp
 * 10/07/19
 **/
class TvShowViewModel(application: Application,
                      val mRepository: MyRepository) : BaseViewModel(application) {

    val startProgress = MutableLiveData<Void>()
    val requestSuccess = MutableLiveData<Void>()
    val requestFailed = MutableLiveData<String>()
    val toast = MutableLiveData<String>()
    val listTvShowsLive = MutableLiveData<List<TvShowResponse>>()
    val listCarouselLive = MutableLiveData<List<TvShowResponse>>()

    var mListTvShowsGenre = ArrayList<GenreResponse>()


    fun getPopularTvShows(listMoviesGenre: List<GenreResponse>) {

        mListTvShowsGenre.clear()
        mListTvShowsGenre.addAll(listMoviesGenre)

        mRepository.getPopularTvShows(object : MyResponseCallback<List<TvShowResponse>> {
            override fun onDataAvailable(data: List<TvShowResponse>?) {
                MyAsyncTask().execute(data)
            }

            override fun onDataNotAvailable() {

            }

            override fun onError(code: Int?, errorMessage: String?) {
                requestFailed.call()
                toast.value = errorMessage
            }
        })
    }

    fun getOnAirTvShows() {

        mRepository.getOnAirTvShows(object :MyResponseCallback<List<TvShowResponse>> {
            override fun onDataAvailable(data: List<TvShowResponse>?) {
                listCarouselLive.value = data
            }

            override fun onDataNotAvailable() {

            }

            override fun onError(code: Int?, errorMessage: String?) {
                toast.value = "Gagal"
            }
        })
    }

    inner class MyAsyncTask : AsyncTask<List<TvShowResponse>, Void, List<TvShowResponse>>() {
        override fun doInBackground(vararg params: List<TvShowResponse>?): List<TvShowResponse> {
            val list = params[0]
            list?.let {
                for (i in list.indices) {

                    list[i].genre_ids?.let {
                        val tempListGenre = ArrayList<String>()

                        for (modelGenre in it) {
                            for (iterateGenre in mListTvShowsGenre) {
                                if (modelGenre == iterateGenre.id) {
                                    tempListGenre.add(iterateGenre.name ?: "")
                                    break
                                }
                            }
                        }

                        list[i].listGenre = tempListGenre
                    }
                }
            }

            return list!!
        }

        override fun onPostExecute(result: List<TvShowResponse>?) {
            super.onPostExecute(result)
            requestSuccess.call()
            listTvShowsLive.value = result
        }
    }

}
