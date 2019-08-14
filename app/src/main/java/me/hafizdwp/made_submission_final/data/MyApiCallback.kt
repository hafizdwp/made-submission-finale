package me.hafizdwp.made_submission_final.data

import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import me.hafizdwp.made_submission_final.base.BaseApiModel
import me.hafizdwp.made_submission_final.util.ext.fromJson
import retrofit2.HttpException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * @author hafizdwp
 * 10/07/19
 **/
abstract class MyApiCallback<T> : Observer<T> {

    abstract fun onSuccess(data: T)
    abstract fun onFailure(code: Int, errorMessage: String)
    //abstract fun onApiSubscribe(disposable: Disposable)

    override fun onNext(t: T) {
        onSuccess(t)
    }

    override fun onError(e: Throwable) {
        e.printStackTrace()
        when (e) {

            is UnknownHostException ->
                onFailure(-100, "No Internet Connection")
            is SocketTimeoutException ->
                onFailure(-200, "Internet Disconnected")

            is HttpException -> {

                val code = e.code()
                var msg = e.message()
                var errorDao: BaseApiModel<T>? = null

                try {
                    val body = e.response().errorBody()
                    errorDao = body?.string()?.fromJson()

                } catch (exception: Exception) {

                    // When extracting the body failed,
                    // return with throwable message instead
                    onFailure(code, exception.message!!)
                }

                when (code) {
                    500 -> msg = errorDao?.status_message ?: "Internal Server Error"
                    504 -> msg = errorDao?.status_message ?: "Error Response"
                    502, 404 ->
                        msg = errorDao?.status_message ?: "Error Connect or Not Found"
                    400 -> msg = errorDao?.status_message ?: "Bad Request"
                    401 -> msg = errorDao?.status_message ?: "Not Authorized"
                }

                // Return with code and filtered msg
                onFailure(code, msg)
            }

            else ->
                onFailure(-300, e.message!!)
        }
    }

    override fun onSubscribe(d: Disposable) {
        //onApiSubscribe(d)
    }

    override fun onComplete() {

    }
}