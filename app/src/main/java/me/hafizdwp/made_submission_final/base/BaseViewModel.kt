package me.hafizdwp.made_submission_final.base

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import io.reactivex.disposables.Disposable

/**
 * @author hafizdwp
 * 10/07/19
 **/
abstract class BaseViewModel(application: Application) : AndroidViewModel(application) {

//    open val listDisposable: List<Disposable?>? = null
//    open val classNameForDisposeLoggingTag: String? = null
//
//    /**
//     *  clear disposable
//     *  to cancel any in-progress API request
//     * */
//    fun clearDisposable() {
//        listDisposable?.let {
//            it.forEach { disposable ->
//                disposable?.dispose()
//
//                if (disposable != null)
//                    loggerDebug("${classNameForDisposeLoggingTag ?: ""} disposed()")
//            }
//        }
//    }
}