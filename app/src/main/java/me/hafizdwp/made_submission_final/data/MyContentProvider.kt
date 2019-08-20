package me.hafizdwp.made_submission_final.data

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import me.hafizdwp.made_submission_final.data.source.local.MyAppDatabase
import me.hafizdwp.made_submission_final.data.source.local.dao.FavoriteDao
import me.hafizdwp.made_submission_final.data.source.local.entity.FavoriteTable
import me.hafizdwp.made_submission_final.util.ext.log

/**
 * @author hafizdwp
 * 20/08/2019
 **/
class MyContentProvider : ContentProvider() {

    private lateinit var database: MyAppDatabase
    private lateinit var favoriteDao: FavoriteDao

    companion object {
        const val AUTHORITY = "me.hafizdwp.made_submission_final.provider"
        const val PATH = Const.DATABASE_NAME
        const val CODE_DB_DIR = 1
        const val CODE_DB_ITEM = 2

        val URI_DB: Uri = Uri.parse("content://$AUTHORITY/$PATH")
    }

    private val uriMatcher = UriMatcher(UriMatcher.NO_MATCH).apply {
        addURI(AUTHORITY, PATH, CODE_DB_DIR)
        addURI(AUTHORITY, "$PATH/*", CODE_DB_ITEM)
    }

    override fun onCreate(): Boolean {
        database = MyAppDatabase.getInstance(context!!)
        favoriteDao = database.favoriteDao()
        return true
    }

    override fun insert(uri: Uri, contentValues: ContentValues?): Uri? {
        log("PROVIDER insert")

        when (uriMatcher.match(uri)) {
            CODE_DB_DIR -> {
                log("PROVIDER insert dir")
                val context = context ?: return null
                val favoriteTable = FavoriteTable.fromContentValues(contentValues!!)
                val id = favoriteDao.insert(favoriteTable)

                // Notify
                context.contentResolver.notifyChange(uri, null)
                return ContentUris.withAppendedId(uri, id)
            }

            CODE_DB_ITEM -> {
                log("PROVIDER insert item")
                throw IllegalArgumentException("Invalid URI, cannot insert with ID: $uri")
            }

            else ->
                throw IllegalArgumentException("Unknown URI: $uri")
        }
    }

    override fun query(uri: Uri, p1: Array<out String>?, p2: String?, p3: Array<out String>?, p4: String?): Cursor? {
        log("PROVIDER query")

        when (val code = uriMatcher.match(uri)) {
            CODE_DB_DIR, CODE_DB_ITEM -> {
                log("PROVIDER query enter")
                val context = context ?: return null
                val cursor = if (code == CODE_DB_DIR) {
                    log("PROVIDER query enter dir")
                    favoriteDao.getAllFavoritedProvider()
                } else {
                    log("PROVIDER query enter item")
                    favoriteDao.getAllFavoritedByIdProvider(ContentUris.parseId(uri))
                }

                cursor.setNotificationUri(context.contentResolver, uri)
                return cursor
            }
            else -> {
                throw IllegalArgumentException("Unknown URI: $uri")
            }
        }
    }

    override fun update(p0: Uri, p1: ContentValues?, p2: String?, p3: Array<out String>?): Int {
        return -1 // unused
    }

    override fun delete(uri: Uri, p1: String?, p2: Array<out String>?): Int {
        log("PROVIDER delete")
        when (uriMatcher.match(uri)) {
            CODE_DB_DIR -> {
                log("PROVIDER delete dir")
                throw IllegalArgumentException("Invalid URI, cannot update without ID $uri")
            }
            CODE_DB_ITEM -> {
                log("PROVIDER delete item")
                val context = context ?: return 0
                val count = favoriteDao.deleteById(ContentUris.parseId(uri))

                // Notify
                context.contentResolver.notifyChange(uri, null)
                return count
            }
            else -> throw IllegalArgumentException("Unknown URI: $uri")
        }
    }

    override fun getType(uri: Uri): String? {
        return when (uriMatcher.match(uri)) {
            CODE_DB_DIR -> "vnd.android.cursor.dir/$AUTHORITY.$PATH"
            CODE_DB_ITEM -> "vnd.android.cursor.item/$AUTHORITY.$PATH"
            else -> throw IllegalArgumentException("Unknown URI: $uri")
        }
    }
}