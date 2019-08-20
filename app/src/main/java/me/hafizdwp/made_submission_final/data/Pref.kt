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
    private val prefs
        get() = SharedPreferencesFactory.getSharedPreferences(MyApp.getContext())

    var listMoviesGenre: String?
        get() = prefs[Const.PREF_GENRE_MOVIES]
        set(value) {
            prefs[Const.PREF_GENRE_MOVIES] = value
        }

    var listTvShowsGenre: String?
        get() = prefs[Const.PREF_GENRE_TVSHOWS]
        set(value) {
            prefs[Const.PREF_GENRE_TVSHOWS] = value
        }
}