package me.hafizdwp.made_submission_final.util.ext

import android.content.res.Resources
import android.util.Log
import android.widget.Toast
import androidx.annotation.MainThread
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import me.hafizdwp.made_submission_final.BuildConfig
import me.hafizdwp.made_submission_final.MyApp
import me.hafizdwp.made_submission_final.base.BaseActivity
import me.hafizdwp.made_submission_final.base.BaseFragment
import me.hafizdwp.made_submission_final.base.BaseViewModel
import me.hafizdwp.made_submission_final.util.SharedPreferencesFactory

/**
 * @author hafizdwp
 * 10/07/19
 **/

/**
 * SharedPreference universal access
 * */
val prefs
    get() = SharedPreferencesFactory.getSharedPreferences(MyApp.getContext())

fun String?.stripIfNullOrEmpty(): String {
    return if (this.isNullOrEmpty())
        "-"
    else this
}

/**
 * LiveData
 *
 * Use this where <T> is Void/Unit
 * to make set value cleaner
 * ---------------------------------------------------------------------------------------------
 * */
@MainThread
fun <T> MutableLiveData<T>.call() {
    value = null
}

/**
 * Gson ez toJson / fromJson
 * ---------------------------------------------------------------------------------------------
 * */
val gson by lazy { Gson() }

fun <T> T.toJson(): String = gson.toJson(this)
inline fun <reified T> makeType() = object : TypeToken<T>() {}.type
inline fun <reified T> String.fromJson(): T = gson.fromJson(this,
        makeType<T>()
)

/**
 * Toast ez
 * ---------------------------------------------------------------------------------------------
 * */
var mToast: Toast? = null

fun AppCompatActivity.toast(msg: String) {
    if (mToast != null)
        mToast?.cancel()

    mToast = Toast.makeText(this, msg, Toast.LENGTH_LONG)
    mToast?.show()
}

fun <T : BaseActivity, VM : BaseViewModel>
        BaseFragment<T, VM>.toast(msg: String) {
    mActivity.toast(msg)
}

val Int.dp: Int
    get() = (this * Resources.getSystem().displayMetrics.density + 0.5f).toInt()

val Float.dp: Float
    get() = (this * Resources.getSystem().displayMetrics.density + 0.5f)

val Int.sp: Int
    get() = (this * Resources.getSystem().displayMetrics.scaledDensity * 0.5f).toInt()

val Float.sp: Float
    get() = (this * Resources.getSystem().displayMetrics.scaledDensity * 0.5f)

fun log(msg: String,
        tag: String? = null) {
    if (BuildConfig.DEBUG)
        Log.d(tag ?: "MyTag", msg)
}