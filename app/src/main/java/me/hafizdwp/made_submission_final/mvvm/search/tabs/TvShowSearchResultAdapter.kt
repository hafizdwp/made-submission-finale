package me.hafizdwp.made_submission_final.mvvm.search.tabs

import android.view.View
import com.bumptech.glide.RequestManager
import kotlinx.android.synthetic.main.tvshow_item.view.*
import me.hafizdwp.made_submission_final.R
import me.hafizdwp.made_submission_final.base.BaseRecyclerAdapter
import me.hafizdwp.made_submission_final.data.Constant
import me.hafizdwp.made_submission_final.data.source.remote.model.TvShowResponse
import me.hafizdwp.made_submission_final.mvvm.tvshow.TvShowActionListener
import me.hafizdwp.made_submission_final.util.ext.visible
import me.hafizdwp.made_submission_final.util.ext.withLoadingPlaceholder

/**
 * @author hafizdwp
 * 15/08/2019
 **/
class TvShowSearchResultAdapter(
        private val listItems: ArrayList<TvShowResponse>,
        private val glide: RequestManager,
        private val actionListener: TvShowActionListener
) : BaseRecyclerAdapter<TvShowResponse>() {

    override val bindItemLayoutRes: Int
        get() = R.layout.tvshow_item
    override val mListItem: List<TvShowResponse>
        get() = listItems

    override fun onBind(itemView: View, model: TvShowResponse) {
        itemView.apply {
            rootView.setOnClickListener {
                actionListener.onTvShowClick(model.id!!, model)
            }

            model.apply {
                textTitle.text = name
                textRating.text = vote_average.toString()
                textYears.text = try {
                    first_air_date?.substring(0, 4)
                } catch (e: Exception) {
                    "-"
                }
                textRating.text = vote_average.toString()

                glide.load(Constant.BASE_IMAGE_PATH + poster_path)
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