package me.hafizdwp.made_submission_final.mvvm.setting

import androidx.lifecycle.LifecycleOwner
import me.hafizdwp.made_submission_final.R
import me.hafizdwp.made_submission_final.base.BaseFragment
import me.hafizdwp.made_submission_final.mvvm.MainActivity
import me.hafizdwp.made_submission_final.util.ext.obtainViewModel
import me.hafizdwp.made_submission_final.util.ext.withArgs

/**
 * @author hafizdwp
 * 15/08/2019
 **/
class SettingFragment : BaseFragment<MainActivity, SettingViewModel>() {

    override val bindLayoutRes: Int
        get() = R.layout.setting_fragment
    override val mViewModel: SettingViewModel
        get() = obtainViewModel()
    override val mLifecycleOwner: LifecycleOwner
        get() = this@SettingFragment

    override fun onViewReady() {
    }

    override fun start() {
    }

    override fun setupObserver(): SettingViewModel? {
        return mViewModel.apply {

        }
    }

    companion object {
        fun newInstance() = SettingFragment().withArgs { }
    }
}