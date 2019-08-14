package me.hafizdwp.made_submission_final.data

/**
 * @author hafizdwp
 * 10/07/19
 **/
interface MyResponseCallback<T> {
    fun onDataAvailable(data: T?)
    fun onDataNotAvailable()
    fun onError(code: Int?, errorMessage: String?)
}