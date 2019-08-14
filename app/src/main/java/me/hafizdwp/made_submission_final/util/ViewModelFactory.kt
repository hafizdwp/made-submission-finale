package me.hafizdwp.made_submission_final.util

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import me.hafizdwp.made_submission_final.NoViewModel
import me.hafizdwp.made_submission_final.SplashscreenViewModel
import me.hafizdwp.made_submission_final.data.MyRepository
import me.hafizdwp.made_submission_final.data.source.local.MyAppDatabase
import me.hafizdwp.made_submission_final.data.source.local.MyLocalDataSource
import me.hafizdwp.made_submission_final.data.source.remote.MyRemoteDataSource
import me.hafizdwp.made_submission_final.mvvm.detail.DetailViewModel
import me.hafizdwp.made_submission_final.mvvm.favorite.FavoriteViewModel
import me.hafizdwp.made_submission_final.mvvm.movie.MovieViewModel
import me.hafizdwp.made_submission_final.mvvm.tvshow.TvShowViewModel
import me.hafizdwp.made_submission_final.util.dbhelper.AppExecutors

/**
 * @author hafizdwp
 * 10/07/19
 **/
class ViewModelFactory private constructor(
        private val mApplication: Application,
        private val mRepository: MyRepository
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>) =
            with(modelClass) {
                when {

                    // Skeleton view model
                    isAssignableFrom(NoViewModel::class.java) ->
                        NoViewModel(mApplication)

                    isAssignableFrom(SplashscreenViewModel::class.java) ->
                        SplashscreenViewModel(mApplication, mRepository)
                    isAssignableFrom(MovieViewModel::class.java) ->
                        MovieViewModel(mApplication, mRepository)
                    isAssignableFrom(TvShowViewModel::class.java) ->
                        TvShowViewModel(mApplication, mRepository)
                    isAssignableFrom(FavoriteViewModel::class.java) ->
                        FavoriteViewModel(mApplication, mRepository)
                    isAssignableFrom(DetailViewModel::class.java) ->
                        DetailViewModel(mApplication, mRepository)

                    else ->
                        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
                }
            } as T

    companion object {

        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var INSTANCE: ViewModelFactory? = null

        @JvmStatic
        fun getInstance(mApplication: Application) =
                INSTANCE ?: synchronized(ViewModelFactory::class.java) {
                    INSTANCE ?: ViewModelFactory(
                            mApplication,
                            provideMyRepository(mApplication.applicationContext)
                    )
                            .also { INSTANCE = it }
                }

        @JvmStatic
        fun provideMyRepository(context: Context): MyRepository {
            val localDatabase = MyAppDatabase.getInstance(context)

            return MyRepository.getInstance(
                    MyRemoteDataSource,
                    MyLocalDataSource.getInstance(
                            AppExecutors(),
                            localDatabase.favoriteDao()
                    )
            )
        }
    }
}