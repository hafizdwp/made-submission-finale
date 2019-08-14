package me.hafizdwp.made_submission_final.base

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView

/**
 * @author hafizdwp
 * 10/07/19
 **/
abstract class BaseRecyclerAdapter<MODEL>
    : RecyclerView.Adapter<BaseRecyclerAdapter<MODEL>.BaseViewHolder>() {

    @get:LayoutRes
    abstract val bindItemLayoutRes: Int
    abstract val mListItem: List<MODEL>
    abstract fun onBind(itemView: View, model: MODEL)
    open fun onGetItemCount(): Int {
        return mListItem.size
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(bindItemLayoutRes, parent, false)

        return BaseViewHolder(view)
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.bind(mListItem[position])
    }

    override fun getItemCount(): Int {
        return onGetItemCount()
    }

    inner class BaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(model: MODEL) {
            onBind(itemView, model)
        }
    }
}