package me.hafizdwp.made_submission_final.base

import androidx.room.*

/**
 * @author hafizdwp
 * 24/07/2019
 **/
interface BaseDao<T> {
    /**
     * Insert an object in the database.
     *
     * @param obj the object to be inserted.
     * @return The SQLite row id
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(obj: T): Long

    /**
     * Insert an array of objects in the database.
     *
     * @param objList the objects to be inserted.
     * @return The SQLite row ids
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(objList: List<T>): List<Long>

    /**
     * Update an object from the database.
     *
     * @param obj the object to be updated
     */
    @Update
    fun update(obj: T): Int

    /**
     * Update an array of objects from the database.
     *
     * @param objList the object to be updated
     */
    @Update
    fun update(objList: List<T>): Int

    /**
     * Delete an object from the database
     *
     * @param obj the object to be deleted
     */
    @Delete
    fun delete(obj: T)

    /**
     * Delete an object from the database
     *
     * @param obj the object to be deleted
     */
    @Delete
    fun delete(obj: List<T>)

    /**
     * Upsert for object
     *
     * @param obj
     */
    @Transaction
    fun upsert(obj: T): Int {
        val id = insert(obj)
        if (id == -1L) {
            return update(obj)
        }

        return id.toInt()
    }
}