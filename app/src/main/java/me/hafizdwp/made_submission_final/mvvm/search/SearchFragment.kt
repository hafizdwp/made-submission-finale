package me.hafizdwp.made_submission_final.mvvm.search

import android.text.Editable
import android.text.TextWatcher
import androidx.lifecycle.LifecycleOwner
import kotlinx.android.synthetic.main.search_fragment.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import me.hafizdwp.made_submission_final.R
import me.hafizdwp.made_submission_final.base.BaseFragment
import me.hafizdwp.made_submission_final.mvvm.MainActivity
import me.hafizdwp.made_submission_final.util.ext.launch
import me.hafizdwp.made_submission_final.util.ext.obtainViewModel
import me.hafizdwp.made_submission_final.util.ext.toast
import me.hafizdwp.made_submission_final.util.ext.withArgs
import kotlin.system.measureTimeMillis

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

    var searchJob: Job? = null

    override fun onViewReady() {
        etSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(newText: CharSequence?, p1: Int, p2: Int, p3: Int) {
                searchJob?.cancel()
                searchJob = launch {
                    delay(2000L)
                    mViewModel.getMovieBySearch(newText.toString())
                    mViewModel.getTvShowBySearch(newText.toString())
                }
            }
        })
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