package me.hafizdwp.made_submission_final.mvvm.search.tabs

import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import kotlinx.android.parcel.Parcelize
import kotlinx.android.synthetic.main.tvshow_search_result_fragment.*
import me.hafizdwp.made_submission_final.R
import me.hafizdwp.made_submission_final.base.BaseFragment
import me.hafizdwp.made_submission_final.data.source.remote.model.TvShowResponse
import me.hafizdwp.made_submission_final.mvvm.MainActivity
import me.hafizdwp.made_submission_final.mvvm.detail.DetailActivity
import me.hafizdwp.made_submission_final.mvvm.search.SearchViewModel
import me.hafizdwp.made_submission_final.mvvm.tvshow.TvShowActionListener
import me.hafizdwp.made_submission_final.util.ext.obtainViewModel
import me.hafizdwp.made_submission_final.util.ext.toast
import me.hafizdwp.made_submission_final.util.ext.withArgs

/**
 * @author hafizdwp
 * 15/08/2019
 **/
@Parcelize
class TvShowSearchResultFragment : BaseFragment<MainActivity, TvShowSearchResultViewModel>(), TvShowActionListener {

    override val bindLayoutRes: Int
        get() = R.layout.tvshow_search_result_fragment
    override val mViewModel: TvShowSearchResultViewModel
        get() = obtainViewModel()
    override val mLifecycleOwner: LifecycleOwner
        get() = this@TvShowSearchResultFragment

    var mAdapter: TvShowSearchResultAdapter? = null
    val mListTvShow = ArrayList<TvShowResponse>()


    override fun onViewReady() {

        mAdapter = TvShowSearchResultAdapter(
                listItems = mListTvShow,
                glide = Glide.with(this@TvShowSearchResultFragment),
                actionListener = this@TvShowSearchResultFragment)

        recyclerView.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    override fun start() {
    }

    override fun setupObserver(): TvShowSearchResultViewModel? {
        return mViewModel.apply {
            listTvShowsLive.observe {
                it?.let { list ->
                    mListTvShow.clear()
                    mListTvShow.addAll(list)
                    mAdapter?.notifyDataSetChanged()
                }
            }

            startProgress.observe {
                tvrProgressView.startProgress()
            }

            requestSuccess.observe {
                tvrProgressView.stopAndGone()
            }

            requestEmpty.observe { emptyMsg ->
                emptyMsg?.let {
                    tvrProgressView.stopAndError(emptyMsg, false)
                }
            }

            requestFailed.observe { errorMsg ->
                errorMsg?.let {
                    tvrProgressView.stopAndError(errorMsg, true)
                    tvrProgressView.setRetryClickListener {
                        toast("tvshow")
                        val parentViewModel = obtainViewModel<SearchViewModel>()
                        parentViewModel.getTvShowBySearch(isCalledFromChild = true)
                    }
                }
            }
        }
    }

    /**
     * TvShowActionListener implementation
     * ---------------------------------------------------------------------------------------------
     * */
    override fun onTvShowClick(tvShowId: Int, tvShowResponse: TvShowResponse) {
        DetailActivity.startActivity(
                context = requireContext(),
                tvShowId = tvShowId,
                tvShowResponse = tvShowResponse)
    }

    companion object {
        fun newInstance() = TvShowSearchResultFragment().withArgs { }
    }
}