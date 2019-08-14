package me.hafizdwp.made_submission_final.mvvm.detail

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.MenuItem
import android.view.View
import androidx.core.widget.ImageViewCompat
import androidx.lifecycle.LifecycleOwner
import com.bumptech.glide.Glide
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.detail_fragment.*
import me.hafizdwp.made_submission_final.R
import me.hafizdwp.made_submission_final.base.BaseFragment
import me.hafizdwp.made_submission_final.data.Constant
import me.hafizdwp.made_submission_final.data.model.MovieDetailResponse
import me.hafizdwp.made_submission_final.data.model.TvShowDetailResponse
import me.hafizdwp.made_submission_final.util.ext.gone
import me.hafizdwp.made_submission_final.util.ext.obtainViewModel
import me.hafizdwp.made_submission_final.util.ext.stripIfNullOrEmpty
import me.hafizdwp.made_submission_final.util.ext.toast
import me.hafizdwp.made_submission_final.util.ext.withArgs
import me.hafizdwp.made_submission_final.util.ext.withLoadingPlaceholder
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*

/**
 * @author hafizdwp
 * 11/07/19
 **/
class DetailFragment : BaseFragment<DetailActivity, DetailViewModel>() {

    override val bindLayoutRes: Int
        get() = R.layout.detail_fragment
    override val mViewModel: DetailViewModel
        get() = obtainViewModel()
    override val mLifecycleOwner: LifecycleOwner
        get() = this@DetailFragment


    override fun onViewReady() {
        // setup toolbar
        mActivity.apply {
            setSupportActionBar(toolbar)
            supportActionBar?.title = ""
            toolbar.navigationIcon = resources.getDrawable(R.drawable.ic_arrow_back)
            toolbar.setNavigationOnClickListener {
                onBackPressed()
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        mActivity.onBackPressed()
        return true
    }

    override fun start() {

        // hide the content first
        hideOrShowContent(true)

        /// Inflate movie or tv shows
        when (mActivity.DETAIL_ENUM) {
            DetailEnum.MOVIE -> {
                mViewModel.getMovieDetail(mActivity.movieId)
                mViewModel.getMovieFromFavorite(mActivity.movieId)
            }

            DetailEnum.TVSHOW -> {
                mViewModel.getTvShowDetail(mActivity.tvShowId)
                mViewModel.getTvShowFromFavorite(mActivity.tvShowId)
            }

            else -> {
            }
        }
    }

    override fun setupObserver(): DetailViewModel? {
        return mViewModel.apply {

            startProgress.observe {
                myProgressView.startProgress()
            }

            requestSuccess.observe {
                myProgressView.stopAndGone()
                hideOrShowContent(false)
            }

            requestFailed.observe { errorMsg ->
                errorMsg?.let {
                    myProgressView.stopAndError(it, true)
                    myProgressView.setRetryClickListener {
                        start()
                    }
                }
            }

            toast.observe {
                it?.let { stringRes ->
                    toast(getString(stringRes))
                }
            }

            detailMovieLive.observe {
                it?.let {
                    showMovieData(it)
                }
            }

            detailTvShowLive.observe {
                it?.let {
                    showTvShowData(it)
                }
            }

            favoriteTableLive.observe {

                // it: true = favorited
                // it: false = unfavorited
                fabFavorite.setFavorited(it != null)
            }
        }
    }

    private fun showMovieData(model: MovieDetailResponse) {
        model.apply {

            Glide.with(this@DetailFragment)
                    .load(Constant.BASE_IMAGE_PATH + backdrop_path)
                    .withLoadingPlaceholder(requireContext())
                    .into(imageBackdrop)

            Glide.with(this@DetailFragment)
                    .load(Constant.BASE_IMAGE_PATH + poster_path)
                    .withLoadingPlaceholder(requireContext())
                    .into(imagePhoto)

            textTitle.text = title
            textGaya1.text = getString(R.string.popularity, popularity.toString())
            textGaya2.text = "$runtime min"

            when (genres?.size) {
                0 -> {
                    textGenre1.gone()
                    textGenre2.gone()
                    textGenre3.gone()
                }

                1 -> {
                    textGenre1.text = genres?.get(0)?.name ?: ""
                    textGenre2.gone()
                    textGenre3.gone()
                }

                2 -> {
                    textGenre1.text = genres?.get(0)?.name ?: ""
                    textGenre2.text = genres?.get(1)?.name ?: ""
                    textGenre3.gone()
                }

                else -> {
                    textGenre1.text = genres?.get(0)?.name ?: ""
                    textGenre2.text = genres?.get(1)?.name ?: ""
                    textGenre3.text = genres?.get(2)?.name ?: ""
                }
            }

            textRating.text = "${vote_average?.toFloat()} / 10"
            ratingBar.rating = (vote_average!!.toFloat()) / 2
            textVoteCount.text = "$vote_count"

            textRelease.text = release_date

            val formatter = NumberFormat.getInstance(Locale.US) as DecimalFormat
            formatter.applyPattern("#,###,###,###")
            textRevenue.text = try {
                "${formatter.format(revenue)} US$"
            } catch (e: Exception) {
                e.printStackTrace()
                "-"
            }


            textTagline.text = tagline?.stripIfNullOrEmpty()
            textOverview.text = overview


            // Fab action listener
            fabFavorite.apply {
                setOnClickListener {
                    val isFavorited = mViewModel.favoriteTableLive.value != null
                    when (isFavorited) {
                        true ->
                            mViewModel.deleteMovieFromFavorite(mViewModel.favoriteTableLive.value!!)
                        false ->
                            mViewModel.saveMovieToFavorite(mActivity.mMovieResponse)
                    }
                }
            }
        }
    }

    private fun showTvShowData(model: TvShowDetailResponse) {
        model.apply {

            Glide.with(this@DetailFragment)
                    .load(Constant.BASE_IMAGE_PATH + backdrop_path)
                    .withLoadingPlaceholder(requireContext())
                    .into(imageBackdrop)

            Glide.with(this@DetailFragment)
                    .load(Constant.BASE_IMAGE_PATH + poster_path)
                    .withLoadingPlaceholder(requireContext())
                    .into(imagePhoto)

            textTitle.text = name
            textGaya1.text = getString(R.string.popularity, popularity.toString())
            textGaya2.text = "$episode_run_time min"

            when (genres?.size) {
                0 -> {
                    textGenre1.gone()
                    textGenre2.gone()
                    textGenre3.gone()
                }

                1 -> {
                    textGenre1.text = genres?.get(0)?.name ?: ""
                    textGenre2.gone()
                    textGenre3.gone()
                }

                2 -> {
                    textGenre1.text = genres?.get(0)?.name ?: ""
                    textGenre2.text = genres?.get(1)?.name ?: ""
                    textGenre3.gone()
                }

                else -> {
                    textGenre1.text = genres?.get(0)?.name ?: ""
                    textGenre2.text = genres?.get(1)?.name ?: ""
                    textGenre3.text = genres?.get(2)?.name ?: ""
                }
            }

            textRating.text = "${vote_average?.toFloat()} / 10"
            ratingBar.rating = (vote_average!!.toFloat()) / 2
            textVoteCount.text = "$vote_count"

            textReleaseLabel.text = getString(R.string.first_air_date)
            textRelease.text = first_air_date

            textRevenueLabel.text = getString(R.string.number_of_seasons)
            textRevenue.text = "$number_of_seasons"

            textTaglineLabel.text = getString(R.string.status)
            textTagline.text = status

            textOverview.text = overview


            // Fab action listener
            fabFavorite.apply {
                setOnClickListener {
                    val isFavorited = mViewModel.favoriteTableLive.value != null
                    when (isFavorited) {
                        true ->
                            mViewModel.deleteTvShowFromFavorite(mViewModel.favoriteTableLive.value!!)
                        false ->
                            mViewModel.saveTvShowToFavorite(mActivity.mTvShowResponse)
                    }
                }
            }
        }
    }

    private fun FloatingActionButton.setFavorited(isFavorited: Boolean) {
        if (isFavorited)
            ImageViewCompat.setImageTintList(this, ColorStateList.valueOf(resources.getColor(R.color.color_favorited)))
        else
            ImageViewCompat.setImageTintList(this, ColorStateList.valueOf(Color.WHITE))
    }

    /**
     * @param which true = hide
     *              false = show
     * */
    fun hideOrShowContent(which: Boolean) {
        conslaTopContent.hideOrShow = which
        textRating.hideOrShow = which
        ratingBar.hideOrShow = which
        textVoteCount.hideOrShow = which
        textVoteCountLabel.hideOrShow = which
        cardPhoto.hideOrShow = which
        textReleaseLabel.hideOrShow = which
        textRelease.hideOrShow = which
        textRevenueLabel.hideOrShow = which
        textRevenue.hideOrShow = which
        textTaglineLabel.hideOrShow = which
        textTagline.hideOrShow = which
        textOverviewLabel.hideOrShow = which
        textOverview.hideOrShow = which
        fabFavorite.hideOrShow = which
    }

    var View.hideOrShow: Boolean
        get() = isGone()
        set(value) = if (value) gone() else visible()

    fun View.gone() {
        visibility = View.GONE
    }

    fun View.isGone(): Boolean {
        return visibility == View.GONE
    }

    fun View.visible() {
        visibility = View.VISIBLE
    }

    companion object {
        fun newInstance() = DetailFragment().withArgs { }
    }
}