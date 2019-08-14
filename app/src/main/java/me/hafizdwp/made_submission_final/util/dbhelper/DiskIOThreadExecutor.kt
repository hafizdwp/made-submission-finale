package me.hafizdwp.made_submission_final.util.dbhelper

import java.util.concurrent.Executor
import java.util.concurrent.Executors

/**
 * @author hafizdwp
 * 24/07/2019
 **/
class DiskIOThreadExecutor : Executor {

    private val diskIO = Executors.newSingleThreadExecutor()

    override fun execute(command: Runnable) {
        diskIO.execute(command)
    }
}