package me.hafizdwp.made_submission_final.mvvm.movie

import android.app.Application
import android.os.AsyncTask
import androidx.lifecycle.MutableLiveData
import me.hafizdwp.made_submission_final.base.BaseViewModel
import me.hafizdwp.made_submission_final.data.MyRepository
import me.hafizdwp.made_submission_final.data.source.remote.MyResponseCallback
import me.hafizdwp.made_submission_final.data.source.remote.model.GenreResponse
import me.hafizdwp.made_submission_final.data.source.remote.model.MovieResponse
import me.hafizdwp.made_submission_final.util.ext.call

/**
 * @author hafizdwp
 * 10/07/19
 **/
class MovieViewModel(application: Application,
                     val myRepository: MyRepository) : BaseViewModel(application) {

    val startProgress = MutableLiveData<Void>()
    val requestSuccess = MutableLiveData<Void>()
    val requestFailed = MutableLiveData<String>()
    val toast = MutableLiveData<String>()
    val listMoviesLive = MutableLiveData<List<MovieResponse>>()
    val listCarouselLive = MutableLiveData<List<MovieResponse>>()

    var mListMoviesGenre = ArrayList<GenreResponse>()


    fun getPopularMovies(listMoviesGenre: List<GenreResponse>) {

        mListMoviesGenre.clear()
        mListMoviesGenre.addAll(listMoviesGenre)

        myRepository.getPopularMovies(object :
            MyResponseCallback<List<MovieResponse>> {
            override fun onDataAvailable(data: List<MovieResponse>?) {
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

    fun getNowPlayingMovies() {

        myRepository.getNowPlayingMovies(object :
            MyResponseCallback<List<MovieResponse>> {
            override fun onDataAvailable(data: List<MovieResponse>?) {
                listCarouselLive.value = data
            }

            override fun onDataNotAvailable() {

            }

            override fun onError(code: Int?, errorMessage: String?) {
                toast.value = errorMessage
            }
        })
    }

    inner class MyAsyncTask : AsyncTask<List<MovieResponse>, Void, List<MovieResponse>>() {
        override fun doInBackground(vararg params: List<MovieResponse>?): List<MovieResponse> {
            val list = params[0]
            list?.let {
                for (i in list.indices) {

                    list[i].genre_ids?.let {
                        val tempListGenre = ArrayList<String>()

                        for (modelGenre in it) {
                            for (iterateGenre in mListMoviesGenre) {
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

        override fun onPostExecute(result: List<MovieResponse>?) {
            super.onPostExecute(result)
            requestSuccess.call()
            listMoviesLive.value = result
        }
    }
}
