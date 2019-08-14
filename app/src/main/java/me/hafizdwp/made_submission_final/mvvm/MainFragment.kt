package me.hafizdwp.made_submission_final.mvvm

import android.os.Bundle
import android.view.View
import androidx.lifecycle.LifecycleOwner
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.main_fragment.*
import me.hafizdwp.made_submission_final.NoViewModel
import me.hafizdwp.made_submission_final.R
import me.hafizdwp.made_submission_final.base.BaseFragment
import me.hafizdwp.made_submission_final.mvvm.favorite.FavoriteFragment
import me.hafizdwp.made_submission_final.mvvm.movie.MovieFragment
import me.hafizdwp.made_submission_final.mvvm.tvshow.TvShowFragment
import me.hafizdwp.made_submission_final.util.ext.disableShiftingMode
import me.hafizdwp.made_submission_final.util.ext.obtainViewModel
import me.hafizdwp.made_submission_final.util.ext.withArgs

/**
 * @author hafizdwp
 * 10/07/19
 **/
class MainFragment : BaseFragment<MainActivity, NoViewModel>() {

    override val bindLayoutRes: Int
        get() = R.layout.main_fragment
    override val mViewModel: NoViewModel
        get() = obtainViewModel()
    override val mLifecycleOwner: LifecycleOwner
        get() = this@MainFragment

    private val mBottomNavItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { menuItem ->

        var fragment: androidx.fragment.app.Fragment? = null

        when (menuItem.itemId) {
            R.id.bnav_movie ->
                fragment = MovieFragment.newInstance()
            R.id.bnav_tvshow ->
                fragment = TvShowFragment.newInstance()
            R.id.bnav_favorite ->
                fragment = FavoriteFragment.newInstance()
        }

        if (fragment != null) {

            childFragmentManager
                    .beginTransaction()
                    .replace(frameMainBottomnav.id, fragment)
                    .commit()

            return@OnNavigationItemSelectedListener true
        }

        return@OnNavigationItemSelectedListener false
    }

    override fun onViewReady() {
        // setup bottomNavigationView
        bottomNav.apply {
            setOnNavigationItemSelectedListener(mBottomNavItemSelectedListener)
            selectedItemId = R.id.bnav_movie
            disableShiftingMode()
        }
    }

    override fun start() {

    }

    override fun setupObserver(): NoViewModel? {
        return null
    }

    companion object {
        fun newInstance() = MainFragment().withArgs { }
    }
}