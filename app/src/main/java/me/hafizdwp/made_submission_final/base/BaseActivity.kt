package me.hafizdwp.made_submission_final.base

import android.content.Context
import android.os.Bundle
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.ConfigurationCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import io.github.inflationx.viewpump.ViewPumpContextWrapper
import me.hafizdwp.made_submission_final.data.Constant
import me.hafizdwp.made_submission_final.util.SharedPreferencesFactory.get
import me.hafizdwp.made_submission_final.util.SharedPreferencesFactory.set
import me.hafizdwp.made_submission_final.util.ext.prefs

/**
 * @author hafizdwp
 * 10/07/19
 **/
abstract class BaseActivity : AppCompatActivity() {

    @get:LayoutRes
    abstract val bindLayoutRes: Int
    @get:IdRes
    abstract val bindFragmentContainerId: Int
    abstract val bindFragmentInstance: Fragment

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase!!))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(bindLayoutRes)

        // Set language api query in preference
        // based on current device's language/locale
        val localeBefore: String = prefs[Constant.PREF_LANGUAGE_API_QUERY] ?: ""
        val currentLocale = ConfigurationCompat.getLocales(resources.configuration)[0]
        when (currentLocale.language) {
            "in" -> {

                // check if user has changed language
                if (localeBefore == "en-US")
                    prefs[Constant.PREF_SHOULD_LOAD_GENRE] = true

                prefs[Constant.PREF_LANGUAGE_API_QUERY] = "id"
            }
            "en" -> {

                // check if user has changed language
                if (localeBefore == "id")
                    prefs[Constant.PREF_SHOULD_LOAD_GENRE] = true

                prefs[Constant.PREF_LANGUAGE_API_QUERY] = "en-US"
            }
        }

        setupFragment()
    }

    open fun setupFragment() {
        inflateFragment(bindFragmentContainerId, bindFragmentInstance)
    }

    private fun inflateFragment(
        frameId: Int,
        fragment: Fragment
    ) {
        supportFragmentManager.transact {
            replace(frameId, fragment)
        }
    }

    private inline fun FragmentManager.transact(action: FragmentTransaction.() -> Unit) {
        beginTransaction().apply {
            action()
        }.commit()
    }
}