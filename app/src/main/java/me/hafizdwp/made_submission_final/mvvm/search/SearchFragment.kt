package me.hafizdwp.made_submission_final.mvvm.search

import androidx.lifecycle.LifecycleOwner
import me.hafizdwp.made_submission_final.R
import me.hafizdwp.made_submission_final.base.BaseFragment
import me.hafizdwp.made_submission_final.mvvm.MainActivity
import me.hafizdwp.made_submission_final.util.ext.obtainViewModel
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

    override fun onViewReady() {
    }

    override fun start() {
    }

    override fun setupObserver(): SearchViewModel? {
        return mViewModel.apply {

        }
    }

    companion object {
        fun newInstance() = SearchFragment().withArgs { }
    }
}