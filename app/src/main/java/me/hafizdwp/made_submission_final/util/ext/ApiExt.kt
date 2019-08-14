package me.hafizdwp.made_submission_final.util.ext

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import me.hafizdwp.made_submission_final.data.source.remote.MyApiCallback

/**
 * @author hafizdwp
 * 10/07/19
 **/
fun <T> Observable<T>.onCallback(
        myApiCallback: MyApiCallback<T>
) {
    this.observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(myApiCallback)
}