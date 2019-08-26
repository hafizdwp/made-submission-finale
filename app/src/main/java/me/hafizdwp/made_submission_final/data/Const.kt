package me.hafizdwp.made_submission_final.data

import me.hafizdwp.made_submission_final.BuildConfig
import me.hafizdwp.made_submission_final.util.SharedPreferencesFactory.get
import me.hafizdwp.made_submission_final.util.ext.prefs
import java.text.SimpleDateFormat
import java.util.*

/**
 * @author hafizdwp
 * 10/07/19
 **/
object Const {

    const val BASE_IMAGE_PATH = "https://image.tmdb.org/t/p/original"
    const val DATABASE_NAME = "cinema.db"

    const val ALARM_DAILY_TIME = "07:00:00"
    const val ALARM_RELEASE_TIME = "08:00:00"

    fun getApiKey(): String {
        return BuildConfig.BASE_API_KEY
    }

    fun getLanguagePreference(): String {
        return prefs[Pref.PREF_LANGUAGE_API_QUERY] ?: "en-US"
    }

    fun getTime(): String {
        val calendar = Calendar.getInstance()
        calendar.timeZone = TimeZone.getTimeZone("GMT+07:00")

        return SimpleDateFormat("yyyy-MM-dd").format(calendar.time)
    }
}