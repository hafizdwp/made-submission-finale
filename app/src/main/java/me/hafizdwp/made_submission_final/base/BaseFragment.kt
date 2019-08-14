package me.hafizdwp.made_submission_final.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

/**
 * @author hafizdwp
 * 10/07/19
 **/
abstract class BaseFragment<T : BaseActivity, VM : BaseViewModel> : androidx.fragment.app.Fragment() {

    @get:LayoutRes
    abstract val bindLayoutRes: Int
    abstract val mViewModel: VM
    abstract val mLifecycleOwner: LifecycleOwner

    lateinit var mActivity: T
    lateinit var mContext: Context
    var mView: View? = null

    /**
     * Init any setup view on this fun
     * */
    abstract fun onViewReady()

    /**
     * Used to call any API that needed to load
     * whenever fragment is created
     * */
    abstract fun start()

    abstract fun setupObserver(): VM?


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mActivity = requireActivity() as T
        mContext = requireContext()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mView = inflater.inflate(bindLayoutRes, container, false)

        return mView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupObserver()
        onViewReady()
        start()
    }


    fun <T> MutableLiveData<T>.observe(action: (T?) -> Unit) {
        observe(mLifecycleOwner, Observer {
            action.invoke(it)
        })
    }

    fun <T> MutableLiveData<T>.observeOnce(action: (T?) -> Unit) {

        val observer: Observer<T> = object : Observer<T> {
            override fun onChanged(t: T?) {
                action.invoke(t)
                removeObserver(this)
            }
        }

        observe(mLifecycleOwner, observer)
    }
}