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
                    mViewModel.getMovieBySearch(newText)
                    mViewModel.getTvShowBySearch(newText)
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


    override fun onViewReady() {
        textSearchLabel.setOnClickListener {
            searchView?.isIconified = false
        }
        searchView.setOnQueryTextListener(searchQueryTextListener)
        searchView.addOnLayoutChangeListener(searchViewExpandCollapseListener)
    }

    override fun start() {

    }

    override fun setupObserver(): SearchViewModel? {
        return mViewModel.apply {
            toast.observe {
                toast(it.toString())
            }
        }
    }

    companion object {
        fun newInstance() = SearchFragment().withArgs { }
    }
}