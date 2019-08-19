package me.hafizdwp.made_submission_final.mvvm.search

import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.LifecycleOwner
import kotlinx.android.synthetic.main.search_fragment.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import me.hafizdwp.made_submission_final.R
import me.hafizdwp.made_submission_final.base.BaseFragment
import me.hafizdwp.made_submission_final.mvvm.MainActivity
import me.hafizdwp.made_submission_final.mvvm.search.tabs.MovieSearchResultFragment
import me.hafizdwp.made_submission_final.mvvm.search.tabs.MovieSearchResultViewModel
import me.hafizdwp.made_submission_final.mvvm.search.tabs.TvShowSearchResultFragment
import me.hafizdwp.made_submission_final.mvvm.search.tabs.TvShowSearchResultViewModel
import me.hafizdwp.made_submission_final.util.ext.call
import me.hafizdwp.made_submission_final.util.ext.gone
import me.hafizdwp.made_submission_final.util.ext.launch
import me.hafizdwp.made_submission_final.util.ext.obtainViewModel
import me.hafizdwp.made_submission_final.util.ext.toast
import me.hafizdwp.made_submission_final.util.ext.visible
import me.hafizdwp.made_submission_final.util.ext.withArgs

/**
 * @author hafizdwp
 * 15/08/2019
 **/
class SearchFragment : BaseFragment<MainActivity, SearchViewModel>() {
    override val bindLayoutRes: Int
        get() = R.layout.search_fragment
    override val mViewModel: SearchViewModel
        get() = obtainViewModel()
    override val mLifecycleOwner: LifecycleOwner
        get() = this@SearchFragment

    var mMovieSearchResultVM: MovieSearchResultViewModel? = null
    var mTvShowSearchResultVM: TvShowSearchResultViewModel? = null

    val SEARCH_ON_TYPE_DELAY = 800L
    var searchJob: Job? = null
    val searchQueryTextListener = object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?): Boolean {
            return false
        }

        override fun onQueryTextChange(newText: String?): Boolean {
            newText?.let {
                searchJob?.cancel()
                searchJob = launch {

                    // Delay and execute
                    delay(SEARCH_ON_TYPE_DELAY)
                    try {
                        mViewModel.getMovieBySearch(newText)
                        mViewModel.getTvShowBySearch(newText)
                    } catch (e: java.lang.Exception) {
                    }
                }
            }
            return true
        }
    }
    val searchViewExpandCollapseListener = View.OnLayoutChangeListener { v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom ->
        try {
            val sView = v as SearchView
            if (!sView.isIconified) {
                textSearchLabel.gone()
            } else {
                textSearchLabel.visible()
            }
        } catch (e: Exception) {
        }
    }
    var mPagerAdapter: SearchPagerAdapter? = null


    override fun onViewReady() {
        textSearchLabel.setOnClickListener {
            searchView?.isIconified = false
        }
        searchView.setOnQueryTextListener(searchQueryTextListener)
        searchView.addOnLayoutChangeListener(searchViewExpandCollapseListener)

        mPagerAdapter = SearchPagerAdapter(childFragmentManager)
        viewPager.adapter = mPagerAdapter
        tabLayout.setupWithViewPager(viewPager)

        // Populate tabs
        mPagerAdapter?.addFragment(
                fragment = MovieSearchResultFragment.newInstance(),
                title = getString(R.string.movies))
        mPagerAdapter?.addFragment(
                fragment = TvShowSearchResultFragment.newInstance(),
                title = getString(R.string.tv_shows))

        viewPager.adapter?.notifyDataSetChanged()

        // Initiate tabs fragment's viewModel
        mMovieSearchResultVM = obtainViewModel()
        mTvShowSearchResultVM = obtainViewModel()
    }

    override fun start() {

    }

    override fun setupObserver(): SearchViewModel? {
        return mViewModel.apply {
            toast.observe {
                toast(it.toString())
            }

            ///
            /// Movies observer
            ///

            listMoviesLive.observe {
                it?.let { list ->

                    // Notify movie search item
                    mMovieSearchResultVM?.listMoviesLive?.value = list
                }
            }

            mvStartProgress.observe {
                mMovieSearchResultVM?.startProgress?.call()
            }

            mvRequestSuccess.observe {
                mMovieSearchResultVM?.requestSuccess?.call()
            }

            mvRequestEmpty.observe { emptyMsg ->
                emptyMsg?.let {
                    mMovieSearchResultVM?.requestEmpty?.value = it
                }
            }

            mvRequestFailed.observeForever { errorMsg ->
                errorMsg?.let {
                    mMovieSearchResultVM?.requestFailed?.value = it
                }
            }

            ///
            /// TvShows observer
            ///

            listTvShowsLive.observe {
                it?.let { list ->

                    // Notify tvshow search item
                    mTvShowSearchResultVM?.listTvShowsLive?.value = list
                }
            }

            tvStartProgress.observe {
                mTvShowSearchResultVM?.startProgress?.call()
            }

            tvRequestSuccess.observe {
                mTvShowSearchResultVM?.requestSuccess?.call()
            }

            tvRequestEmpty.observe { emptyMsg ->
                emptyMsg?.let {
                    mTvShowSearchResultVM?.requestEmpty?.value = it
                }
            }

            tvRequestFailed.observeForever { errorMsg ->
                errorMsg?.let {
                    mTvShowSearchResultVM?.requestFailed?.value = it
                }
            }
        }
    }

    companion object {
        fun newInstance() = SearchFragment().withArgs { }
    }
}