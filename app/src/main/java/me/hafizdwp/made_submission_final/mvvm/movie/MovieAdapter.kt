package me.hafizdwp.made_submission_final.mvvm.movie

import android.view.View
import com.bumptech.glide.RequestManager
import kotlinx.android.synthetic.main.movie_item.view.*
import me.hafizdwp.made_submission_final.R
import me.hafizdwp.made_submission_final.base.BaseRecyclerAdapter
import me.hafizdwp.made_submission_final.data.Const
import me.hafizdwp.made_submission_final.data.source.remote.model.MovieResponse
import me.hafizdwp.made_submission_final.util.ext.visible
import me.hafizdwp.made_submission_final.util.ext.withLoadingPlaceholder

/**
 * @author hafizdwp
 * 10/07/19
 **/
class MovieAdapter(
        private val listItems: ArrayList<MovieResponse>,
        private val glide: RequestManager,
        private val actionListener: MovieActionListener
) : BaseRecyclerAdapter<MovieResponse>() {

    override val bindItemLayoutRes: Int
        get() = R.layout.movie_item
    override val mListItem: List<MovieResponse>
        get() = listItems

    override fun onGetItemCount(): Int {
        return if (mListItem.size > 9)
            9
        else
            mListItem.size
    }

    override fun onBind(itemView: View, model: MovieResponse) {
        itemView.apply {

            rootView.setOnClickListener {
                actionListener.onMovieClick(model.id!!, model)
            }

            model.apply {
                textTitle.text = title
                textYears.text = release_date?.substring(0, 4)
                textRating.text = vote_average.toString()

                glide.load(Const.BASE_IMAGE_PATH + poster_path)
                        .withLoadingPlaceholder(itemView.context)
                        .into(imagePhoto)

                if (vote_average == null) {
                    ratingBar.rating = 0F
                } else {
                    ratingBar.rating = (vote_average!!.toFloat()) / 2
                }

                listGenre?.let {
                    when (it.size) {
                        0 -> {

                        }

                        1 -> {
                            textGenre1.visible()
                            textGenre1.text = it[0]
                        }

                        else -> {
                            textGenre1.visible()
                            textGenre2.visible()
                            textGenre1.text = it[0]
                            textGenre2.text = it[1]
                        }
                    }
                }
            }
        }
    }
}