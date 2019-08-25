package me.hafizdwp.made_submission_final.mvvm.setting

import android.app.Application
import androidx.lifecycle.MutableLiveData
import me.hafizdwp.made_submission_final.base.BaseViewModel
import me.hafizdwp.made_submission_final.data.MyRepository

/**
 * @author hafizdwp
 * 15/08/2019
 **/
class SettingViewModel(application: Application,
                       val mRepository: MyRepository) : BaseViewModel(application) {

    val dailyAlarmStatus = MutableLiveData<Boolean>()
    val releaseAlarmStatus = MutableLiveData<Boolean>()


    fun saveAlarmStatus(alarmType: AlarmType,
                        status: Boolean) {
        mRepository.saveAlarmStatus(alarmType, status)
    }

    fun getAlarmStatus(alarmType: AlarmType) {
        when (alarmType) {
            AlarmType.DAILY ->
                dailyAlarmStatus.value = mRepository.getAlarmStatus(alarmType)
            AlarmType.RELEASE ->
                releaseAlarmStatus.value = mRepository.getAlarmStatus(alarmType)
        }
    }
}
