package me.hafizdwp.made_submission_final.base

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import me.hafizdwp.made_submission_final.util.ext.fromJson
import retrofit2.HttpException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * @author hafizdwp
 * 10/07/19
 **/
abstract class BaseViewModel(application: Application) : AndroidViewModel(application) {

    fun <T> Throwable.getErrorMessage(): String {
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