package me.hafizdwp.made_submission_final.mvvm.movie

import android.os.Parcelable
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import me.hafizdwp.made_submission_final.data.source.remote.model.MovieResponse

/**
 * @author hafizdwp
 * 10/07/19
 **/
class MovieCarouselAdapter(fragmentManager: FragmentManager,
                           val listCarousel: List<MovieResponse>,
                           val actionListener: MovieActionListener) : FragmentStatePagerAdapter(fragmentManager) {

    override fun getItem(position: Int): Fragment {

        // return empty fragment
        // if the data isn't loaded yet
        return if (listCarousel.isNotEmpty()) {
            MovieCarouselFragment.newInstance(
                    movieResponse = listCarousel[position],
                    actionListener = actionListener)
        } else {
            MovieCarouselFragment.newInstance(
                    isEmpty = true
            )
        }
    }

    override fun getCount(): Int {
        return if (listCarousel.isNotEmpty()) {
            if (listCarousel.size > 9)
                9
            else
                listCarousel.size
        } else {
            1 // Empty Fragment
        }
    }

    override fun restoreState(state: Parcelable?, loader: ClassLoader?) {
        try {
            super.restoreState(state, loader)
        } catch (e: Exception) {
        }
    }
}