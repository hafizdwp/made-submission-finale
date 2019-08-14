package me.hafizdwp.made_submission_final.mvvm.movie

import android.content.Intent
import android.os.Handler
import android.provider.Settings
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import kotlinx.android.parcel.Parcelize
import kotlinx.android.synthetic.main.movie_fragment.*
import me.hafizdwp.made_submission_final.R
import me.hafizdwp.made_submission_final.SplashscreenViewModel
import me.hafizdwp.made_submission_final.base.BaseFragment
import me.hafizdwp.made_submission_final.data.Constant
import me.hafizdwp.made_submission_final.data.Pref
import me.hafizdwp.made_submission_final.data.source.remote.model.GenreResponse
import me.hafizdwp.made_submission_final.data.source.remote.model.MovieResponse
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
class MovieFragment : BaseFragment<MainActivity, MovieViewModel>(), MovieActionListener {

    override val bindLayoutRes: Int
        get() = R.layout.movie_fragment
    override val mViewModel: MovieViewModel
        get() = obtainViewModel()
    override val mLifecycleOwner: LifecycleOwner
        get() = this@MovieFragment

    private var mAdapter: MovieAdapter? = null
    private var mCarouselAdapter: MovieCarouselAdapter? = null
    private var mListMovies: ArrayList<MovieResponse> = ArrayList()
    private var mListMoviesGenre: ArrayList<GenreResponse> = Pref.listMoviesGenre?.fromJson()
        ?: ArrayList()
    private var mListCarousel: ArrayList<MovieResponse> = ArrayList()

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
        val shouldLoadGenre: Boolean = prefs[Constant.PREF_SHOULD_LOAD_GENRE] ?: false
        if (shouldLoadGenre) {
            val splashscreenViewModel: SplashscreenViewModel = obtainViewModel()
            splashscreenViewModel.also { viewModel ->

                // load movie genre and tvshow genre
                viewModel.start()

                // observe the change
                viewModel.apply {
                    listMoviesGenre.observe { listGenre ->
                        Pref.listMoviesGenre = listGenre.toJson()
                        mListMoviesGenre.clear()
                        mListMoviesGenre.addAll(listGenre!!)
                        log(mListMoviesGenre.toJson())
                    }

                    listTvShowsGenre.observe { listGenre ->
                        Pref.listTvShowsGenre = listGenre.toJson()
                    }

                    requestSuccess.observe {
                        prefs[Constant.PREF_SHOULD_LOAD_GENRE] = false
                        mViewModel.getPopularMovies(mListMoviesGenre)
                        mViewModel.getNowPlayingMovies()
                    }
                }

            }
        } else {
            mViewModel.getPopularMovies(mListMoviesGenre)
            mViewModel.getNowPlayingMovies()
        }
    }

    override fun setupObserver(): MovieViewModel? {
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

            listMoviesLive.observe {
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

    private fun setupCarousel() {
        mCarouselAdapter = MovieCarouselAdapter(
            fragmentManager = childFragmentManager,
            listCarousel = mListCarousel,
            actionListener = this@MovieFragment
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
                    val movieResponse: MovieResponse? = mListCarousel[position]
                    Glide.with(this@MovieFragment)
                        .load(Constant.BASE_IMAGE_PATH + movieResponse?.poster_path)
                        .withLoadingPlaceholder(requireContext())
                        .into(imageCover)
                } catch (e: Exception) {
                }
            }
        })
    }

    private fun setupRecycler() {
        mAdapter = MovieAdapter(
            listItems = mListMovies,
            glide = Glide.with(this),
            actionListener = this@MovieFragment
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
     * MovieActionListener implementation
     * ---------------------------------------------------------------------------------------------
     * */
    override fun onMovieClick(movieId: Int, movieResponse: MovieResponse) {
        DetailActivity.startActivity(
            context = requireContext(),
            movieId = movieId,
            movieResponse = movieResponse
        )
    }

    override fun onChangeLanguageClick() {
        startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
    }

    companion object {
        fun newInstance() = MovieFragment().withArgs { }
    }
}