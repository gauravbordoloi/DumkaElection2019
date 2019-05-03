package com.gadgetsfury.electionindia.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.gadgetsfury.electionindia.events.ElectionEvent

@Dao
interface EventDao {

    @Query("SELECT isInterested FROM ElectionEvent WHERE id = :id  ORDER BY id DESC")
    fun isInterested(id: Int): Int

    /*@Query("UPDATE ElectionEvent SET isInterested = :isInterested WHERE id = :id")
    fun update(id: Int, isInterested: Int)*/

    @Insert(onConflict = REPLACE)
    fun update(event: ElectionEvent)

    @Query("SELECT * FROM ElectionEvent")
    fun getAll(): List<ElectionEvent>

    @Insert(onConflict = REPLACE)
    fun insertAll(list: List<ElectionEvent>)

    @Query("DELETE FROM ElectionEvent")
    fun clearAll()

}