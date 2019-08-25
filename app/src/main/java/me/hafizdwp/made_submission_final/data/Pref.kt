package me.hafizdwp.made_submission_final.data

import me.hafizdwp.made_submission_final.MyApp
import me.hafizdwp.made_submission_final.util.SharedPreferencesFactory
import me.hafizdwp.made_submission_final.util.SharedPreferencesFactory.get
import me.hafizdwp.made_submission_final.util.SharedPreferencesFactory.set

/**
 * @author hafizdwp
 * 11/07/19
 **/
object Pref {

    const val PREF_LANGUAGE_API_QUERY = "pref_language_api_query"
    const val PREF_SHOULD_LOAD_GENRE = "pref_should_load_genre"
    const val PREF_GENRE_MOVIES = "pref_movies_genre"
    const val PREF_GENRE_TVSHOWS = "pref_tvshows_genre"
    const val PREF_DAILY_ALARM_STATUS = "pref_daily_alarm_status"
    const val PREF_RELEASE_ALARM_STATUS = "pref_release_alarm_status"


    private val prefs
        get() = SharedPreferencesFactory.getSharedPreferences(MyApp.getContext())

    var listMoviesGenre: String?
        get() = prefs[PREF_GENRE_MOVIES]
        set(value) {
            prefs[PREF_GENRE_MOVIES] = value
        }

    var listTvShowsGenre: String?
        get() = prefs[PREF_GENRE_TVSHOWS]
        set(value) {
            prefs[PREF_GENRE_TVSHOWS] = value
        }

    var dailyAlarmStatus: Boolean?
        get() = prefs[PREF_DAILY_ALARM_STATUS]
        set(value) {
            prefs[PREF_DAILY_ALARM_STATUS] = value
        }

    var releaseAlarmStatus: Boolean?
        get() = prefs[PREF_RELEASE_ALARM_STATUS]
        set(value) {
            prefs[PREF_RELEASE_ALARM_STATUS] = value
        }
}