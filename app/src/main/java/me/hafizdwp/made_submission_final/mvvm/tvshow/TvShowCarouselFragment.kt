package me.hafizdwp.made_submission_final.mvvm.tvshow

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import kotlinx.android.synthetic.main.movie_carousel_fragment.*
import me.hafizdwp.made_submission_final.R
import me.hafizdwp.made_submission_final.data.Const
import me.hafizdwp.made_submission_final.data.source.remote.model.TvShowResponse
import me.hafizdwp.made_submission_final.util.ext.withArgs
import me.hafizdwp.made_submission_final.util.ext.withLoadingPlaceholder

/**
 * @author hafizdwp
 * 01/08/2019
 **/
class TvShowCarouselFragment : Fragment() {

    var mTvShowResponse: TvShowResponse? = null
    var mActionListener: TvShowActionListener? = null
    var isEmpty: Boolean = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        // Extract arguments
        mTvShowResponse = arguments?.getParcelable(ARGUMENTS_MOVIE)
        isEmpty = arguments?.getBoolean(ARGUMENTS_ISEMPTY) ?: false
        mActionListener = arguments?.getParcelable(ARGUMENTS_TVSHOW_LISTENER)

        return when (isEmpty) {
            true ->
                inflater.inflate(R.layout.tvshow_carousel_empty_fragment, container, false)
            else ->
                inflater.inflate(R.layout.tvshow_carousel_fragment, container, false)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (!isEmpty) {

            rootCard.setOnClickListener {
                mTvShowResponse?.id?.let { id ->
                    mActionListener?.onTvShowClick(id, mTvShowResponse!!)
                }
            }

            Glide.with(this)
                    .load(Const.BASE_IMAGE_PATH + mTvShowResponse?.backdrop_path)
                    .withLoadingPlaceholder(requireContext())
                    .listener(object : RequestListener<Drawable> {
                        override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                            return false
                        }

                        override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {

                            // use the alpha background
                            // to look more stylish
                            imageAlpha.setBackgroundResource(R.drawable.carousel_gradient_alpha)
                            return false
                        }
                    })
                    .into(imageCarousel)

            titleCarousel.text = mTvShowResponse?.name
        }
    }

    companion object {
        private const val ARGUMENTS_MOVIE = "args"
        private const val ARGUMENTS_ISEMPTY = "args_isempty"
        private const val ARGUMENTS_TVSHOW_LISTENER = "args_listener"

        fun newInstance(tvShowResponse: TvShowResponse? = null,
                        isEmpty: Boolean = false,
                        actionListener: TvShowActionListener? = null) = TvShowCarouselFragment().withArgs {
            putParcelable(ARGUMENTS_MOVIE, tvShowResponse)
            putBoolean(ARGUMENTS_ISEMPTY, isEmpty)
            putParcelable(ARGUMENTS_TVSHOW_LISTENER, actionListener)
        }
    }
}