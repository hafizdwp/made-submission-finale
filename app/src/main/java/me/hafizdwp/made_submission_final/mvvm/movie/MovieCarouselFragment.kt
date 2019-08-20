package me.hafizdwp.made_submission_final.mvvm.movie

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
import me.hafizdwp.made_submission_final.data.source.remote.model.MovieResponse
import me.hafizdwp.made_submission_final.util.ext.withArgs
import me.hafizdwp.made_submission_final.util.ext.withLoadingPlaceholder

/**
 * @author hafizdwp
 * 10/07/19
 **/
class MovieCarouselFragment : Fragment() {

    var mMovieResponse: MovieResponse? = null
    var mActionListener: MovieActionListener? = null
    var isEmpty: Boolean = false


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        // Extract arguments
        mMovieResponse = arguments?.getParcelable(ARGUMENTS_MOVIE)
        isEmpty = arguments?.getBoolean(ARGUMENTS_ISEMPTY) ?: false
        mActionListener = arguments?.getParcelable(ARGUMENTS_MOVIE_LISTENER)

        return when (isEmpty) {
            true ->
                inflater.inflate(R.layout.movie_carousel_empty_fragment, container, false)
            else ->
                inflater.inflate(R.layout.movie_carousel_fragment, container, false)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (!isEmpty) {

            rootCard.setOnClickListener {
                mMovieResponse?.id?.let { id ->
                    mActionListener?.onMovieClick(id, mMovieResponse!!)
                }
            }

            Glide.with(this)
                    .load(Const.BASE_IMAGE_PATH + mMovieResponse?.backdrop_path)
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

            titleCarousel.text = mMovieResponse?.title
        }
    }

    companion object {
        private const val ARGUMENTS_MOVIE = "args"
        private const val ARGUMENTS_ISEMPTY = "args_isempty"
        private const val ARGUMENTS_MOVIE_LISTENER = "args_listener"

        fun newInstance(movieResponse: MovieResponse? = null,
                        isEmpty: Boolean = false,
                        actionListener: MovieActionListener? = null) = MovieCarouselFragment().withArgs {
            putParcelable(ARGUMENTS_MOVIE, movieResponse)
            putBoolean(ARGUMENTS_ISEMPTY, isEmpty)
            putParcelable(ARGUMENTS_MOVIE_LISTENER, actionListener)
        }
    }
}