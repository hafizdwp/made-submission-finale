package me.hafizdwp.made_submission_final.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.core.os.ConfigurationCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import me.hafizdwp.made_submission_final.data.Constant
import me.hafizdwp.made_submission_final.util.SharedPreferencesFactory.get
import me.hafizdwp.made_submission_final.util.SharedPreferencesFactory.set
import me.hafizdwp.made_submission_final.util.ext.prefs

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

    abstract fun onViewReady()
    abstract fun start() /// If u have API request when opening the page, call in this fun
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

//        // Set language api query in preference
//        // based on current device's language/locale
//        val localeBefore: String = prefs[Constant.PREF_LANGUAGE_API_QUERY] ?: ""
//        val currentLocale = ConfigurationCompat.getLocales(resources.configuration)[0]
//        when (currentLocale.language) {
//            "in" -> {
//
//                // check if user has changed language
//                if (localeBefore == "en-US")
//                    prefs[Constant.PREF_SHOULD_LOAD_GENRE] = true
//
//                prefs[Constant.PREF_LANGUAGE_API_QUERY] = "id"
//            }
//            "en" -> {
//
//                // check if user has changed language
//                if (localeBefore == "id")
//                    prefs[Constant.PREF_SHOULD_LOAD_GENRE] = true
//
//                prefs[Constant.PREF_LANGUAGE_API_QUERY] = "en-US"
//            }
//        }

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