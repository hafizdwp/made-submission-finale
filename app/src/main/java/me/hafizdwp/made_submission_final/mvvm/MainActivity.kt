package me.hafizdwp.made_submission_final.mvvm

import android.content.Context
import android.os.Handler
import androidx.fragment.app.Fragment
import me.hafizdwp.made_submission_final.R
import me.hafizdwp.made_submission_final.base.BaseActivity
import me.hafizdwp.made_submission_final.util.ext.startActivity
import me.hafizdwp.made_submission_final.util.ext.toast

/**
 * @author hafizdwp
 * 10/07/19
 **/
class MainActivity : BaseActivity() {

    override val bindLayoutRes: Int
        get() = R.layout.main_activity
    override val bindFragmentContainerId: Int
        get() = R.id.frameMainContent
    override val bindFragmentInstance: Fragment
        get() = MainFragment.newInstance()


    /// Double-backpress-to-exit-mechanism
    private var doubleBackPressToExit = false
    private var handlerBack: Handler? = Handler()
    private var runnableBack = Runnable { doubleBackPressToExit = false }

    override fun onBackPressed() {
        if (doubleBackPressToExit) {
            supportFinishAfterTransition()
            return
        }

        toast(getString(R.string.double_back_to_exit))
        doubleBackPressToExit = true
        handlerBack?.postDelayed(runnableBack, 2000)
    }

    companion object {
        fun startActivity(context: Context) {
            context.startActivity<MainActivity>()
        }
    }
}