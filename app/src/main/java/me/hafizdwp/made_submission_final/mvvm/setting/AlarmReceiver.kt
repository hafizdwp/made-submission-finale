package me.hafizdwp.made_submission_final.mvvm.setting

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import me.hafizdwp.made_submission_final.R
import me.hafizdwp.made_submission_final.base.BaseApiModel
import me.hafizdwp.made_submission_final.data.Const
import me.hafizdwp.made_submission_final.data.source.remote.ApiServiceFactory
import me.hafizdwp.made_submission_final.data.source.remote.api.MovieApi
import me.hafizdwp.made_submission_final.data.source.remote.model.MovieResponse
import me.hafizdwp.made_submission_final.util.MyNotification
import me.hafizdwp.made_submission_final.util.ext.await
import me.hafizdwp.made_submission_final.util.ext.fromJson
import me.hafizdwp.made_submission_final.util.ext.launch
import me.hafizdwp.made_submission_final.util.ext.toast
import retrofit2.HttpException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.*

/**
 * @author hafizdwp
 * 25/08/2019
 **/
class AlarmReceiver : BroadcastReceiver() {

    companion object {

        private const val EXTRA_ALARM_TITLE = "alarm_title"
        private const val EXTRA_ALARM_MESSAGE = "alarm_message"
        private const val EXTRA_ALARM_TYPE = "alarm_type"
        const val TYPE_DAILY = 100
        const val TYPE_RELEASE = 200

        private fun getAlarmTypeCode(alarmType: AlarmType): Int {
            return when (alarmType) {
                AlarmType.RELEASE -> TYPE_RELEASE
                AlarmType.DAILY -> TYPE_DAILY
            }
        }

        fun setupAlarm(context: Context, title: String?, message: String?, type: AlarmType, time: String) {
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val intent = Intent(context, AlarmReceiver::class.java).apply {
                putExtra(EXTRA_ALARM_TITLE, title)
                putExtra(EXTRA_ALARM_MESSAGE, message)
                putExtra(EXTRA_ALARM_TYPE, getAlarmTypeCode(type))
            }

            val timeArray = time.split(":")
            val calendar = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, timeArray[0].toInt())
                set(Calendar.MINUTE, timeArray[1].toInt())
                set(Calendar.SECOND, 0)
            }

            val requestCode = getAlarmTypeCode(type)
            val pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, 0)

            alarmManager.setInexactRepeating(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    AlarmManager.INTERVAL_DAY,
                    pendingIntent
            )

            context.toast {
                when (type) {
                    AlarmType.DAILY -> {
                        context.getString(R.string.alarm_daily_activated)
                    }
                    AlarmType.RELEASE -> {
                        context.getString(R.string.alarm_release_activated)
                    }
                }
            }
        }

        fun cancelAlarm(context: Context, type: AlarmType) {
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val intent = Intent(context, AlarmReceiver::class.java)

            val requestCode = getAlarmTypeCode(type)
            val pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, 0)

            pendingIntent.cancel()
            alarmManager.cancel(pendingIntent)

            context.toast {
                when (type) {
                    AlarmType.DAILY -> {
                        context.getString(R.string.alarm_daily_deactivated)
                    }
                    AlarmType.RELEASE -> {
                        context.getString(R.string.alarm_release_deactivated)
                    }
                }
            }
        }
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        intent?.apply {
            val title = getStringExtra(EXTRA_ALARM_TITLE)
            val message = getStringExtra(EXTRA_ALARM_MESSAGE)
            val typeCode = getIntExtra(EXTRA_ALARM_TYPE, 0)
            when (typeCode) {
                TYPE_DAILY -> {
                    MyNotification.createNotificationDaily(
                            requireNotNull(context), title, message)
                }
                TYPE_RELEASE -> {
                    getNewReleaseMovieThenShowNotif(requireNotNull(context), title, message)
                }
                else -> {
                    throw Exception(context?.getString(R.string.error_exception))
                }
            }
        }
    }

    private fun getNewReleaseMovieThenShowNotif(context: Context, title: String?, message: String?) {
        launch {

            val movieApi = ApiServiceFactory.builder<MovieApi>()

            try {
                val response = await { movieApi.getNewReleaseMovie() }
                val listMovies = response.results
                if (!listMovies.isNullOrEmpty()) {
                    val data = listMovies[0]
                    val movieTitle = data.title ?: title // override title
                    val imageUrl = Const.BASE_IMAGE_PATH + data.backdrop_path
                    val bitmap: Bitmap? = try {
                        Glide.with(context)
                                .asBitmap()
                                .load(imageUrl)
                                .apply(RequestOptions()
                                        .centerCrop()
                                        .transform(RoundedCorners(48)))
                                .submit()
                                .get()
                    } catch (e: java.lang.Exception) {
                        null
                    }

                    // push notif
                    MyNotification.createNotificationRelease(
                            context, movieTitle, message, bitmap, data)
                }
            } catch (e: Throwable) {
                e.getErrorMessage<BaseApiModel<List<MovieResponse>>>()
            }
        }
    }

    private fun <T> Throwable.getErrorMessage(): String {
        when (this) {
            is UnknownHostException ->
                return ("No Internet Connection")

            is SocketTimeoutException ->
                return ("Internet Disconnected")

            is HttpException -> {

                val code = this.code()
                var msg = this.message()
                var errorDao: BaseApiModel<T>? = null

                try {
                    val body = this.response()?.errorBody()
                    errorDao = body?.string()?.fromJson()

                } catch (exception: Exception) {

                    // When extracting the body failed,
                    // return with throwable message instead
                    return (exception.message!!)
                }

                when (code) {
                    500 -> msg = errorDao?.status_message ?: "Internal Server Error"
                    503 -> msg = errorDao?.status_message ?: "Server Error"
                    504 -> msg = errorDao?.status_message ?: "Error Response"
                    502, 404 ->
                        msg = errorDao?.status_message ?: "Error Connect or Not Found"
                    400 -> msg = errorDao?.status_message ?: "Bad Request"
                    401 -> msg = errorDao?.status_message ?: "Not Authorized"
                }

                // Return with code and filtered msg
                return (msg)
            }

            else ->
                return (this.message!!)
        }
    }
}