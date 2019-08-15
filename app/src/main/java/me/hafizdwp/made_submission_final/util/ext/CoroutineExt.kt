package me.hafizdwp.made_submission_final.util.ext

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

/**
 * @author hafizdwp
 * 15/08/2019
 **/

fun launch(
        context: CoroutineContext = Dispatchers.Main,
        runner: suspend CoroutineScope.() -> Unit
): Job = CoroutineScope(context).launch { runner.invoke(this) }

fun <T> async(
        context: CoroutineContext = Dispatchers.IO,
        runner: suspend CoroutineScope.() -> T
): Deferred<T> = CoroutineScope(context).async { runner.invoke(this) }

fun <T> asyncAwait(
        tobeAwait: suspend () -> Deferred<T>): Deferred<T> {
        return async { tobeAwait.invoke().await() }
}


suspend inline fun <reified T> CoroutineScope.await(block: () -> Deferred<T>): T = block().await()

suspend inline fun <reified T> CoroutineScope.awaitAsync(
        context: CoroutineContext = Dispatchers.Default,
        crossinline block: () -> T
): T = withContext(context) { block() }

suspend fun <T> asyncTask(function: () -> T): T = withContext(Dispatchers.Default) { function() }