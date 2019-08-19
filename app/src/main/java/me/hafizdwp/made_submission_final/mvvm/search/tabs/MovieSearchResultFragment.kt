package me.hafizdwp.made_submission_final.mvvm.search.tabs

import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import kotlinx.android.parcel.Parcelize
import kotlinx.android.synthetic.main.movie_search_result_fragment.*
import me.hafizdwp.made_submission_final.R
import me.hafizdwp.made_submission_final.base.BaseFragment
import me.hafizdwp.made_submission_final.data.source.remote.model.MovieResponse
import me.hafizdwp.made_submission_final.mvvm.MainActivity
import me.hafizdwp.made_submission_final.mvvm.detail.DetailActivity
import me.hafizdwp.made_submission_final.mvvm.movie.MovieActionListener
import me.hafizdwp.made_submission_final.mvvm.search.SearchViewModel
import me.hafizdwp.made_submission_final.util.ext.obtainViewModel
import me.hafizdwp.made_submission_final.util.ext.withArgs

/**
 * @author hafizdwp
 * 15/08/2019
 **/
@Parcelize
class MovieSearchResultFragment : BaseFragment<MainActivity, MovieSearchResultViewModel>(), MovieActionListener {

    override val bindLayoutRes: Int
        get() = R.layout.movie_search_result_fragment
    override val mViewModel: MovieSearchResultViewModel
        get() = obtainViewModel()
    override val mLifecycleOwner: LifecycleOwner
        get() = this@MovieSearchResultFragment

    var mAdapter: MovieSearchResultAdapter? = null
    val mListMovies = ArrayList<MovieResponse>()


    override fun onViewReady() {

        mAdapter = MovieSearchResultAdapter(
                listItem = mListMovies,
                glide = Glide.with(this@MovieSearchResultFragment),
                actionListener = this@MovieSearchResultFragment)

        recyclerView.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    override fun start() {

    }

    override fun setupObserver(): MovieSearchResultViewModel? {
        return mViewModel.apply {
            listMoviesLive.observe {
                it?.let { list ->
                    mListMovies.clear()
                    mListMovies.addAll(list)
                    mAdapter?.notifyDataSetChanged()
                }
            }

            startProgress.observe {
                msrProgressView.startProgress()
            }

            requestSuccess.observe {
                msrProgressView.stopAndGone()
            }

            requestEmpty.observe { emptyMsg ->
                emptyMsg?.let {
                    msrProgressView.stopAndError(emptyMsg, false)
                }
            }

            requestFailed.observe { errorMsg ->
                errorMsg?.let {
                    msrProgressView.stopAndError(errorMsg, true)
                    msrProgressView.setRetryClickListener {
                        val parentViewModel = obtainViewModel<SearchViewModel>()
                        parentViewModel.getMovieBySearch(
                                query = parentViewModel.tempMovieQuery,
                                isCalledFromChild = true)
                    }
                }
            }
        }
    }


    /**
     * MovieActionListener implementation
     * ---------------------------------------------------------------------------------------------
     * */
    override fun onMovieClick(movieId: Int, movieResponse: MovieResponse) {
        DetailActivity.startActivity(
                context = requireContext(),
                movieId = movieId,
                movieResponse = movieResponse)
    }


    companion object {
        fun newInstance() = MovieSearchResultFragment().withArgs { }
    }
}