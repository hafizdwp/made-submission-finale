package me.hafizdwp.made_submission_final.mvvm.search

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

/**
 * @author hafizdwp
 * 15/08/2019
 **/
class SearchPagerAdapter(manager: FragmentManager) : FragmentPagerAdapter(manager) {

    private var fragmentList: ArrayList<Fragment> = ArrayList()
    private var fragmentTitleList: ArrayList<String> = ArrayList()

    override fun getCount(): Int {
        return fragmentList.size
    }

    override fun getItem(position: Int): Fragment {
        return fragmentList[position]
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return fragmentTitleList[position]
    }

    fun addFragment(fragment: Fragment, title: String) {
        fragmentList.add(fragment)
        fragmentTitleList.add(title)
    }
}