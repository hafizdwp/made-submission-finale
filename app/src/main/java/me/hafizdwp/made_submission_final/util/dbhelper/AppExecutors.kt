package me.hafizdwp.made_submission_final.util.dbhelper

/**
 * @author hafizdwp
 * 24/07/2019
 **/

import android.os.Handler
import android.os.Looper
import java.util.concurrent.Executor
import java.util.concurrent.Executors

open class AppExecutors constructor(val diskIO: Executor = DiskIOThreadExecutor(),
                                    val networkIO: Executor = Executors.newFixedThreadPool(3),
                                    val mainThread: Executor = MainThreadExecutor()) {

    private class MainThreadExecutor : Executor {
        private val mainThreadHandler = Handler(Looper.getMainLooper())

        override fun execute(command: Runnable) {
            mainThreadHandler.post(command)
        }
    }
}