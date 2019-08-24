package me.hafizdwp.made_submission_final.data.source.local

import android.content.Context
import android.net.Uri
import kotlinx.coroutines.Deferred
import me.hafizdwp.made_submission_final.data.MyContentProvider
import me.hafizdwp.made_submission_final.data.source.MyDataSource
import me.hafizdwp.made_submission_final.data.source.local.dao.FavoriteDao
import me.hafizdwp.made_submission_final.data.source.local.entity.FavoriteTable
import me.hafizdwp.made_submission_final.data.source.remote.MyResponseCallback
import me.hafizdwp.made_submission_final.util.dbhelper.AppExecutors
import me.hafizdwp.made_submission_final.util.ext.async

/**
 * @author hafizdwp
 * 24/07/2019
 **/
class MyLocalDataSource private constructor(val context: Context,
                                            val appExecutors: AppExecutors,
                                            val favoriteDao: FavoriteDao) : MyDataSource {

    override suspend fun getAllFavorited(): Deferred<List<FavoriteTable>> {
        return async {
            favoriteDao.getAllFavorited()
        }
    }

    override fun saveDataToFavorite(favoriteTable: FavoriteTable) {
        appExecutors.diskIO.execute {
            // favoriteDao.insert(favoriteTable)
            context.contentResolver.insert(
                    MyContentProvider.URI_DB,
                    FavoriteTable.toContentValues(favoriteTable))
        }
    }

    override fun deleteDataFromFavorite(favoriteTable: FavoriteTable) {
        appExecutors.diskIO.execute {
            // favoriteDao.delete(favoriteTable)
            context.contentResolver.delete(
                    Uri.parse("${MyContentProvider.URI_DB}/${favoriteTable.id}"),
                    "${FavoriteTable.COLUMN_ID}=${favoriteTable.id}",
                    null
            )
        }
    }

    override fun getMovieFromFavorite(movieId: Int, callback: MyResponseCallback<FavoriteTable>) {
        appExecutors.diskIO.execute {
            val data = favoriteDao.getFavoritedMovieById(movieId)

            appExecutors.mainThread.execute {
                if (data != null)
                    callback.onDataAvailable(data)
                else
                    callback.onDataNotAvailable()
            }
        }
    }

    override fun getTvShowFromFavorite(tvShowId: Int, callback: MyResponseCallback<FavoriteTable>) {
        appExecutors.diskIO.execute {
            val data = favoriteDao.getFavoritedTvShowById(tvShowId)

            appExecutors.mainThread.execute {
                if (data != null)
                    callback.onDataAvailable(data)
                else
                    callback.onDataNotAvailable()
            }
        }
    }

    companion object {

        private var INSTANCE: MyLocalDataSource? = null

        @JvmStatic
        fun getInstance(
                context: Context,
                appExecutors: AppExecutors,
                favoriteDao: FavoriteDao
        ): MyLocalDataSource {
            if (INSTANCE == null) {
                synchronized(MyLocalDataSource::javaClass) {
                    INSTANCE = MyLocalDataSource(context, appExecutors, favoriteDao)
                }
            }
            return INSTANCE!!
        }
    }
}