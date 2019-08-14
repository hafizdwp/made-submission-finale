package me.hafizdwp.made_submission_final

import androidx.multidex.MultiDexApplication
import com.facebook.stetho.Stetho
import io.github.inflationx.calligraphy3.CalligraphyConfig
import io.github.inflationx.calligraphy3.CalligraphyInterceptor
import io.github.inflationx.viewpump.ViewPump

/**
 * @author hafizdwp
 * 05/07/19
 **/
class MyApp : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()
        instance = this

        // Stetho
        Stetho.initializeWithDefaults(this)

        // Calligraphy
        ViewPump.init(
            ViewPump.builder()
                .addInterceptor(
                    CalligraphyInterceptor(
                        CalligraphyConfig.Builder()
                            .setDefaultFontPath("fonts/Lato-Regular.ttf")
                            .setFontAttrId(R.attr.fontPath)
                            .build()
                    )
                ).build()
        )
    }

    companion object {
        lateinit var instance: MyApp

        fun getContext() = instance.applicationContext
    }
}