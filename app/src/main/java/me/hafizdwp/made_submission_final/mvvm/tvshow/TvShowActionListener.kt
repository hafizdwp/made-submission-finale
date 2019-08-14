package me.hafizdwp.made_submission_final.mvvm.tvshow

import android.os.Parcelable
import me.hafizdwp.made_submission_final.data.source.remote.model.TvShowResponse

/**
 * @author hafizdwp
 * 01/08/2019
 **/
interface TvShowActionListener : Parcelable {
    fun onTvShowClick(tvShowId: Int, tvShowResponse: TvShowResponse)
    fun onChangeLanguageClick() {}
}