package me.hafizdwp.made_submission_final.mvvm.tvshow

import android.content.Intent
import android.os.Handler
import android.provider.Settings
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import kotlinx.android.parcel.Parcelize
import kotlinx.android.synthetic.main.tvshow_fragment.*
import me.hafizdwp.made_submission_final.R
import me.hafizdwp.made_submission_final.SplashscreenViewModel
import me.hafizdwp.made_submission_final.base.BaseFragment
import me.hafizdwp.made_submission_final.data.Const
import me.hafizdwp.made_submission_final.data.Pref
import me.hafizdwp.made_submission_final.data.source.remote.model.GenreResponse
import me.hafizdwp.made_submission_final.data.source.remote.model.TvShowResponse
import me.hafizdwp.made_submission_final.mvvm.MainActivity
import me.hafizdwp.made_submission_final.mvvm.detail.DetailActivity
import me.hafizdwp.made_submission_final.util.SharedPreferencesFactory.get
import me.hafizdwp.made_submission_final.util.SharedPreferencesFactory.set
import me.hafizdwp.made_submission_final.util.ext.*
import java.util.*
import kotlin.collections.ArrayList

/**
 * @author hafizdwp
 * 10/07/19
 **/
@Parcelize
class TvShowFragment : BaseFragment<MainActivity, TvShowViewModel>(), TvShowActionListener {

    override val bindLayoutRes: Int
        get() = R.layout.tvshow_fragment
    override val mViewModel: TvShowViewModel
        get() = obtainViewModel()
    override val mLifecycleOwner: LifecycleOwner
        get() = this@TvShowFragment

    private var mAdapter: TvShowAdapter? = null
    private var mCarouselAdapter: TvShowCarouselAdapter? = null
    private var mListMovies: ArrayList<TvShowResponse> = ArrayList()
    private var mListTvShowGenre: ArrayList<GenreResponse> = Pref.listTvShowsGenre?.fromJson()
        ?: ArrayList()
    private var mListCarousel: ArrayList<TvShowResponse> = ArrayList()

    var swipeHandler: Handler? = null
    var swipeRunnable: Runnable? = null
    var swipeTimer: Timer? = null
    var currentPage = 0


    override fun onViewReady() {
        setupViewListener()
        setupCarousel()
        setupRecycler()
    }

    override fun start() {
        val shouldLoadGenre: Boolean = prefs[Pref.PREF_SHOULD_LOAD_GENRE] ?: false
        if (shouldLoadGenre) {
            val splashscreenViewModel: SplashscreenViewModel = obtainViewModel()
            splashscreenViewModel.also { viewModel ->

                // load movie genre and tvshow genre
                viewModel.start()

                // observe the change
                viewModel.apply {
                    listMoviesGenre.observe { listGenre ->
                        Pref.listMoviesGenre = listGenre.toJson()
                    }

                    listTvShowsGenre.observe { listGenre ->
                        Pref.listTvShowsGenre = listGenre.toJson()
                        mListTvShowGenre.clear()
                        mListTvShowGenre.addAll(listGenre!!)
                    }

                    requestSuccess.observe {
                        prefs[Pref.PREF_SHOULD_LOAD_GENRE] = false
                        mViewModel.getPopularTvShows(mListTvShowGenre)
                        mViewModel.getOnAirTvShows()
                    }
                }

            }
        } else {
            mViewModel.getPopularTvShows(mListTvShowGenre)
            mViewModel.getOnAirTvShows()
        }
    }

    override fun setupObserver(): TvShowViewModel? {
        return mViewModel.apply {

            startProgress.observe {
                progressBarRecycler.visible()
            }

            requestSuccess.observe {
                progressBarRecycler.gone()
                recyclerView.visible()
            }

            requestFailed.observe {
                progressBarRecycler.gone()
            }

            toast.observe { msg ->
                msg?.let { toast(it) }
            }

            listTvShowsLive.observe {
                it?.let { listMovies ->
                    mListMovies.clear()
                    mListMovies.addAll(listMovies)
                    mAdapter?.notifyDataSetChanged()
                }
            }

            listCarouselLive.observe {
                it?.let { listMovies ->
                    mListCarousel.clear()
                    mListCarousel.addAll(listMovies)

                    setupCarousel()
                }
            }
        }
    }

    private fun setupCarousel() {
        mCarouselAdapter = TvShowCarouselAdapter(
            fragmentManager = childFragmentManager,
            listCarousel = mListCarousel,
            actionListener = this@TvShowFragment
        )

        viewPager.adapter = mCarouselAdapter
        circleIndicator.setViewPager(viewPager)

        // Code to preview previous and next item
        viewPager.setPadding(32.dp, 0, 32.dp, 0)
        viewPager.clipToPadding = false
        viewPager.pageMargin = 20.dp

        // Initiate the auto swipe
        swipeHandler = Handler()
        swipeRunnable = Runnable {
            if (currentPage == mListCarousel.size - 1) {
                currentPage = 0
            }
            try {
                viewPager.setCurrentItem(currentPage++, true)
            } catch (e: Exception) {
            }
        }

        // Run it
        runAutoSwipeViewPager()

        // swipe listener
        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {}
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
            override fun onPageSelected(position: Int) {
                currentPage = position

                try {
                    val movieResponse: TvShowResponse? = mListCarousel[position]
                    Glide.with(this@TvShowFragment)
                        .load(Const.BASE_IMAGE_PATH + movieResponse?.poster_path)
                        .withLoadingPlaceholder(requireContext())
                        .into(imageCover)
                } catch (e: Exception) {
                }
            }
        })
    }

    override fun onResume() {
        super.onResume()
        runAutoSwipeViewPager()
    }

    override fun onPause() {
        super.onPause()
        pauseAutoSwipeViewPager()
    }

    fun runAutoSwipeViewPager() {

        // Do the run if the data is ready
        if (!mListCarousel.isNullOrEmpty()) {
            pauseAutoSwipeViewPager()

            swipeTimer = Timer()
            swipeTimer?.schedule(object : TimerTask() {
                override fun run() {
                    swipeHandler?.post(swipeRunnable)
                }
            }, 1000, 3000L)
        }
    }

    fun pauseAutoSwipeViewPager() {
        swipeTimer?.cancel()
        swipeTimer?.purge()
        swipeTimer = null
    }

    private fun setupRecycler() {
        mAdapter = TvShowAdapter(
            listItems = mListMovies,
            glide = Glide.with(this),
            actionListener = this@TvShowFragment
        )

        recyclerView.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun setupViewListener() {
        imgLanguage.setOnClickListener {
            onChangeLanguageClick()
        }
    }

    /**
     * TvShowActionListener implementations
     * ---------------------------------------------------------------------------------------------
     * */

    override fun onTvShowClick(tvShowId: Int, tvShowResponse: TvShowResponse) {
        DetailActivity.startActivity(
            context = requireContext(),
            tvShowId = tvShowId,
            tvShowResponse = tvShowResponse
        )
    }

    override fun onChangeLanguageClick() {
        startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
    }

    companion object {
        fun newInstance() = TvShowFragment().withArgs { }
    }
}