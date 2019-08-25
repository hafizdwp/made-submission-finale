package me.hafizdwp.made_submission_final

import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.ConfigurationCompat
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main._splashscreen.*
import me.hafizdwp.made_submission_final.data.Const
import me.hafizdwp.made_submission_final.data.Pref
import me.hafizdwp.made_submission_final.mvvm.MainActivity
import me.hafizdwp.made_submission_final.util.SharedPreferencesFactory.set
import me.hafizdwp.made_submission_final.util.ext.*

/**
 * @author hafizdwp
 * 10/07/19
 **/
class SplashscreenActivity : AppCompatActivity() {

    lateinit var mViewModel: SplashscreenViewModel
    var mHandler: Handler? = null
    var mRunnable: Runnable? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout._splashscreen)

        mViewModel = obtainViewModel()

        // Set language api query in preference
        // based on current device's language/locale
        val currentLocale = ConfigurationCompat.getLocales(resources.configuration)[0]
        when (currentLocale.language) {
            "in" -> {
                prefs[Pref.PREF_LANGUAGE_API_QUERY] = "id"
            }
            "en" -> // English
                prefs[Pref.PREF_LANGUAGE_API_QUERY] = "en-US"
        }

        setupObserver()

        mRunnable = Runnable {
            val listMoviesGenre: String? = Pref.listMoviesGenre
            val listTvShowsGenre: String? = Pref.listTvShowsGenre

            // Kalau salah satu kosong, panggil API
            // else go to MainActivity
            if (listMoviesGenre.isNullOrBlank() || listTvShowsGenre.isNullOrBlank())
                mViewModel.start()
            else
                gotoMain()
        }

        mHandler = Handler()
        mHandler?.postDelayed(mRunnable, 2000)
    }

    private fun setupObserver() {
        mViewModel.apply {
            startProgress.observe(this@SplashscreenActivity, Observer {
                progressBar.visible()
            })

            listMoviesGenre.observe(this@SplashscreenActivity, Observer { listGenre ->
                Pref.listMoviesGenre = listGenre.toJson()
            })

            listTvShowsGenre.observe(this@SplashscreenActivity, Observer { listGenre ->
                Pref.listTvShowsGenre = listGenre.toJson()
            })

            requestSuccess.observe(this@SplashscreenActivity, Observer {
                progressBar.gone()

                gotoMain()
            })
        }
    }

    private fun gotoMain() {
        MainActivity.startActivity(this@SplashscreenActivity)
        finishAffinity()
    }

    override fun onDestroy() {
        mHandler?.removeCallbacks(mRunnable)

        super.onDestroy()
    }
}