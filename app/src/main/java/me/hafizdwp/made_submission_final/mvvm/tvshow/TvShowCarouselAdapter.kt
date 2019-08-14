package me.hafizdwp.made_submission_final.mvvm.tvshow

import android.os.Parcelable
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import me.hafizdwp.made_submission_final.data.model.TvShowResponse

/**
 * @author hafizdwp
 * 01/08/2019
 **/
class TvShowCarouselAdapter(fragmentManager: FragmentManager,
                            val listCarousel: List<TvShowResponse>,
                            val actionListener: TvShowActionListener) : FragmentStatePagerAdapter(fragmentManager) {

    override fun getItem(position: Int): Fragment {

        // return empty fragment
        // if the data isn't loaded yet
        return if (listCarousel.isNotEmpty()) {
            TvShowCarouselFragment.newInstance(
                    tvShowResponse = listCarousel[position],
                    actionListener = actionListener)
        } else {
            TvShowCarouselFragment.newInstance(
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