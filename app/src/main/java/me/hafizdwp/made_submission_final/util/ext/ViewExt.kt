package me.hafizdwp.made_submission_final.util.ext

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.View
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.google.android.material.bottomnavigation.BottomNavigationView

/**
 * @author hafizdwp
 * 11/07/19
 **/
fun View.visible() {
    visibility = View.VISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

fun View.invisible() {
    visibility = View.VISIBLE
}

@SuppressLint("RestrictedApi")
fun BottomNavigationView.disableShiftingMode() {
    val menuView = getChildAt(0) as BottomNavigationMenuView
    try {
        val shiftingMode = menuView.javaClass.getDeclaredField("mShiftingMode")
        shiftingMode.isAccessible = true
        shiftingMode.setBoolean(menuView, false)
        shiftingMode.isAccessible = false
        for (i in 0 until menuView.childCount) {
            val item = menuView.getChildAt(i) as BottomNavigationItemView
            item.setShifting(false)
            item.setChecked(item.itemData.isChecked)
        }
    } catch (e: NoSuchFieldException) {
        Log.e("disableShiftingMode", "disableShiftingMode: Unable to get shift mode field")
    } catch (e: IllegalAccessException) {
        Log.e("disableShiftingMode", "disableShiftingMode: Unable to change value of shift mode")
    }
}

fun Context.myCircularProgressDrawable(): androidx.swiperefreshlayout.widget.CircularProgressDrawable =
        androidx.swiperefreshlayout.widget.CircularProgressDrawable(this).apply {
            strokeWidth = 5f
            centerRadius = 30f
            start()
        }

fun <T : Drawable> RequestBuilder<T>.withLoadingPlaceholder(context: Context): RequestBuilder<T> {
    return this.apply(
            RequestOptions().placeholder(context.myCircularProgressDrawable()))
}