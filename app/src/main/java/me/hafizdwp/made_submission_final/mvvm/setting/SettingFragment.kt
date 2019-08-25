package me.hafizdwp.made_submission_final.mvvm.setting

import androidx.lifecycle.LifecycleOwner
import kotlinx.android.synthetic.main.setting_fragment.*
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

        setupSwitchRelease()
        setupSwitchDaily()
    }

    private fun setupSwitchDaily() {
        swDailyReminder.setOnCheckedChangeListener { compoundButton, isChecked ->
            mViewModel.saveAlarmStatus(AlarmType.DAILY, isChecked)
        }
        swReleaseReminder.setOnCheckedChangeListener { compoundButton, isChecked ->
            mViewModel.saveAlarmStatus(AlarmType.RELEASE, isChecked)
        }
    }

    private fun setupSwitchRelease() {

    }

    override fun start() {
        mViewModel.getAlarmStatus(AlarmType.DAILY)
        mViewModel.getAlarmStatus(AlarmType.RELEASE)
    }

    override fun setupObserver(): SettingViewModel? {
        return mViewModel.apply {
            dailyAlarmStatus.observe { status ->
                when (status) {
                    true -> swDailyReminder.isChecked = true
                    false -> swDailyReminder.isChecked = false
                    else -> swDailyReminder.isChecked = false
                }
            }

            releaseAlarmStatus.observe { status ->
                when (status) {
                    true -> swReleaseReminder.isChecked = true
                    false -> swReleaseReminder.isChecked = false
                    else -> swReleaseReminder.isChecked = false
                }
            }
        }
    }

    companion object {
        fun newInstance() = SettingFragment().withArgs { }
    }
}