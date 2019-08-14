package me.hafizdwp.made_submission_final.mvvm.detail

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import me.hafizdwp.made_submission_final.R
import me.hafizdwp.made_submission_final.base.BaseActivity
import me.hafizdwp.made_submission_final.data.source.remote.model.MovieResponse
import me.hafizdwp.made_submission_final.data.source.remote.model.TvShowResponse
import me.hafizdwp.made_submission_final.util.ext.bundleTo
import me.hafizdwp.made_submission_final.util.ext.startActivity

/**
 * @author hafizdwp
 * 11/07/19
 **/
class DetailActivity : BaseActivity() {

    override val bindLayoutRes: Int
        get() = R.layout.detail_activity
    override val bindFragmentContainerId: Int
        get() = R.id.frameDetailContent
    override val bindFragmentInstance: Fragment
        get() = DetailFragment.newInstance()

    /// To decide which data will be called and inflated in fragments
    var DETAIL_ENUM = DetailEnum.NULL

    var movieId: Int = -1
    var tvShowId: Int = -1
    lateinit var mMovieResponse: MovieResponse
    lateinit var mTvShowResponse: TvShowResponse

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        movieId = intent.getIntExtra(EXTRA_MOVIE_ID, -1)
        if (movieId != -1) {
            mMovieResponse = intent.getParcelableExtra(EXTRA_MOVIE_RESPONSE)
            DETAIL_ENUM = DetailEnum.MOVIE
        }

        tvShowId = intent.getIntExtra(EXTRA_TVSHOW_ID, -1)
        if (tvShowId != -1) {
            mTvShowResponse = intent.getParcelableExtra(EXTRA_TVSHOW_RESPONSE)
            DETAIL_ENUM = DetailEnum.TVSHOW
        }

    }

    companion object {
        private const val EXTRA_MOVIE_ID = "extra_movie_id"
        private const val EXTRA_TVSHOW_ID = "extra_tvshow_id"
        private const val EXTRA_MOVIE_RESPONSE = "extra_movie_response"
        private const val EXTRA_TVSHOW_RESPONSE = "extra_tvshow_response"

        fun startActivity(context: Context,
                          movieId: Int? = null,
                          movieResponse: MovieResponse? = null,
                          tvShowId: Int? = null,
                          tvShowResponse: TvShowResponse? = null) {

            when {
                movieId != null ->
                    context.startActivity<DetailActivity>(
                            EXTRA_MOVIE_ID bundleTo movieId,
                            EXTRA_MOVIE_RESPONSE bundleTo movieResponse!!)

                tvShowId != null ->
                    context.startActivity<DetailActivity>(
                            EXTRA_TVSHOW_ID bundleTo tvShowId,
                            EXTRA_TVSHOW_RESPONSE bundleTo tvShowResponse!!)
            }
        }
    }
}