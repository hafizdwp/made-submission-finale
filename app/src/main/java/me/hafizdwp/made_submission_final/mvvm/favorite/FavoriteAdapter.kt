package me.hafizdwp.made_submission_final.mvvm.favorite

import android.view.View
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.favorite_item.view.*
import me.hafizdwp.made_submission_final.R
import me.hafizdwp.made_submission_final.base.BaseRecyclerAdapter
import me.hafizdwp.made_submission_final.data.Const
import me.hafizdwp.made_submission_final.data.source.remote.model.MovieResponse
import me.hafizdwp.made_submission_final.data.source.remote.model.TvShowResponse
import me.hafizdwp.made_submission_final.data.source.local.entity.FavoriteTable
import me.hafizdwp.made_submission_final.mvvm.movie.MovieActionListener
import me.hafizdwp.made_submission_final.mvvm.tvshow.TvShowActionListener

/**
 * @author hafizdwp
 * 24/07/2019
 **/
class FavoriteAdapter(
        val listFavorite: List<FavoriteTable>,
        val movieActionListener: MovieActionListener,
        val tvShowActionListener: TvShowActionListener
) : BaseRecyclerAdapter<FavoriteTable>() {

    override val bindItemLayoutRes: Int
        get() = R.layout.favorite_item
    override val mListItem: List<FavoriteTable>
        get() = listFavorite

    override fun onBind(itemView: View, model: FavoriteTable) {
        itemView.apply {

            Glide.with(itemView.context)
                    .load(Const.BASE_IMAGE_PATH + model.poster_path)
                    .into(image)
            textTitle.text = model.title

            rootView.setOnClickListener {
                if (model.movie_id != 0)
                    movieActionListener.onMovieClick(
                            movieId = model.movie_id!!,
                            movieResponse = MovieResponse.from(model))
                else if (model.tvshow_id != 0)
                    tvShowActionListener.onTvShowClick(
                            tvShowId = model.tvshow_id!!,
                            tvShowResponse = TvShowResponse.from(model))
            }
        }
    }
}