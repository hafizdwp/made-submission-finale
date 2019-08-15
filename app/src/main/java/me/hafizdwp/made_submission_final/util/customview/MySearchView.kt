package me.hafizdwp.made_submission_final.util.customview

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.SearchView

/**
 * @author hafizdwp
 * 15/08/2019
 **/
class MySearchView(ctx: Context,
                   attrs: AttributeSet) : SearchView(ctx, attrs) {

    private var mListener: MySearchViewListener? = null

    override fun onActionViewExpanded() {
        super.onActionViewExpanded()
        mListener?.onSearchViewExpanded()
    }

    override fun onActionViewCollapsed() {
        super.onActionViewCollapsed()
        mListener?.onSearchViewCollapsed()
    }

    fun setExcolListener(listener: MySearchViewListener) {
        mListener = listener
    }

    interface MySearchViewListener {
        fun onSearchViewExpanded()
        fun onSearchViewCollapsed()
    }
}