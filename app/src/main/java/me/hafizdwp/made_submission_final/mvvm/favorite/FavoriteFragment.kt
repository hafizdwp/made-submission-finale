package me.hafizdwp.made_submission_final.mvvm.favorite

import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.parcel.Parcelize
import kotlinx.android.synthetic.main.favorite_fragment.*
import me.hafizdwp.made_submission_final.R
import me.hafizdwp.made_submission_final.base.BaseFragment
import me.hafizdwp.made_submission_final.data.model.MovieResponse
import me.hafizdwp.made_submission_final.data.model.TvShowResponse
import me.hafizdwp.made_submission_final.data.source.local.entity.FavoriteTable
import me.hafizdwp.made_submission_final.mvvm.MainActivity
import me.hafizdwp.made_submission_final.mvvm.detail.DetailActivity
import me.hafizdwp.made_submission_final.mvvm.movie.MovieActionListener
import me.hafizdwp.made_submission_final.mvvm.tvshow.TvShowActionListener
import me.hafizdwp.made_submission_final.util.ext.obtainViewModel
import me.hafizdwp.made_submission_final.util.ext.withArgs

/**
 * @author hafizdwp
 * 10/07/19
 **/
@Parcelize
class FavoriteFragment : BaseFragment<MainActivity, FavoriteViewModel>(),
        MovieActionListener, TvShowActionListener {

    override val bindLayoutRes: Int
        get() = R.layout.favorite_fragment
    override val mViewModel: FavoriteViewModel
        get() = obtainViewModel()
    override val mLifecycleOwner: LifecycleOwner
        get() = this@FavoriteFragment

    lateinit var mMovieAdapter: FavoriteAdapter
    lateinit var mTvShowAdapter: FavoriteAdapter
    val mListMovieFavorite = ArrayList<FavoriteTable>()
    val mListTvShowFavorite = ArrayList<FavoriteTable>()


    override fun onViewReady() {
        setupRecycler()
    }

    override fun start() {

    }

    override fun onResume() {
        super.onResume()

        mViewModel.getFavoritedMovies()
        mViewModel.getFavoritedTvShows()
    }

    override fun setupObserver(): FavoriteViewModel? {
        return mViewModel.apply {
            startProgress.observe {
                myProgressView.startProgress()
            }

            requestSuccess.observe {
                myProgressView.stopAndGone()
            }

            requestEmpty.observe { stringRes ->
                myProgressView.stopAndError(getString(stringRes!!), false)
            }

            listMovieFavoritedLive.observe { listFavorite ->
                listFavorite?.let {
                    mListMovieFavorite.clear()
                    mListMovieFavorite.addAll(listFavorite)
                    mMovieAdapter.notifyDataSetChanged()
                }
            }

            listTvShowFavoritedLive.observe { listTvShow ->
                listTvShow?.let {
                    mListTvShowFavorite.clear()
                    mListTvShowFavorite.addAll(listTvShow)
                    mTvShowAdapter.notifyDataSetChanged()
                }
            }
        }
    }

    /**
     * MovieActionListener & TvShowActionListener implementations
     * ---------------------------------------------------------------------------------------------
     * */

    override fun onMovieClick(movieId: Int, movieResponse: MovieResponse) {
        DetailActivity.startActivity(
                context = requireContext(),
                movieId = movieId,
                movieResponse = movieResponse
        )
    }

    override fun onTvShowClick(tvShowId: Int, tvShowResponse: TvShowResponse) {
        DetailActivity.startActivity(
                context = requireContext(),
                tvShowId = tvShowId,
                tvShowResponse = tvShowResponse
        )
    }

    override fun onChangeLanguageClick() {

    }

    fun setupRecycler() {

        // Movie recyclerView
        mMovieAdapter = FavoriteAdapter(
                listFavorite = mListMovieFavorite,
                movieActionListener = this@FavoriteFragment,
                tvShowActionListener = this@FavoriteFragment)

        recyclerMovies.apply {
            adapter = mMovieAdapter
            layoutManager = GridLayoutManager(requireContext(), 3)
        }

        // TvShow recyclerView
        mTvShowAdapter = FavoriteAdapter(
                listFavorite = mListTvShowFavorite,
                movieActionListener = this@FavoriteFragment,
                tvShowActionListener = this@FavoriteFragment)


        recyclerTvShows.apply {
            adapter = mTvShowAdapter
            layoutManager = GridLayoutManager(requireContext(), 3)
        }
    }

    companion object {
        fun newInstance() = FavoriteFragment().withArgs { }
    }
}