package me.hafizdwp.made_submission_final.mvvm.setting

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import me.hafizdwp.made_submission_final.R
import me.hafizdwp.made_submission_final.util.ext.log
import java.util.*

/**
 * @author hafizdwp
 * 25/08/2019
 **/
class AlarmReceiver : BroadcastReceiver() {

    companion object {

        const val EXTRA_ALARM_TITLE = "alarm_title"
        const val EXTRA_ALARM_MESSAGE = "alarm_message"
        const val EXTRA_NOTIFICATION_ID = "notification_id"
        const val TYPE_DAILY = 100
        const val TYPE_RELEASE = 200

        private fun getAlarmTypeRequestCode(alarmType: AlarmType): Int {
            return when (alarmType) {
                AlarmType.RELEASE -> TYPE_RELEASE
                AlarmType.DAILY -> TYPE_DAILY
            }
        }

        fun setupAlarm(context: Context, title: String, message: String, type: AlarmType, time: String) {
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val intent = Intent(context, AlarmReceiver::class.java).apply {
                putExtra(EXTRA_ALARM_TITLE, title)
                putExtra(EXTRA_ALARM_MESSAGE, message)
                putExtra(EXTRA_NOTIFICATION_ID, getAlarmTypeRequestCode(type))
            }

            val timeArray = time.split(":")
            val calendar = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, timeArray[0].toInt())
                set(Calendar.MINUTE, timeArray[1].toInt())
                set(Calendar.SECOND, 0)
            }

            val requestCode = getAlarmTypeRequestCode(type)
            val pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, 0)

            alarmManager.setInexactRepeating(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    AlarmManager.INTERVAL_DAY,
                    pendingIntent
            )

            Toast.makeText(context, "Repeating Alarm Activated", Toast.LENGTH_LONG).show()
        }

        fun cancelAlarm(context: Context, type: AlarmType) {
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val intent = Intent(context, AlarmReceiver::class.java)

            val requestCode = getAlarmTypeRequestCode(type)
            val pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, 0)

            pendingIntent.cancel()
            alarmManager.cancel(pendingIntent)

            Toast.makeText(context, "Repeating Alarm Deactivated", Toast.LENGTH_LONG).show()
        }
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        intent?.apply {
            val title = getStringExtra(EXTRA_ALARM_TITLE)
            val message = getStringExtra(EXTRA_ALARM_MESSAGE)
            val notificationId = getIntExtra(EXTRA_NOTIFICATION_ID, 0)
            when (notificationId) {
                TYPE_DAILY -> {
                    //TODO: ganti
                    log(title)
                    log(message)
                }
                TYPE_RELEASE -> {
                    //TODO: ganti
                    log(title)
                    log(message)
                }
                else -> {
                    throw Exception(context?.getString(R.string.error_exception))
                }
            }
        }
    }
}