package me.hafizdwp.made_submission_final.util.customview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import me.hafizdwp.made_submission_final.R

/**
 * @author hafizdwp
 * 10/07/19
 **/
class MyProgressView(ctx: Context,
                     attrs: AttributeSet) : RelativeLayout(ctx, attrs) {

    private var ivProgress: ViewGroup
    private var ivProgressAnim: ImageView
    private var tvMessage: TextView
    private var btnRetry: Button
    private var anim: Animation

    init {

        val v = LayoutInflater.from(ctx).inflate(R.layout.view_progress,
                null)

        // progress
        ivProgress = v.findViewById(R.id.progress)
        ivProgressAnim = v.findViewById(R.id.progress_anim)
        btnRetry = v.findViewById(R.id.btn_retry)
        tvMessage = v.findViewById(R.id.tv_message)

        anim = AnimationUtils.loadAnimation(ctx, R.anim.rotate_anim)
        ivProgressAnim.startAnimation(anim)

        addView(v)
    }

    fun setRetryClickListener(onClick: View.() -> Unit) {
        btnRetry.setOnClickListener { v -> onClick.invoke(v) }
    }

    fun startProgress() {
        visibility = View.VISIBLE
        ivProgress.visibility = View.VISIBLE
        btnRetry.visibility = View.GONE
        tvMessage.visibility = View.GONE

        ivProgressAnim.startAnimation(anim)
    }

    fun startProgress(msg: String) {
        visibility = View.VISIBLE
        ivProgress.visibility = View.VISIBLE
        btnRetry.visibility = View.GONE
        tvMessage.text = msg
        ivProgressAnim.startAnimation(anim)
    }

    fun stopAndGone() {
        visibility = View.GONE
    }

    fun stopAndError(errorMessage: String, isRetry: Boolean) {
        ivProgress.clearAnimation()
        ivProgress.visibility = View.GONE
        if (isRetry)
            btnRetry.visibility = View.VISIBLE
        else
            btnRetry.visibility = View.GONE
        tvMessage.visibility = View.VISIBLE
        tvMessage.text = errorMessage
    }

    fun stopAndDataEmpty(errorMessage: String) {
        btnRetry.visibility = View.GONE
        tvMessage.text = errorMessage
        tvMessage.visibility = View.VISIBLE
        ivProgressAnim.clearAnimation()
        //ivProgressAnim.setImageResource(R.mipmap.no_content_icon)
    }

    fun hideText() {
        tvMessage.visibility = View.GONE
    }
}