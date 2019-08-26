package me.hafizdwp.made_submission_final.mvvm.setting

import androidx.lifecycle.LifecycleOwner
import kotlinx.android.synthetic.main.setting_fragment.*
import me.hafizdwp.made_submission_final.R
import me.hafizdwp.made_submission_final.base.BaseFragment
import me.hafizdwp.made_submission_final.data.Const
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
        swDailyReminder.setOnCheckedChangeListener { compoundButton, isChecked ->
            mViewModel.saveAlarmStatus(AlarmType.DAILY, isChecked)
        }
        swReleaseReminder.setOnCheckedChangeListener { compoundButton, isChecked ->
            mViewModel.saveAlarmStatus(AlarmType.RELEASE, isChecked)
        }
    }

    override fun start() {
        mViewModel.getAlarmStatus(AlarmType.DAILY)
        mViewModel.getAlarmStatus(AlarmType.RELEASE)
    }

    override fun setupObserver(): SettingViewModel? {
        return mViewModel.apply {
            dailyAlarmStatus.observe { status ->
                swDailyReminder.isChecked = when (status) {
                    true -> true
                    false -> false
                    else -> false
                }
            }

            releaseAlarmStatus.observe { status ->
                swReleaseReminder.isChecked = when (status) {
                    true -> true
                    false -> false
                    else -> false
                }
            }

            onDailySwitchChanged.observe { status ->
                when (status) {
                    true -> AlarmReceiver.setupAlarm(
                            context = requireContext(),
                            title = getString(R.string.alarm_daily_reminder_title),
                            message = getString(R.string.alarm_daily_reminder_label),
                            type = AlarmType.DAILY,
                            time = Const.ALARM_DAILY_TIME)

                    false -> AlarmReceiver.cancelAlarm(
                            context = requireContext(),
                            type = AlarmType.DAILY)
                }
            }

            onReleaseSwitchChanged.observe { status ->
                when (status) {
                    true -> AlarmReceiver.setupAlarm(
                            context = requireContext(),
                            title = getString(R.string.alarm_release_reminder_title),
                            message = getString(R.string.alarm_release_reminder_label),
                            type = AlarmType.RELEASE,
                            time = Const.ALARM_RELEASE_TIME)

                    false -> AlarmReceiver.cancelAlarm(
                            context = requireContext(),
                            type = AlarmType.RELEASE)
                }
            }
        }
    }

    companion object {
        fun newInstance() = SettingFragment().withArgs { }
    }
}