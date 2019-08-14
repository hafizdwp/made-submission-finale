package me.hafizdwp.made_submission_final.mvvm.movie

import android.os.Parcelable
import me.hafizdwp.made_submission_final.data.model.MovieResponse

/**
 * @author hafizdwp
 * 11/07/19
 **/
interface MovieActionListener : Parcelable {
    fun onMovieClick(movieId: Int, movieResponse: MovieResponse)
    fun onChangeLanguageClick() {}
}