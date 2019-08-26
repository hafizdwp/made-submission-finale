package me.hafizdwp.made_submission_final.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.TaskStackBuilder
import me.hafizdwp.made_submission_final.R
import me.hafizdwp.made_submission_final.data.source.remote.model.MovieResponse
import me.hafizdwp.made_submission_final.mvvm.MainActivity
import me.hafizdwp.made_submission_final.mvvm.detail.DetailActivity
import me.hafizdwp.made_submission_final.mvvm.setting.AlarmReceiver
import java.util.*

/**
 * @author hafizdwp
 * 26/08/2019
 **/
class MyNotification {

    companion object {
        fun createNotificationDaily(context: Context, title: String?, message: String?) {
            
            val intent = Intent(context, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            val pendingIntent = TaskStackBuilder.create(context).run {
                addNextIntentWithParentStack(intent)
                getPendingIntent(AlarmReceiver.TYPE_DAILY, PendingIntent.FLAG_UPDATE_CURRENT)
            }

            sendNotification(context, title, message, null, pendingIntent)
        }

        fun createNotificationRelease(context: Context, title: String?, message: String?, bitmap: Bitmap?,
                                      movieResponse: MovieResponse?) {

            val intent = Intent(context, DetailActivity::class.java).apply {
                putExtra(DetailActivity.EXTRA_MOVIE_ID, movieResponse?.id ?: -1)
                putExtra(DetailActivity.EXTRA_MOVIE_RESPONSE, movieResponse)
            }
            val pendingIntent = TaskStackBuilder.create(context).run {
                addNextIntentWithParentStack(intent)
                getPendingIntent(AlarmReceiver.TYPE_RELEASE, PendingIntent.FLAG_UPDATE_CURRENT)
            }

            sendNotification(context, title, message, bitmap, pendingIntent)
        }

        private fun sendNotification(context: Context,
                                     title: String?,
                                     message: String?,
                                     bitmap: Bitmap?,
                                     pendingIntent: PendingIntent?) {

            // ID of notification
            // Generate unique id for notification
            val randomId = Random().nextInt(9999 - 1000) + 1000

            val notificationManager: NotificationManager? = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            notificationManager?.let {

                var builder: NotificationCompat.Builder? = null

                @RequiresApi(26)
                if (isAboveOreo) {
                    val channel = NotificationChannel(
                            context.getString(R.string.app_name),
                            message,
                            NotificationManager.IMPORTANCE_HIGH
                    ).apply {
                        description = message
                        enableVibration(true)
                        enableLights(true)
                        lightColor = Color.WHITE
                    }

                    // Create channel
                    it.createNotificationChannel(channel)

                    // Instantiate builder
                    builder = NotificationCompat.Builder(context, channel.id)
                    builder.setChannelId(channel.id)
                }

                // Instantiate builder if below oreo
                if (builder == null)
                    builder = NotificationCompat.Builder(context)


                builder.apply {
                    setContentTitle(title)
                    setContentText(message)
                    setContentIntent(pendingIntent)
                    setTicker(title)
                    setSmallIcon(R.drawable.ic_cinema)
                    setAutoCancel(true)
                    setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                    if (bitmap != null)
                        setStyle(NotificationCompat
                                .BigPictureStyle()
                                .bigPicture(bitmap)
                        )
                }

                // Push the notification
                it.notify(randomId, builder.build())
            }
        }

        private val isAboveOreo
            get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
    }
}