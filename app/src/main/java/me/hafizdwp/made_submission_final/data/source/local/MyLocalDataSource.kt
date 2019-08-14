package me.hafizdwp.made_submission_final.data.source.local

import me.hafizdwp.made_submission_final.data.MyResponseCallback
import me.hafizdwp.made_submission_final.data.source.local.dao.FavoriteDao
import me.hafizdwp.made_submission_final.data.source.local.entity.FavoriteTable
import me.hafizdwp.made_submission_final.data.source.remote.MyDataSource
import me.hafizdwp.made_submission_final.util.dbhelper.AppExecutors

/**
 * @author hafizdwp
 * 24/07/2019
 **/
class MyLocalDataSource private constructor(val appExecutors: AppExecutors,
                                            val favoriteDao: FavoriteDao) : MyDataSource {

    override fun saveDataToFavorite(favoriteTable: FavoriteTable) {
        appExecutors.diskIO.execute {
            favoriteDao.insert(favoriteTable)
        }
    }

    override fun deleteDataFromFavorite(favoriteTable: FavoriteTable) {
        appExecutors.diskIO.execute {
            favoriteDao.delete(favoriteTable)
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

    override fun getMoviesFromFavorite(callback: MyResponseCallback<List<FavoriteTable>>) {
        appExecutors.diskIO.execute {
            val data = favoriteDao.getAllFavoritedMovies()

            appExecutors.mainThread.execute {
                if (!data.isNullOrEmpty())
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

    override fun getTvShowsFromFavorite(callback: MyResponseCallback<List<FavoriteTable>>) {
        appExecutors.diskIO.execute {
            val data = favoriteDao.getAllFavoritedTvShow()

            appExecutors.mainThread.execute {
                if (!data.isNullOrEmpty())
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
                appExecutors: AppExecutors,
                favoriteDao: FavoriteDao
        ): MyLocalDataSource {
            if (INSTANCE == null) {
                synchronized(MyLocalDataSource::javaClass) {
                    INSTANCE = MyLocalDataSource(appExecutors, favoriteDao)
                }
            }
            return INSTANCE!!
        }
    }
}