package me.hafizdwp.made_submission_final.data.source.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import me.hafizdwp.made_submission_final.BuildConfig
import me.hafizdwp.made_submission_final.data.source.local.dao.FavoriteDao
import me.hafizdwp.made_submission_final.data.source.local.entity.FavoriteTable

/**
 * @author hafizdwp
 * 24/07/2019
 **/

@Database(
        entities = [
            FavoriteTable::class],
        version = 1)
abstract class MyAppDatabase : RoomDatabase() {

    abstract fun favoriteDao(): FavoriteDao


    companion object {

        @Volatile
        private var INSTANCE: MyAppDatabase? = null

        fun getInstance(context: Context): MyAppDatabase =
                INSTANCE ?: synchronized(this) {
                    INSTANCE ?: buildDatabase(context).also {
                        INSTANCE = it
                    }
                }

        private fun buildDatabase(context: Context) =
                Room.databaseBuilder(
                        context.applicationContext,
                        MyAppDatabase::class.java,
                        "movie.db")
                        .fallbackToDestructiveMigration()
                        .build()
    }
}